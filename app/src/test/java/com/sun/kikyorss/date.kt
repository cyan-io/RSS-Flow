package com.sun.kikyorss

import io.reactivex.rxjava3.core.*
import kotlin.concurrent.thread


fun main(){
    val fl= MyRxJa()
    fl.flow.subscribe(io.reactivex.rxjava3.functions.Consumer {
        Thread.sleep(100)
        if(it!=null)
            println(it.toString())
    })

    repeat(1000000){
        thread {
            fl.emitter.onNext(Pair(it.toString(),it%3==0))
        }
    }
}


fun main2(){/*
    val observable= Observable.create(object : ObservableOnSubscribe<String> {
        override fun subscribe(emitter: ObservableEmitter<String>?) {
            print("yes")
            emitter?.let {

            }
        }
    }).subscribe(object :Observer<String>{
        override fun onNext(t: String?) {
        }

        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable?) {
        }

        override fun onError(e: Throwable?) {
        }
    })*/

    var count=0

    /*val flowable=Flowable.create(FlowableOnSubscribe<String> {
        val emitter=it
        repeat(1000){
            thread {

                emitter.onNext(it.toString())
            }
        }
        emitter.onComplete()
    },BackpressureStrategy.BUFFER).subscribe(io.reactivex.rxjava3.functions.Consumer {
        count+=1
        println(it)

    })*/

    val flowable=Flowable.create(FlowableOnSubscribe<String> {
        val emitter=it
        repeat(1000){
            thread {

                emitter.onNext(it.toString())
            }
        }
        emitter.onComplete()
    },BackpressureStrategy.BUFFER).subscribe()

}



