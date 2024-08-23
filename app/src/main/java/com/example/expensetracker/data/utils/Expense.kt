package com.example.expensetracker.data.utils

import androidx.compose.runtime.remember

data class Expense(
    val id: String? = null,
    val category: String,
    val amount: Float? = null,
    val date: String? = null,
    val tags: String? = null


) {
    constructor() : this(null, null.toString(), null, null, null)

}




