package com.ssg.ic.sp.ktor.com.service

import org.kodein.di.*
import kotlin.reflect.KClass

class KodeinFactory(override val di: DI) : DIService() {

    inline fun <reified T : Any> getFindBean(tag: String, type: KClass<T>): LazyDelegate<T> {
        return di.instance(tag)
    }

    inline fun <reified T : Any> getFindBean(tag: String): T {
        return di.direct.instance(tag)
    }



}