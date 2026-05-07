package com.aswan.todo.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.aswan.todo.TaskDatabase

class IosDatabaseDriverFactory(): DatabaseDrivenFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            TaskDatabase.Schema,
            "task.db"
        )
    }
}