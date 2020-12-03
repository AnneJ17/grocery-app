package com.apolis.groceryapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.adapters.ProductAdapter
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.Product
import com.apolis.groceryapp.models.ProductResponse
import com.apolis.groceryapp.models.Subcategory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_subcategory.*
import kotlinx.android.synthetic.main.fragment_subcategory.view.*


class SubcategoryFragment : Fragment(), ProductAdapter.OnAdapterListener {

    private var subId: Int? = null
    lateinit var productAdapter: ProductAdapter
    lateinit var dbHelper: DBHelper
    var mList: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getInt(Subcategory.KEY_SUB_ID)
        }
        dbHelper = DBHelper(activity!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_subcategory, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        getProductData()
        productAdapter = ProductAdapter(activity!!)
        productAdapter.setOnAdapterListener(this)
        view.recycler_view_subcategory.layoutManager = LinearLayoutManager(activity)
        view.recycler_view_subcategory.adapter = productAdapter
    }

    private fun getProductData() {
        var request = StringRequest(
            Request.Method.GET,
            Endpoints.getProductBySubId(subId!!),
            Response.Listener {
                indeterminate_progress_bar.visibility = View.GONE
                var gson = Gson()
                var productResponse = gson.fromJson(it, ProductResponse::class.java)
                mList = productResponse.data
                for(i in 0 until mList.size) {
                    mList[i].quantity = 0
                }
                productAdapter.setData(mList)
            },
            Response.ErrorListener {  }
        )
        Volley.newRequestQueue(activity).add(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            SubcategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(Subcategory.KEY_SUB_ID, param1)
                }
            }
    }

    override fun onButtonClicked(view: View, position: Int) {
        var item = mList[position]

        if(!dbHelper.isItemInCart(item)) {
            dbHelper.addItem(item)
        }

        when(view.id) {
            R.id.button_add -> {
                item.quantity++
                updateItemChanged(item, position)
            }
            R.id.button_minus -> {
                if(item.quantity > 0) {
                    item.quantity--
                    updateItemChanged(item, position)
                }
            }
        }
    }

    private fun updateItemChanged(item: Product, position: Int) {
        dbHelper.updateQuantity(item)
        mList[position] = item
        productAdapter.notifyItemChanged(position, item)
    }
}