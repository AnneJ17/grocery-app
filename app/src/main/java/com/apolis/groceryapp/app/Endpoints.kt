package com.apolis.groceryapp.app

class Endpoints {
    companion object {
        private val URL_USER = "users/"
        private val URL_CATEGORY = "category"
        private val URL_SUBCATEGORY = "subcategory/"
        private val URL_PRODUCT_BY_SUB_ID = "products/sub/"
        private val URL_REGISTER = "auth/register"
        private val URL_LOGIN = "auth/login"
        private val URL_ADDRESS = "address/"
        private val URL_ORDER = "orders/"

        fun putUser(id: String): String {
            return "${Config.BASE_URL + URL_USER + id}"
        }

        fun getCategory(): String {
            return "${Config.BASE_URL + URL_CATEGORY}"
        }

        fun getSubCategoryByCatId(catId: Int): String {
            return "${Config.BASE_URL + URL_SUBCATEGORY + catId}"
        }

        fun getProductBySubId(subId: Int): String {
            return "${Config.BASE_URL + URL_PRODUCT_BY_SUB_ID + subId}"
        }

        fun getRegister(): String {
            return "${Config.BASE_URL + URL_REGISTER}"
        }

        fun getLogin(): String {
            return "${Config.BASE_URL + URL_LOGIN}"
        }

        fun postAddress(): String? {
            return "${Config.BASE_URL + URL_ADDRESS}"
        }

        fun getAddress(id: String): String? {
            return "${Config.BASE_URL + URL_ADDRESS + id}"
        }

        fun postOrder(): String {
            return "${Config.BASE_URL + URL_ORDER}"
        }

        fun getOrderSummary(id: String): String {
            return "${Config.BASE_URL + URL_ORDER + id}"
        }
    }
}