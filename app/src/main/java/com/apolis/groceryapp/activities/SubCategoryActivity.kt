package com.apolis.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apolis.groceryapp.R
import com.apolis.groceryapp.adapters.FragmentAdapter
import com.apolis.groceryapp.adapters.ProductAdapter
import com.apolis.groceryapp.app.Endpoints
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.Category
import com.apolis.groceryapp.models.Product
import com.apolis.groceryapp.models.Subcategory
import com.apolis.groceryapp.models.SubcategoryResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*

class SubCategoryActivity : AppCompatActivity(){
    lateinit var fragmentAdapter: FragmentAdapter
    lateinit var dbHelper: DBHelper
    var category: Category? = null
    var mList: ArrayList<Subcategory> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        dbHelper = DBHelper(this)

        if(intent.hasExtra(Category.CATEGORY_KEY)){
            category = intent.getSerializableExtra(Category.CATEGORY_KEY) as Category
        }

        setupToolbar()
        init()
    }

    private fun init() {
        getSubcategoryData()
        fragmentAdapter = FragmentAdapter(supportFragmentManager)
    }

    private fun getSubcategoryData() {
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoints.getSubCategoryByCatId(category!!.catId),
            Response.Listener {
                var gson = Gson()
                var subcategoryResponse =
                    gson.fromJson(it, SubcategoryResponse::class.java)
                mList.addAll(subcategoryResponse.data)

                for (i in 0 until mList.size) {
                    fragmentAdapter.addSubcategoryFragments(mList[i])
                }
                view_pager.adapter = fragmentAdapter
                tab_layout.setupWithViewPager(view_pager)
            },
            Response.ErrorListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            })
        requestQueue.add(request)
    }

    private fun setupToolbar(){
        toolbar.title = category?.catName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menu_cart -> startActivity(Intent(this, CartActivity::class.java))
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

}