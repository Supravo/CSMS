package com.chargepoint.csms.transaction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TransactionServiceApplication

fun main(args: Array<String>) {
	runApplication<TransactionServiceApplication>(*args)
}
