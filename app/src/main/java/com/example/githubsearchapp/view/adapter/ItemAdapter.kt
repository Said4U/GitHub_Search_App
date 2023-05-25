package com.example.githubsearchapp.view.adapter


import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearchapp.R
import com.example.githubsearchapp.data.Item
import com.squareup.picasso.Picasso


class ItemAdapter(val itemList: List<Item>?, val itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onItemClick(id: Int?, url: String?, isUser: Boolean, owner: String, repo: String)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list videoItems to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (itemList?.get(position)?.login == null) {
            holder.imageView.visibility = View.GONE
            holder.score.text = itemList?.get(position)?.forks_count.toString() + " \nForks"
            holder.score.setTextColor(Color.BLACK)
            holder.name.text = itemList?.get(position)?.name.toString()
            holder.description.visibility = View.VISIBLE
            holder.description.text = itemList?.get(position)?.description?.toString() ?: ""

        } else {
            holder.score.visibility = View.VISIBLE
            holder.imageView.visibility = View.VISIBLE
            holder.description.visibility = View.GONE
            holder.score.setTextColor(Color.parseColor("#FFA500"))
            holder.name.text = itemList[position].login
            holder.score.text = itemList[position].score.toString()
            Picasso.get().load(itemList[position].avatar_url).fit().into(holder.imageView)
        }
    }

    // return the number of the videoItems in the list
    override fun getItemCount(): Int {
        return itemList!!.size
    }


    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cardImageView)
        var name: TextView = itemView.findViewById(R.id.name)
        var description: TextView = itemView.findViewById(R.id.description)
        var score: TextView = itemView.findViewById(R.id.score)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
        init {
            ItemView.setOnClickListener {
                Log.i("login", itemList?.get(layoutPosition).toString())
                itemClickListener.onItemClick(
                    id = itemList?.get(layoutPosition)?.id,
                    url = itemList?.get(layoutPosition)?.html_url,
                    isUser = itemList?.get(layoutPosition)?.login != null,
                    owner = itemList?.get(layoutPosition)?.owner?.login ?: "null",
                    repo = itemList?.get(layoutPosition)?.name ?: "null"

                )
            }
        }
    }
}