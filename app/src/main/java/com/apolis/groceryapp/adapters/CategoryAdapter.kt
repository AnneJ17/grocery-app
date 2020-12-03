package com.apolis.groceryapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.groceryapp.R
import com.apolis.groceryapp.activities.SubCategoryActivity
import com.apolis.groceryapp.app.Config
import com.apolis.groceryapp.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_slider_item.view.*
import kotlinx.android.synthetic.main.row_adapter_category.view.*

class CategoryAdapter(var mContext: Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

    var mList: ArrayList<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var category = mList[position]
        holder.bind(category)
    }

    fun setData(data: ArrayList<Category>) {
        mList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            itemView.text_view_title.text = category.catName

            itemView.setOnClickListener {
                var intent = Intent(mContext, SubCategoryActivity::class.java)
                intent.putExtra(Category.CATEGORY_KEY, category)
                mContext.startActivity(intent)
            }
            Picasso
                .get()
                .load("${Config.IMAGE_URL + category.catImage}")
                .placeholder(R.drawable.orangee)
                .into(itemView.image_view)
        }
    }
}