package com.sun.kikyorss

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun p1() {
    println(1)
    println(2)
    println(3)
}

fun p2() {
    println(4)
    println(5)
    println(6)
}

fun main() {
    runBlocking {
        launch {
            p1()
            delay(1000)
        }
        launch { p2() }
    }
    //Thread.sleep(100)
}