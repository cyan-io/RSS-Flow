package com.sun.kikyorss.logic

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
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
    return try {
        val date = format.parse(dateString)
        if (date == null)
            dateString
        else
            format2.format(date)
    } catch (e: Exception) {
        dateString
    }
}

/*判断是否为RSS2格式*/
fun isRss2(content: String): Boolean {
    var isRss = false
    val parser = XmlPullParserFactory.newInstance().newPullParser()
    parser.setInput(StringReader(content))
    return try {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "rss")
                isRss = try {
                    parser.getAttributeValue("", "version") == "2.0"
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            eventType = parser.next()
        }
        isRss
    }catch (e:Exception){
        e.printStackTrace()
        false
    }
}