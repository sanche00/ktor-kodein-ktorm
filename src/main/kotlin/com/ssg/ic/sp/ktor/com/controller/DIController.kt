package com.ssg.ic.sp.ktor.com.controller
import io.ktor.server.application.*
import org.kodein.di.DIAware
import org.kodein.di.instance

abstract class DIController : DIAware {
    val app: Application by instance()
}