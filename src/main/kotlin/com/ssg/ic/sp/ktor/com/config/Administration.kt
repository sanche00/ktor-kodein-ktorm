package com.ssg.ic.sp.ktor.com.config

import io.ktor.server.engine.*
import io.ktor.server.application.*

fun Application.configureAdministration() {
    install(ShutDownUrl.ApplicationCallPlugin) {
        // The URL that will be intercepted (you can also use the application.conf's ktor.deployment.shutdown.url key)
        shutDownUrl = "/admin/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }
}
