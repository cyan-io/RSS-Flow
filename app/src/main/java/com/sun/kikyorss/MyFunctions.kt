package com.sun.kikyorss

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/*调整webView图片大小*/
fun resizeHtmlImg(htmltext: String): String {
    /*使用Jsoup对html文本进行解析，改变图片大小以适应手机
    * implementation 'org.jsoup:jsoup:1.13.1'*/
    val doc: Document? = Jsoup.parse(htmltext);
    val elements = doc?.getElementsByTag("img");
    if (elements != null) {
        for (element in elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
    }
    return doc.toString();
}

/*格式化日期*/
fun formatDate(dateString: String, formatString: String = "yyyy/MM/dd HH:mm:ss"): String {
    val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
    val format2 = SimpleDateFormat(formatString, Locale.getDefault())
    try {
        val date = format.parse(dateString)
        if (date == null)
            return dateString
        else
            return format2.format(date)
    } catch (e: Exception) {
        return dateString
    }
}

/*判断是否为RSS2格式*/
fun isRss2(content: String): Boolean {
    var isrss2 = false
    val parser = XmlPullParserFactory.newInstance().newPullParser()
    parser.setInput(StringReader(content))
    try {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "rss")
                try {
                    isrss2 = parser.getAttributeValue("", "version") == "2.0"
                } catch (e: java.lang.Exception) {
                    isrss2 = false
                }
            eventType = parser.next()
        }
        return isrss2
    }catch (e:java.lang.Exception){
        return false
    }
}

/*检查更新*/
fun checkUpdate(view: View,activity: Activity){
    val client= MyOkHttp.getClient()
    val request= MyOkHttp.getRequset(Configurations.updateUrl)
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response){
            val parser = XmlPullParserFactory.newInstance().newPullParser()
            parser.setInput(StringReader(response.body?.string()?:""))
            var eventType=parser.eventType
            var latestVer:Int=0
            while (eventType!=XmlPullParser.END_DOCUMENT){
                if(parser.eventType==XmlPullParser.START_TAG&&parser.name=="latestVersion")
                    latestVer=parser.nextText().toInt()
                eventType=parser.next()
            }
            Log.i("__update",latestVer.toString())
            if(latestVer>Configurations.currentVersion){
                Snackbar.make(view,"新版本已推出，是否下载更新", Snackbar.LENGTH_SHORT).setAction("是"
                ) {
                    val uri: Uri = Uri.parse(Configurations.releasePage)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    activity.startActivity(intent)
                }.show()
            }
        }
        override fun onFailure(call: Call, e: IOException) {
            Log.i("__update","onFailure")
        }
    })
}

/*规范化url*/
fun formatUrl(url:String):String{
    if(url.isNotBlank())
        if(url.last()!='/')
            return "$url/"
    return url
}