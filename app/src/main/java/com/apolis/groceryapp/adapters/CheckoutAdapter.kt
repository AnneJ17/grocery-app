package com.apolis.groceryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.groceryapp.R
import com.apolis.groceryapp.models.Product
import kotlinx.android.synthetic.main.row_adapter_checkout.view.*

class CheckoutAdapter(var mContext: Context, var mList: ArrayList<Product>) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_checkout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = mList[position]
        holder.onBind(item)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(item: Product) {
            itemView.text_view_title.text = item.productName
            itemView.text_view_quantity.text = item.quantity.toString()
            itemView.text_view_price.text = item.price.toString()
        }
    }
}