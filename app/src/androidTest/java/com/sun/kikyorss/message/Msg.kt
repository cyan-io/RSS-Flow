package com.sun.kikyorss.message

import android.os.Looper
import android.util.Log
import com.sun.kikyorss.logic.MyApplication.Companion.appContext
import es.dmoral.toasty.Toasty
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.Disposable

class Msg {
    private lateinit var emitter: ObservableEmitter<MsgType>

    init {
        Observable.create<MsgType>(ObservableOnSubscribe {
            this.emitter = it
        }).subscribe(object : io.reactivex.rxjava3.core.Observer<MsgType> {
            override fun onNext(t: MsgType?) {
                t?.let {
                    when (t) {
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
                        MsgType.INFO -> Toasty.info(appContext, "", Toasty.LENGTH_LONG).show()
                        MsgType.SUCCESS_ADD -> {
                            Looper.prepare()
                            Toasty.success(
                                appContext,
                                "添加成功,请手动刷新",
                                Toasty.LENGTH_SHORT
                            ).show()
                            Looper.loop()
                            Log.i("___", "da")
                        }
                        else -> Toasty.info(appContext, "UNKNOWM", Toasty.LENGTH_LONG).show()
                    }
                }
            }

            override fun onComplete() {}
            override fun onSubscribe(d: Disposable?) {}
            override fun onError(e: Throwable?) {}
        })
    }

    fun sendMsg(msgType: MsgType) {
        emitter.onNext(msgType)
    }
}