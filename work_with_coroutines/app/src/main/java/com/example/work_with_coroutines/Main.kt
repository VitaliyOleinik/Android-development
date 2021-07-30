package com.example.work_with_coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private fun log(msg : String) = println(
    "[${Thread.currentThread().name}] $msg"
)

fun main() {
    //networkRequest()

    runBlocking {
        launch {
            makeAPIRequest()
        }
        log("This will be printed first")
    }
    log("Finished!")
}


private suspend fun makeAPIRequest () {
    checkForInternet()
    delay(1000)
    log("API request failed because of missing internet.")
}

private suspend fun checkForInternet(){
    delay(3000)
    log("There is internet")
    delay(1000)
    log("Just kidding, there is no internet")
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