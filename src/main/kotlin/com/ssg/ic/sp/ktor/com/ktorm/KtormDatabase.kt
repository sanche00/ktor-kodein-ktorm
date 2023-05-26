package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.db.DualDataBase
import com.ssg.ic.sp.db.ORM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import java.sql.ResultSet

const val NEXT_VAL = "SELECT nextval('%s')"

class KtormDatabase(override val database: DualDataBase<Database>) : ORM<Database>{

    override fun readDatabase() = database.readDataBase
    override fun writeDatabase() = database.writeDataBase

    override fun <T> read(block: (database: Database) -> T): T {
        val database = readDatabase();
        return database.useTransaction { block(database) }
    }

    override fun <T> write(block: (database: Database) -> T): T {
        val database = writeDatabase();
        return database.useTransaction { block(database) }
    }

    override suspend fun <T> writeExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        writeDatabase().useTransaction { block() }
    }

    override suspend fun <T> readExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        readDatabase().useTransaction { block() }
    }

    inline fun <reified T : Any> nativeSql(sql: String, block: (ResultSet) -> T): List<T> =
        readDatabase().useConnection { conn ->
            conn.prepareStatement(sql).use { statement ->
                statement.executeQuery().asIterable().map { block(it) }.toList()
            }
        }

    override fun nextval(sequence: String): Long = nativeSql(NEXT_VAL.format(sequence)) { it.getLong(1) }.first()

}