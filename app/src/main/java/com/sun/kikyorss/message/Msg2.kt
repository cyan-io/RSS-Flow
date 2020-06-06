package com.sun.kikyorss.message

import com.sun.kikyorss.MyApplication.Companion.context
import es.dmoral.toasty.Toasty

class Msg2 {
    fun sendMsg(msgType: MsgType) {
        when (msgType) {
            MsgType.ERROR_EXIST -> Toasty.error(context, "订阅已存在", Toasty.LENGTH_SHORT)
                .show()
            MsgType.ERROR -> Toasty.error(context, "ERROR", Toasty.LENGTH_LONG).show()
            MsgType.ERROR_NET -> Toasty.error(context, "网络错误", Toasty.LENGTH_LONG)
                .show()
            MsgType.ERROR_WRONG_FEED -> Toasty.error(
                context,
                "错误的订阅地址",
                Toasty.LENGTH_LONG
            )
                .show()
            MsgType.INFO -> Toasty.info(context, "", Toasty.LENGTH_LONG).show()
            MsgType.SUCCESS_ADD -> Toasty.success(
                context,
                "添加成功,请手动刷新",
                Toasty.LENGTH_SHORT
            ).show()
            else -> Toasty.info(context, "UNKNOWM", Toasty.LENGTH_LONG).show()
        }
    }
}