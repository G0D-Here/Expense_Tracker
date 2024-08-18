package com.example.expensetracker.data.utils

data class Expense(
    val id: String? = null,
    val note: String? = null,
    val category: String,
    val amount: Float? = null,
    val date: String? = null,
    val tags: String? = null


) {
    constructor() : this(null, null, null.toString(), null, null, null)
}
