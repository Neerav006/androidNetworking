package com.carehome.patient.retrofit

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by BB on 23-01 023.
 */
    /**
     * Excepts the an object of lifecycle owner (activity,fragment etc) as the first argument so that,
     * call will continue till the lifecycle is active.
     * Once the lifecycle finishes, the existing call will be cancelled
     * Note: Cancelling an running call will call onFailure method,
     *       so you should handle your cancelled call properly in onFailure
     */
    fun <T> Call<T>.enqueue(lifeCycle: LifecycleOwner, callback: Callback<T>) {
        // add an observer to activity which will cancel an ongoing call
        // when activity is destroyed
        lifeCycle.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun cancelCalls() {
               this@enqueue.cancel()
            }
        })
        this.enqueue(callback)
    }