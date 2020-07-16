package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.fragments.FragmentA
import com.example.myapplication.model.NewsResponse
import com.example.myapplication.retrofit.ApiClient
import com.example.myapplication.retrofit.AppRetrofitCallBack
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

     val TAG = MainActivity::class.java.simpleName

     var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.container,FragmentA(),FragmentA::class.simpleName).commit()

        btnFirst.setOnClickListener {

            val intent = Intent(this,PaginationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e(TAG,"new intent called")
    }

    fun getNewsApi(){
        ApiClient.service.getNews(page,10).enqueue(object :Callback<NewsResponse.Response>{
            override fun onFailure(call: Call<NewsResponse.Response>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<NewsResponse.Response>,
                response: Response<NewsResponse.Response>
            ) {

            }


        })
    }


    fun getNextNewsApi(){
        ApiClient.service.getNews(page,10).enqueue(object :Callback<NewsResponse.Response>{
            override fun onFailure(call: Call<NewsResponse.Response>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<NewsResponse.Response>,
                response: Response<NewsResponse.Response>
            ) {

            }


        })
    }
}