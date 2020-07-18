package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.NewsResponse

class NewsListAdapter(private val context: Context,private val newsList: ArrayList<NewsResponse.News>,private val onItemClick:(NewsResponse.News,Int)->Unit)
    :RecyclerView.Adapter<NewsListAdapter.ViewHolder>(),Filterable{

    var newsListFiltered:ArrayList<NewsResponse.News> = newsList

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val tvName: TextView=  view.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.text_row_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsListFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = newsListFiltered[position].title+" "+position.toString()
        holder.itemView.setOnClickListener {
             onItemClick(newsListFiltered[position],position)
        }
    }

    override fun getFilter(): Filter {
          return object : Filter(){
              override fun performFiltering(constraint: CharSequence?): FilterResults {
                      val query = constraint?.toString()?.trim()?.toLowerCase()?:""
                  newsListFiltered = if (query.isEmpty()){
                      newsList
                  } else{
                      val tempNewsList = ArrayList<NewsResponse.News>()
                      for (item in newsList){
                          if (item.title.trim().toLowerCase().contains(query)){
                              tempNewsList.add(item)
                          }
                      }
                      tempNewsList
                  }

                  val filterResult  = FilterResults()
                  filterResult.values = newsListFiltered
                  return filterResult

              }

              override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                      newsListFiltered = results?.values as ArrayList<NewsResponse.News>
                      notifyDataSetChanged()
              }

          }
    }

}