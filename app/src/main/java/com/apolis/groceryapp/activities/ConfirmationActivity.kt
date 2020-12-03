package com.apolis.groceryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.apolis.groceryapp.R
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.app_bar.*

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        init()
    }

    private fun init() {
        setupToolbar()

        text_view_receipt.setOnClickListener {
            startActivity(Intent(this, ReceiptActivity::class.java))
            finish()
        }
    }

    private fun setupToolbar() {
        toolbar.title = "Order Confirmation"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        return true
    }
}