package com.apolis.groceryapp.models

data class ReceiptResponse(
    val count: Int,
    val data: ArrayList<Receipt>,
    val error: Boolean
)

data class Receipt(
    val __v: Int,
    val _id: String,
    val date: String,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val products: List<Product>,
    val shippingAddress: ShippingAddress,
    val user: User,
    val userId: String
)