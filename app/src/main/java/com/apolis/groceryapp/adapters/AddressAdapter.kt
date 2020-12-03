package com.apolis.groceryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apolis.groceryapp.R
import com.apolis.groceryapp.models.Address
import kotlinx.android.synthetic.main.row_adapter_address.view.*

class AddressAdapter(var mContext: Context) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    var mList: ArrayList<Address> = ArrayList()
    var listener: OnAdapterListener? = null
    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_address, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var address = mList[position]
        holder.onBind(address, position)
    }

    fun setData(addressList: ArrayList<Address>) {
        mList = addressList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(address: Address, position: Int) {
            itemView.text_view_type.text = address.type
            itemView.button_edit.setOnClickListener {
                listener?.onButtonClicked(it, position)
            }
            itemView.radio_button.isChecked = position == checkedPosition       // This puts the value (true/false) and accordingly make it checked or unchecked
            itemView.radio_button.setOnClickListener{
                checkedPosition = position
                notifyDataSetChanged()
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