package org.bloglog.processor

import org.bloglog.processor.handler.*
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class ProcessorApplication

fun main(args: Array<String>) {
	runApplication<ProcessorApplication>(*args)
}

@RestController
class RestController() {
	@Autowired
	lateinit var adder: Adder

	@Autowired
	lateinit var remover: Remover

	@PostMapping("/add")
	fun add(@Valid @RequestBody request : AddRequest): AddResponse {
		return adder.add(request)
	}

	@PostMapping("/remove")
	fun add(@Valid @RequestBody request : RemoveRequest): RemoveResponse {
		return remover.remove(request)
	}
}
