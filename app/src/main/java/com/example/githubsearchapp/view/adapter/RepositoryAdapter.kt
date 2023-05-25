package com.example.githubsearchapp.view.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearchapp.R
import com.example.githubsearchapp.data.Content
import com.example.githubsearchapp.data.Entry


class RepositoryAdapter(val contentList: List<Content>?, val itemClickListener: ItemClickListener, val context: Context)
    : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onItemClick(path: String, type: String, url: String)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list videoItems to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (contentList?.get(position)?.type == "dir"){
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_folder_24))
        } else {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_insert_drive_file_24))
        }
        holder.name.text = contentList?.get(position)?.name.toString()
        }


    // return the number of the videoItems in the list
    override fun getItemCount(): Int {
        return contentList!!.size
    }


    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cardImageViewFile)
        var name: TextView = itemView.findViewById(R.id.nameFile)
        init {
            ItemView.setOnClickListener {
                itemClickListener.onItemClick(
                    path = contentList!![layoutPosition].path,
                    type = contentList[layoutPosition].type,
                    url = contentList[layoutPosition].html_url
                )
            }
        }
    }
}