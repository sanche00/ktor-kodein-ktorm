package com.ssg.ic.sp.ktor.com.ktorm

import com.ssg.ic.sp.ktor.com.service.DIService
import com.ssg.ic.sp.ktorm.KtormDatabase
import org.kodein.di.instance

abstract class KtormDataBaseService: DIService() {

    protected val database : KtormDatabase by instance()

}