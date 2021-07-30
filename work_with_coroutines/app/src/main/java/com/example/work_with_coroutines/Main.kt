package com.example.work_with_coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun log(msg : String) = println(
    "[${Thread.currentThread().name}] $msg"
)

fun main() {
    networkRequest()
}

private fun networkRequest(){
    GlobalScope.launch {
        log("Making network request")

        for (i in 1..3) {
            delay(1000)
            println("First: $i")
        }

        log("First network request made!")
    }

    GlobalScope.launch {
        delay(500)
        log("Making network request")

        for (i in 1..3) {
            delay(1000)
            println("Second: $i")
        }

        log("Second network request made!")
    }

    Thread.sleep(4000)

    log("Done")
}