package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.db.DualDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database

class KtormDatabase(private val database: DualDataBase<Database>) {

    fun readDatabase() = database.readDataBase
    fun writeDatabase() = database.writeDataBase

    fun <T> read(block: (database:Database) -> T): T {
        val database = readDatabase();
        return database.useTransaction { block(database) }
    }

    fun <T> write(block: (database:Database) -> T): T {
        val database = writeDatabase();
        return database.useTransaction { block(database) }
    }

    suspend fun <T> writeExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        writeDatabase().useTransaction { block() }
    }

    suspend fun <T> readExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        readDatabase().useTransaction { block() }
    }
}