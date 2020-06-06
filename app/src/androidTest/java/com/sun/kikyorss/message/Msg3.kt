package com.sun.kikyorss.message

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sun.kikyorss.MyApplication
import es.dmoral.toasty.Toasty
import java.lang.Exception
import java.nio.channels.Channel
import kotlin.math.sign

class Msg3(private val lifecycleOwner: LifecycleOwner) {
    private val signal = MutableLiveData<MsgType>()

    fun sendMsg(msgType: MsgType) {
        try {
            signal.postValue(msgType)
        } catch (e: Exception) {
            signal.value = msgType
        }
    }

    init {
        signal.observe(lifecycleOwner, Observer {
            if (it != null)
                when (it) {
                    MsgType.ERROR_EXIST -> Toasty.error(
                        MyApplication.context,
                        "订阅已存在",
                        Toasty.LENGTH_SHORT
                    )
                        .show()
                    MsgType.ERROR -> Toasty.error(
                        MyApplication.context,
                        "ERROR",
                        Toasty.LENGTH_LONG
                    ).show()
                    MsgType.ERROR_NET -> Toasty.error(
                        MyApplication.context,
                        "网络错误",
                        Toasty.LENGTH_LONG
                    )
                        .show()
                    MsgType.ERROR_WRONG_FEED -> Toasty.error(
                        MyApplication.context,
                        "错误的订阅地址",
                        Toasty.LENGTH_LONG
                    )
                        .show()
                    MsgType.INFO -> {/*todo*/
                    }
                    MsgType.SUCCESS_ADD -> {
                        Looper.prepare()

                        Toasty.success(
                            MyApplication.context,
                            "添加成功,请手动刷新",
                            Toasty.LENGTH_SHORT
                        ).show()
                        Looper.loop()
                    }
                    else -> Toasty.info(MyApplication.context, "UNKNOWM", Toasty.LENGTH_LONG).show()
                }
        })
    }
}