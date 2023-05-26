package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.db.DBConnect
import com.ssg.ic.sp.db.DualDataBase
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar
import javax.sql.DataSource
import kotlin.test.assertEquals

class KtormTestH2File {

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
                url = "jdbc:h2:file:./test;MODE=PostgreSQL;INIT=RUNSCRIPT FROM './test-sql/create-city.sql';DATABASE_TO_UPPER=false;CASE_INSENSITIVE_IDENTIFIERS=true",
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
        ktormDatabase.read { database ->
            database.from(KtormCitis).select().forEach { println(it) }
        }

    }


    @Test
    fun ktormDao() {
//        val ret = ktormDatabase.write { database ->
//            database.insert(KtormCitis) {
//                set(it.id1, 2)
//                set(it.id2, 2)
//                set(it.name, "test")
//            }
//        }
//        ktormDatabase.read { database ->
//            database.from(KtormCitis).select().forEach { println(it) }
//        }

        ktormDatabase.write { database ->
            database.ktormCitis.add(KtormCity {
//                id = KtormCityId(1,2)
                id1 = 1
                id2 = 3
                name = "test"
            })
        }
        val ret = ktormDatabase.read { database ->
            database.ktormCitis.find { it.id1 eq 1 }
        }
        ret?.let {
            println(it)
        }
    }

    val Database.ktormCitis get() = this.sequenceOf(KtormCitis)
}

object Cities : Table("tba_city") {
    val id1 = integer("id1")
    val id2 = integer("id2")

    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id1, id2)
}

interface KtormCity : Entity<KtormCity> {
    companion object : Entity.Factory<KtormCity>()

    var id1: Int
    var id2: Int
    var name: String

}

object KtormCitis : org.ktorm.schema.Table<KtormCity>("tba_city") {
    val id1 = int("id1").primaryKey().bindTo { it.id1 }
    val id2 = int("id2").primaryKey().bindTo { it.id2 }
    val name = varchar("name").bindTo { it.name }

}
data class KtormCityId(val id1 : Int, val id2 : Int)


