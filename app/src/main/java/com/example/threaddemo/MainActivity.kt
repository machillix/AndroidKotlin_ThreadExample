package com.example.threaddemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Counter
        var counter = 0
        // How many threads we will create
        var numberOfThreads = 1000

        // Measure time to create 1000 threads
        var time = measureTimeMillis {
            // Create new threads on for loop
            for (i in 0..numberOfThreads){
                thread() {
                    counter++
                }
            }
        }

        Log.v("ToniWesterlund", "Created  $numberOfThreads threads in $time ms")


        // How many coroutines we will create
        var numberOfCoroutines = 1000

        // Measure time to create 1000 coroutines
        time = measureTimeMillis {
            // Create new coroutines on for loop
            for(i in 0..numberOfCoroutines){
                GlobalScope.launch {
                    counter++
                }
            }
        }

        Log.v("ToniWesterlund", "Created  $numberOfCoroutines coroutines in $time ms")

        // How many iteration on for loop
        var forLoopCount = 100000

        time = measureTimeMillis {
            // Now we do samething in main thread
            for (i in 0..forLoopCount){
                counter++
            }

        }

        Log.v("ToniWesterlund", "Do  $forLoopCount iteration in $time ms")
    }
}