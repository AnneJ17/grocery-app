package com.apolis.groceryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.groceryapp.R
import com.apolis.groceryapp.models.Receipt
import kotlinx.android.synthetic.main.row_adapter_receipt.view.*
import java.text.SimpleDateFormat

class ReceiptAdapter(var mContext: Context): RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {

    var mList: ArrayList<Receipt> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_receipt, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var receipt = mList[position]
        holder.onBind(receipt)
    }

    fun setData(data: ArrayList<Receipt>) {
        mList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(receipt: Receipt) {
            var parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            var formatter = SimpleDateFormat("MM.dd.yyyy")
            var timeFormatter = SimpleDateFormat("HH:mm")
            var date = formatter.format(parser.parse(receipt.date))
            var time = timeFormatter.format(parser.parse(receipt.date))
            itemView.text_view_date.text = ":  " + date
            itemView.text_view_time.text = ":  " + time
            itemView.text_view_status.text = ":  " + receipt.orderStatus
            itemView.text_view_num.text = ":  " + receipt._id
            itemView.text_view_count.text = ":  " + receipt.products.size
            itemView.text_view_amount.text = ":  ₹" + receipt.orderSummary.ourPrice
            itemView.text_view_delivery_charges.text = ":  ₹" + receipt.orderSummary.deliveryCharges
            itemView.text_view_paid.text =  "₹" + receipt.orderSummary.ourPrice
        }
    }
}