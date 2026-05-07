package com.aswan.todo.data

import app.cash.sqldelight.db.SqlDriver

interface DatabaseDrivenFactory {
    fun createDriver(): SqlDriver
}