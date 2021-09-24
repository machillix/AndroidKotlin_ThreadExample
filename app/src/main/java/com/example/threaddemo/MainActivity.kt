package com.example.threaddemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        Log.v("ToniWesterlund", "Created  ${numberOfThreads} threads in ${time} ms")


    }
}