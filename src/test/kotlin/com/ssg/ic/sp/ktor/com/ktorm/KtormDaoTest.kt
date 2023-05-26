package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.db.DAO
import com.ssg.ic.sp.db.DBConnect
import com.ssg.ic.sp.db.DualDataBase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.ktorm.dsl.insert
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import javax.sql.DataSource
import kotlin.test.assertEquals

class KtormDaoTest {

    lateinit var ktormDatabase: KtormDatabase
    lateinit var dualDataBase: DualDataBase<Database>

    @BeforeEach
    fun before() {

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
    fun savaTest() {

        val cityDao = CityDao(KtormDao(ktormDatabase))

        var ret = cityDao.save(KtormCity {
            id1 = 2
            id2 = 2
            name = "test"
        }, cityDao.writeDatabase())

        assertEquals(1, ret)

        val cityDao2 = FakeCityDao(FakeDAO())
        ret =  cityDao2.transaction { database, _ ->
            cityDao2.save(KtormCity {
                id1 = 2
                id2 = 2
                name = "test"
            }, database)
        }

        assertEquals(2, ret)
    }
}

class FakeDAO:DAO<Any, Any> {
    override fun <T> transaction(database: Any?, block: (database: Any, transaction: Any?) -> T): T {
        return block(database?:writeDatabase(), Any())
    }

    override fun <T> readTransaction(database: Any?, block: (database: Any, transaction: Any?) -> T): T {
        return block(database?:readDatabase(), Any())
    }

    override fun writeDatabase(): Any {
        return Any()
    }

    override fun readDatabase(): Any {
        return Any()
    }

    override fun nextval(sequence: String): Long {
        return 1
    }

}

interface City1<DATABASE, TRAN> : DAO<DATABASE, TRAN> {
    fun save(city: KtormCity, database: DATABASE): Int
}

class FakeCityDao(dao: DAO<Any, Any>) : City1<Any, Any>, DAO<Any, Any> by dao {
    override fun save(city: KtormCity, database: Any): Int {
        return 2
    }

}
class CityDao(dao: DAO<Database, Transaction>) : City1<Database, Transaction>, DAO<Database, Transaction> by dao {
    override fun save(city: KtormCity, database: Database): Int {
        return database.insert(KtormCitis) {
            set(it.id1, city.id1)
            set(it.id2, city.id2)
            set(it.name, city.name)
        }
    }

}