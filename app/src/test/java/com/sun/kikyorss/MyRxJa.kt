package com.sun.kikyorss

import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import io.reactivex.rxjava3.core.FlowableOnSubscribe

class MyRxJa {
    lateinit var flow: Flowable<Pair<String,Boolean>>
    lateinit var emitter: FlowableEmitter<Pair<String, Boolean>>
    init {
        flow=Flowable.create({ emitter ->
            if (emitter != null) {
                this@MyRxJa.emitter=emitter
            }
        },BackpressureStrategy.BUFFER)
    }
}