package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carehome.patient.retrofit.enqueue
import com.example.myapplication.model.NewsResponse
import com.example.myapplication.retrofit.ApiClient
import com.example.myapplication.retrofit.AppRetrofitCallBack
import kotlinx.android.synthetic.main.activity_pagination.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.myapplication.ItemClickListener as ItemClickListener

class PaginationActivity : AppCompatActivity(), ItemClickListener {

    var page = 1
    var isLoading = false
    var VISIBLE_THRESOLD = 2
    val newsListArrayList = ArrayList<NewsResponse.News>()
    lateinit var customAdapter: CustomAdapter
    lateinit var newsApiCall: Call<NewsResponse.Response>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagination)

        rvList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        customAdapter = CustomAdapter(this@PaginationActivity,newsListArrayList,this)
        rvList.adapter = customAdapter
        setupScrollListener()

        getNextNewsApi()

    }

    fun getNewsApi(){
        isLoading = true
        progressBar?.visibility = View.VISIBLE
        newsApiCall = ApiClient.service.getNews(page,10)
        newsApiCall.enqueue(this,object : Callback<NewsResponse.Response> {
            override fun onFailure(call: Call<NewsResponse.Response>, t: Throwable) {
                     isLoading =false
                progressBar?.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<NewsResponse.Response>,
                response: Response<NewsResponse.Response>
            ) {
                isLoading =false
                progressBar?.visibility = View.GONE
                if (response.isSuccessful && response.body()!=null){
                    page++
                    newsListArrayList.addAll(response.body()!!.news)
                    customAdapter.notifyDataSetChanged()

                }
            }


        })
    }


    fun getNextNewsApi(){
        isLoading = true
        progressBar?.visibility = View.VISIBLE
        newsApiCall = ApiClient.service.getNews(page,10)
        newsApiCall.enqueue(this,/*object : Callback<NewsResponse.Response> {
            override fun onFailure(call: Call<NewsResponse.Response>, t: Throwable) {
               isLoading =false
                progressBar?.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<NewsResponse.Response>,
                response: Response<NewsResponse.Response>
            ) {
                isLoading = false
                progressBar?.visibility = View.GONE
                page++
                if (response.isSuccessful && response.body()!=null){
                    newsListArrayList.addAll(response.body()!!.news)
                    customAdapter.notifyDataSetChanged()
                }
            }


        }*/
        object :AppRetrofitCallBack<NewsResponse.Response>{
            override fun onSuccess(
                call: Call<NewsResponse.Response>,
                response: Response<NewsResponse.Response>
            ) {
                isLoading = false
                progressBar?.visibility = View.GONE
                page++
                newsListArrayList.addAll(response.body()!!.news)
                customAdapter.notifyDataSetChanged()
            }

            override fun onError(call: Call<NewsResponse.Response>, t: Throwable) {
                isLoading =false
                progressBar?.visibility = View.GONE
            }

        }
        )
    }

    private fun setupScrollListener() {
        val layoutManager = rvList.layoutManager as LinearLayoutManager
        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading &&  lastVisibleItem + VISIBLE_THRESOLD >= totalItemCount){
                    // load more
                    isLoading = true
                    getNextNewsApi()
                }

            }
        })
    }

    override fun onListItemClick(pos: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}