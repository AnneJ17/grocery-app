package com.apolis.groceryapp.models

import java.io.Serializable

data class SubcategoryResponse(
    val count: Int,
    val data: ArrayList<Subcategory>,
    val error: Boolean
)

data class Subcategory(
    val __v: Int,
    val _id: String,
    val catId: Int,
    val position: Int,
    val status: Boolean,
    val subDescription: String,
    val subId: Int,
    val subImage: String,
    val subName: String
) : Serializable {
    companion object {
        const val KEY_SUB_ID = "subId"
    }
}