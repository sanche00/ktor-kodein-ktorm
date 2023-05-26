package com.ssg.ic.sp.ktor.com.ktorm
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import java.time.Instant

interface TbaFlmntdlcmpmapngL : Entity<TbaFlmntdlcmpmapngL> {
    companion object : Entity.Factory<TbaFlmntdlcmpmapngL>()

    var flmntCd: String
    var flmntDlcmpCd: String
    var dlcmpCd: String
    var delYn: String
    var frRegId: String
    var frRegDtm: Instant
    var frRegIp: String
    var fnlUpdId: String
    var fnlUpdDtm: Instant
    var fnlUpdIp: String
}

object TbaFlmntdlcmpmapngLs : Table<TbaFlmntdlcmpmapngL>("tba_flmntdlcmpmapng_l") {
    val flmntCd = varchar("flmnt_cd").primaryKey().bindTo { it.flmntCd }
    val flmntDlcmpCd = varchar("flmnt_dlcmp_cd").primaryKey().bindTo { it.flmntDlcmpCd }
    val dlcmpCd = varchar("dlcmp_cd").primaryKey().bindTo { it.dlcmpCd }
    val delYn = varchar("del_yn").bindTo { it.delYn }
    val frRegId = varchar("fr_reg_id").bindTo { it.frRegId }
    val frRegDtm = timestamp("fr_reg_dtm").bindTo { it.frRegDtm }
    val frRegIp = varchar("fr_reg_ip").bindTo { it.frRegIp }
    val fnlUpdId = varchar("fnl_upd_id").bindTo { it.fnlUpdId }
    val fnlUpdDtm = timestamp("fnl_upd_dtm").bindTo { it.fnlUpdDtm }
    val fnlUpdIp = varchar("fnl_upd_ip").bindTo { it.fnlUpdIp }
}

val Database.tbaFlmntdlcmpmapngLs get() = this.sequenceOf(TbaFlmntdlcmpmapngLs)