package com.example.myapplication.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 interface AppRetrofitCallBack<T> : Callback<T> {


    override fun onFailure(call: Call<T>, t: Throwable) {
              onError(call,t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful && response.body()!=null){
            onSuccess(call,response)
        }
        else{
            onError(call,Throwable("Response is null"))
        }

    }

    fun onSuccess(call: Call<T>,response: Response<T>)
    fun onError(call: Call<T>,t: Throwable)

}