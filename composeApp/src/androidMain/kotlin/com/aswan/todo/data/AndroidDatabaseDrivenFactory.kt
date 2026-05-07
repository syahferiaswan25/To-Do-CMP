package com.aswan.todo.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aswan.todo.TaskDatabase

class AndroidDatabaseDrivenFactory(
    private val context: Context
): DatabaseDrivenFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            TaskDatabase.Schema,
            context = context,
            name = "task.db",
        )
    }
}