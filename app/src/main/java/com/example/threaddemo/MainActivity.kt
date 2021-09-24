package com.example.threaddemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.threaddemo.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

const val HELLO_MSG = 1
const val PING_PONG_MSG = 2
const val QUIT_MSG = 3

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var mainThreadHandler : Handler? = null

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


        // Create and start Thread
        val mySimpleThread = SimpleThread()
        mySimpleThread.start()

        // Create and start runnable (Thread)
        val mySimpleRunnable = SimpleRunnable()
        val myThread = Thread(mySimpleRunnable)
        myThread.start()

        mainThreadHandler = object : Handler(Looper.getMainLooper()){

            override fun handleMessage(msg: Message) {
                if(HELLO_MSG == msg.what){
                    Log.v("ToniWesterlund" , "Hello World ${Thread.currentThread()}")
                }
            }
        }

        val myWorkerRunnable = WorkerRunnable(mainThreadHandler)
        val myWorkerThread = Thread(myWorkerRunnable)


        // Start Thread
        binding.startButton.setOnClickListener{
            myWorkerThread.start()
        }
        // Send Hello Message
        binding.helloButton.setOnClickListener{
            val msg = Message()
            msg.what = HELLO_MSG
            msg.obj = "Hello"
            myWorkerRunnable.workerRunnableHandler!!.sendMessage(msg)

        }
        // Send Ping Pong Message
        binding.pingButton.setOnClickListener{
            val msg = Message()
            msg.what = PING_PONG_MSG
            msg.obj = "PING PONG"
            myWorkerRunnable.workerRunnableHandler!!.sendMessage(msg)
        }
        // Send Quit Message
        binding.quitButton.setOnClickListener{
            val msg = Message()
            msg.what = QUIT_MSG
            msg.obj = "Quit"
            myWorkerRunnable.workerRunnableHandler!!.sendMessage(msg)
        }
    }
}

class WorkerRunnable(val mainThreadHandler : Handler?) : Runnable{

    var workerRunnableHandler : Handler? = null

    override fun run() {
        Log.v("ToniWesterlund", "Run Start")

        // Loop start Point
        Looper.prepare()

        workerRunnableHandler = object  : Handler(Looper.myLooper()!!){
            override fun handleMessage(msg: Message) {

                // Handle Messages
                if(HELLO_MSG == msg.what){
                    Log.v("ToniWesterlund", "Hello World ${Thread.currentThread()} ")
                }else if(PING_PONG_MSG == msg.what){
                    Log.v("ToniWesterlund", "PING")
                    val msgReply = Message()
                    msgReply.what = HELLO_MSG
                    msgReply.obj = "Hello"

                    // Reply Message back to main thread
                    mainThreadHandler!!.sendMessage(msgReply)

                }else if(QUIT_MSG == msg.what){
                    Log.v("ToniWesterlund", "QUIT_MSG")
                    Looper.myLooper()?.quit()
                }

            }
        }
        // Loop End Point
        Looper.loop()

    }

}


// SimpleThread Class, Inherited from Thread class
class SimpleThread : Thread(){

    public override fun run() {
        Log.v("ToniWesterlund", "Thread Run - ${Thread.currentThread()} has run")
    }
}

// SimpleRunnable, Implements Runnable interface
class SimpleRunnable : Runnable{
    override fun run() {
        Log.v("ToniWesterlund", "Runnable Run - ${Thread.currentThread()} has run")
    }

}