package com.apolis.groceryapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.apolis.groceryapp.R
import com.apolis.groceryapp.activities.ProductDetailActivity
import com.apolis.groceryapp.app.Config
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.view.*
import kotlinx.android.synthetic.main.row_adapter_product.view.*
import kotlinx.android.synthetic.main.row_adapter_product.view.button_add_to_cart
import kotlinx.android.synthetic.main.row_adapter_product.view.image_view
import kotlinx.android.synthetic.main.row_adapter_product.view.text_view_mrp
import kotlinx.android.synthetic.main.row_adapter_product.view.text_view_price

class ProductAdapter(var mContext: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var mList: ArrayList<Product> = ArrayList()
    var listener: OnAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(mContext).inflate(R.layout.row_adapter_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product = mList[position]
        holder.onBind(product, position)
    }

    fun setData(data: java.util.ArrayList<Product>) {
        mList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(product: Product, position: Int) {
            itemView.text_view_name.text = product.productName
            itemView.text_view_price.text = "₹" + product.price.toString()
            itemView.text_view_mrp.text = " ₹" + product.mrp.toString() + " "
            itemView.text_view_quantity.text = product.quantity.toString()
            Picasso
                .get()
                .load("${Config.IMAGE_URL + product.image}")
                .placeholder(R.drawable.orangee)
                .into(itemView.image_view)

            itemView.setOnClickListener {
                var intent = Intent(mContext, ProductDetailActivity::class.java)
                intent.putExtra(Product.KEY_PRODUCT, product)
                mContext.startActivity(intent)
            }

            checkQuantity(itemView, product)

            itemView.button_add_to_cart.setOnClickListener {
                listener?.onButtonClicked(it, position)
                itemView.quantity_layout.visibility = View.VISIBLE
                itemView.button_add_to_cart.visibility = View.GONE
                product.quantity++
                itemView.text_view_quantity.text = product.quantity.toString()
            }

            itemView.button_add.setOnClickListener {
                checkQuantity(itemView, product)
                listener?.onButtonClicked(it, position)
            }

            itemView.button_minus.setOnClickListener {
                checkQuantity(itemView, product)
                listener?.onButtonClicked(it, position)
            }
        }
    }

    private fun checkQuantity(itemView: View, product: Product) {
        if (product.quantity == 0) {
            DBHelper(mContext).deleteItem(product._id)
            itemView.quantity_layout.visibility = View.GONE
            itemView.button_add_to_cart.visibility = View.VISIBLE
        } else {
            itemView.quantity_layout.visibility = View.VISIBLE
            itemView.button_add_to_cart.visibility = View.GONE
        }
    }

    interface OnAdapterListener {
        fun onButtonClicked(view: View, position: Int)
    }

    fun setOnAdapterListener(onAdapterListener: OnAdapterListener) {
        listener = onAdapterListener
    }
}