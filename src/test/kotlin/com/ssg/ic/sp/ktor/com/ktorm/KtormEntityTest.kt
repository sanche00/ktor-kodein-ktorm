package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.db.DBConnect
import com.ssg.ic.sp.db.DualDataBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.neq
import org.ktorm.entity.first
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import javax.sql.DataSource

const val ENV_DB_DRIVER = "db.driver"
const val ENV_DB_WRITE_URL = "db.url"
const val ENV_DB_WRITE_USER = "db.user"
const val ENV_DB_WRITE_PWD = "db.pwd"

const val ENV_DB_READ_URL = "db.read.url"
const val ENV_DB_READ_USER = "db.read.user"
const val ENV_DB_READ_PWD = "db.read.pwd"
const val ENV_DB_POOL_SIZE = "db.pool.size"
const val DEFAULT_POOL_SIZE = 3

class KtormEntityTest {

    lateinit var ktormDatabase:KtormDatabase

    @BeforeEach
    fun before() {
        System.setProperty(com.ssg.ic.sp.db.ENV_DB_DRIVER, "org.postgresql.Driver")
        System.setProperty(
            com.ssg.ic.sp.db.ENV_DB_WRITE_URL,
            "jdbc:postgresql://sellpick-dev-main.cluster-c6bwy8w0ihhy.ap-northeast-2.rds.amazonaws.com:5432/sellpickdevSales"
        )
        System.setProperty(com.ssg.ic.sp.db.ENV_DB_WRITE_USER, "us_sellpick")
        System.setProperty(com.ssg.ic.sp.db.ENV_DB_WRITE_PWD, "us_sellpick")

        val databaseConnection = { dataSource: DataSource ->
            Database.connect(
                dataSource,
                logger = ConsoleLogger(threshold = LogLevel.DEBUG)
            )
        }
        val dualDataBase = DualDataBase(
            writeConnect = DBConnect(),
            databaseConnection = databaseConnection
        )
        ktormDatabase = KtormDatabase(dualDataBase)
    }

    @Test
    fun findFulfillmentDlvCode() {
        var ret = ktormDatabase.read { database ->
            database.tbaFlmntdlcmpmapngLs.first {
                (it.delYn neq "Y") and
                        (it.flmntCd eq "GMKFLMNT") and
                        (it.dlcmpCd eq "KGB")
            }
        }.flmntDlcmpCd
        Assertions.assertEquals("10003", ret)
//        val ret = deliveryCodeMapper.findFulfillmentDlvCode("GMKFLMNT","KGB")
//        Assertions.assertEquals("10003", ret)
    }
}