package com.apolis.groceryapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apolis.groceryapp.R
import com.apolis.groceryapp.app.Config
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_adapter_cart.view.*

class CartAdapter(var mContext: Context) : RecyclerView.Adapter<CartAdapter.ViewHolder>(){

    var mList: ArrayList<Product> = ArrayList()
    var dbHelper = DBHelper(mContext)
    var listener: OnAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = mList[position]
        holder.onBind(item, position)
    }

    fun setData(cartItems: ArrayList<Product>) {
        mList = cartItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        var item = mList[position]
        dbHelper.deleteItem(item._id)
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(item: Product, position: Int) {
            itemView.text_view_product_name.text = item.productName
            itemView.text_view_price.text = "₹"+ item.price.toString()
            itemView.text_view_mrp.text = " ₹"+ item.mrp.toString() + " "
            itemView.text_view_quantity.text = item.quantity.toString()
            Picasso
                .get()
                .load("${Config.IMAGE_URL + item.image}")
                .placeholder(R.drawable.orangee)
                .into(itemView.image_view)
            itemView.button_add.setOnClickListener{
                listener?.onButtonClicked(it, position)
            }
            itemView.button_minus.setOnClickListener {
                listener?.onButtonClicked(it, position)
            }
        }
    }

    interface OnAdapterListener {
        fun onButtonClicked(view: View, position: Int)
    }

    fun setOnAdapterListener(onAdapterListener: OnAdapterListener) {
        listener = onAdapterListener
    }

}