package com.chargepoint.csms.authentication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class AuthenticationServiceApplication

fun main(args: Array<String>) {
	runApplication<AuthenticationServiceApplication>(*args)
}
