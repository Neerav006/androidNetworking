package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carehome.patient.retrofit.enqueue
import com.example.myapplication.adapter.NewsListAdapter
import com.example.myapplication.database.NewsDatabase
import com.example.myapplication.database.NewsRepository
import com.example.myapplication.model.NewsResponse
import com.example.myapplication.model.room.NewsRoom
import com.example.myapplication.retrofit.ApiClient
import com.example.myapplication.retrofit.AppRetrofitCallBack
import kotlinx.android.synthetic.main.activity_pagination.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.myapplication.ItemClickListener as ItemClickListener

class PaginationActivity : AppCompatActivity(), ItemClickListener {

    var page = 1
    var isLoading = false
    var VISIBLE_THRESOLD = 2
    val newsListArrayList = ArrayList<NewsResponse.News>()
    val filteredNewsList = ArrayList<NewsResponse.News>()
    lateinit var customAdapter: CustomAdapter
    lateinit var newsListAdapter: NewsListAdapter
    lateinit var newsApiCall: Call<NewsResponse.Response>
    lateinit var searchView:SearchView
    var searchMode =false

    private lateinit var newsDatabase: NewsDatabase
    private lateinit var newsRepository: NewsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagination)

        newsDatabase = NewsDatabase.getDatabase(applicationContext)
        newsRepository = NewsRepository(newsDatabase.newsDao())
      //  deleteNews()


        setSupportActionBar(tool_bar)

       /* rvList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        customAdapter = CustomAdapter(this@PaginationActivity,filteredNewsList,this)
        rvList.adapter = customAdapter*/

        rvList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        newsListAdapter = NewsListAdapter(this@PaginationActivity,newsListArrayList) { news, i -> onItemClick(news,i) }
        rvList.adapter = newsListAdapter

        // fetch data from local if no network
       // fetchDataFromLocal()
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
                    newsListAdapter.notifyDataSetChanged()

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
                filteredNewsList.addAll(response.body()!!.news)

                insertNewsToRoom(newsListArrayList)

                newsListAdapter.notifyDataSetChanged()
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
                val lastItemPosition =  layoutManager.findLastCompletelyVisibleItemPosition()

                /*if (!isLoading &&  lastVisibleItem + VISIBLE_THRESOLD >= totalItemCount){
                    // load more
                    isLoading = true
                    getNextNewsApi()
                }*/

                if (!isLoading &&  lastItemPosition == filteredNewsList.size-1){
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

    //search view
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_view, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setSubmitButtonEnabled(true)
        searchView.setQueryHint("Search")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                 if (newsListArrayList.isEmpty()){
                     return false
                 }

                 /*filteredNewsList.clear()
                 if (newText.trim().toLowerCase().isNotEmpty()){
                     for (item in newsListArrayList){
                         if (item.title.toLowerCase().contains(newText.trim().toLowerCase())){
                             filteredNewsList.add(item)
                         }
                     }
                 }
                else{
                     for (item in newsListArrayList){
                         filteredNewsList.add(item)
                     }
                 }
                 newsListAdapter.notifyDataSetChanged()*/

                // using filterable
                newsListAdapter.filter.filter(newText)


                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                newsListAdapter.filter.filter(query)
                return false
            }

        })
        searchView.setOnCloseListener {
            Log.e("close","search_view_closed")
            return@setOnCloseListener false
        }


        return super.onCreateOptionsMenu(menu)
    }

   private fun onItemClick(news: NewsResponse.News,pos: Int){
         Toast.makeText(this,news.title,Toast.LENGTH_SHORT).show()
    }

    private fun insertNewsToRoom(newsList: List<NewsResponse.News>){
        GlobalScope.launch {
            val newsRoomList = ArrayList<NewsRoom>()
            for (item in newsList){
                val news = NewsRoom(title = item.title)
                newsRoomList.add(news)
            }
            try {
                newsRepository.deleteAllNews()
                newsRepository.insertAll(newsRoomList)
                val roomList = newsRepository.getAllNews()
                Log.e("room data size",roomList.size.toString())
                if (roomList.isNotEmpty()){
                    newsListArrayList.clear()
                    for (item in roomList){
                        val model = NewsResponse.News(title = item.title,image = "")
                        newsListArrayList.add(model)
                    }
                    newsListAdapter.notifyDataSetChanged()
                }



            }
            catch (e:Exception){

            }


        }
    }

    private fun deleteNews(){
        GlobalScope.launch {
            newsRepository.deleteAllNews()

        }
    }

    private fun fetchDataFromLocal(){

        GlobalScope.launch {

            val roomList = newsRepository.getAllNews()
           // Log.e("room data size",roomList.size.toString())
            if (roomList.isNotEmpty()){
                newsListArrayList.clear()
                for (item in roomList){
                    val model = NewsResponse.News(title = item.title,image = "")
                    newsListArrayList.add(model)
                }
                newsListAdapter.notifyDataSetChanged()
            }
        }



    }
}