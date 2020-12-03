package com.apolis.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.apolis.groceryapp.R
import com.apolis.groceryapp.app.Config
import com.apolis.groceryapp.database.DBHelper
import com.apolis.groceryapp.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar.*

class ProductDetailActivity : AppCompatActivity() {

    var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        product = intent.getSerializableExtra(Product.KEY_PRODUCT) as Product

        setupToolbar()
        init()
    }

    private fun init() {
        Picasso
            .get()
            .load("${Config.IMAGE_URL + product!!.image}")
            .placeholder(R.drawable.orangee)
            .into(image_view)
        text_view_price.text = "₹${product!!.price}"
        text_view_mrp.text = " ₹${product!!.mrp} "
        text_view_description.text = product!!.description

        button_add_to_cart.setOnClickListener {
            var dbHelper = DBHelper(this)
            // Add the item to the database in the cart table
            product!!.quantity++
            dbHelper.addItem(product!!)

            // Advancing to shopping cart page
            var intent = Intent(this, CartActivity::class.java)
            this.finish()
            startActivity(intent)
        }
    }

    private fun setupToolbar(){
        toolbar.title = product!!.productName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

}