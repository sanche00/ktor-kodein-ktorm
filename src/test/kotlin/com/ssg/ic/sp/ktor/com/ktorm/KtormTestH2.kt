package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.db.DBConnect
import com.ssg.ic.sp.db.DualDataBase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import javax.sql.DataSource
import kotlin.test.assertEquals

class KtormTestH2 {

    lateinit var ktormDatabase: KtormDatabase
    lateinit var dualDataBase: DualDataBase<Database>

    @BeforeEach
    fun before() {


//        val dbConnect = DBConnect(url = "jdbc:h2:file:./test;MODE=PostgreSQL;INIT=RUNSCRIPT FROM './test-sql/create-city.sql';DATABASE_TO_UPPER=false;CASE_INSENSITIVE_IDENTIFIERS=true", driver = "org.h2.Driver", user = "sa")
//        val database = dbConnect.database { org.jetbrains.exposed.sql.Database.connect(it) }
//        transaction(database) {
//            addLogger(StdOutSqlLogger)
//            SchemaUtils.create(Cities)
//        }

        val databaseConnection = { dataSource: DataSource ->
            Database.connect(
                dataSource,
                logger = ConsoleLogger(threshold = LogLevel.INFO)
            )
        }
        dualDataBase = DualDataBase(
            writeConnect = DBConnect(
                url = "jdbc:h2:mem:test;MODE=PostgreSQL;INIT=RUNSCRIPT FROM './test-sql/create-city.sql';DATABASE_TO_UPPER=false;CASE_INSENSITIVE_IDENTIFIERS=true",
                driver = "org.h2.Driver",
                user = "sa"
            ),
            databaseConnection = databaseConnection
        )
        ktormDatabase = KtormDatabase(dualDataBase)

    }

    @Test
    fun ktormDatabase() {
        val ret = ktormDatabase.write { database ->
            database.insert(KtormCitis) {
                set(it.id1, 2)
                set(it.id2, 2)
                set(it.name, "test")
            }
        }
        var city = ktormDatabase.read { database ->
            database.from(KtormCitis).select().map {
                City(it[KtormCitis.id1]!!, it[KtormCitis.id2]!!, it[KtormCitis.name]!!)
            }
        }
        assertEquals(city.first().name , "test")
    }

}

data class City(val id1: Int, val id2: Int, val name: String)

data class CityId(val id1: Int, val id2: Int) {

}
//
//object Cities : Table("tba_city") {
//    val id1 = integer("id1")
//    val id2 = integer("id2")
//
//    val name = varchar("name", 50)
//
//    override val primaryKey = PrimaryKey(id1, id2)
//}
//
//object KtormCitis : org.ktorm.schema.Table<Nothing>("tba_city") {
//
//    val id1 = int("id1").primaryKey()
//    val id2 = int("id2").primaryKey()
//    val name = varchar("name")
//}