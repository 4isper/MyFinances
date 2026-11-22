package com.m4isper.local

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.platform.app.InstrumentationRegistry

/**
 * In-memory база для instrumented-тестов DAO на устройстве.
 * android-вариант BundledSQLiteDriver использует android.database.sqlite,
 * поэтому JVM-вариант (без Context) в unit-тестах не работает.
 */
fun buildTestDatabase(): AppDatabase {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    return Room
        .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .setDriver(BundledSQLiteDriver())
        .build()
}
