package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.NewsResponse

class CustomAdapter(val context:Context, val data:ArrayList<NewsResponse.News>, val itemClickListener: ItemClickListener):
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
   inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView

        init {
            tvName = v.findViewById(R.id.tvName)

            v.setOnClickListener {
                itemClickListener.onListItemClick(adapterPosition)
            }
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.tvName.text = data[position].title+" "+position.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.size

}