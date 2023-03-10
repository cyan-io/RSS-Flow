package com.sun.kikyorss.message

import android.os.Looper
import com.sun.kikyorss.logic.MyApplication.Companion.appContext
import es.dmoral.toasty.Toasty

class Msg2 {
    fun sendMsg(msgType: MsgType) {
        //
        when (msgType) {
            MsgType.ERROR_EXIST -> Toasty.error(appContext, "订阅已存在", Toasty.LENGTH_SHORT)
                .show()
            MsgType.ERROR -> Toasty.error(appContext, "ERROR", Toasty.LENGTH_LONG).show()
            MsgType.ERROR_NET -> Toasty.error(appContext, "网络错误", Toasty.LENGTH_LONG)
                .show()
            MsgType.ERROR_WRONG_FEED -> Toasty.error(
                appContext,
                "错误的订阅地址",
                Toasty.LENGTH_LONG
            )
                .show()
            MsgType.INFO -> {/*todo*/
            }
            MsgType.SUCCESS_ADD -> {
                Looper.prepare()

                Toasty.success(
                    appContext,
                    "添加成功,请手动刷新",
                    Toasty.LENGTH_SHORT
                ).show()
                Looper.loop()
            }
            else -> Toasty.info(appContext, "UNKNOWM", Toasty.LENGTH_LONG).show()
        }
        Looper.loop()
    }
}