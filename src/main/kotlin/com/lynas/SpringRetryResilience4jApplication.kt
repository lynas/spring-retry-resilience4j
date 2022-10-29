package com.lynas

import io.github.resilience4j.retry.annotation.Retry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.util.*

@SpringBootApplication
class SpringRetryResilience4jApplication {

	@Bean
	fun restTemplate() = RestTemplate()
}

fun main(args: Array<String>) {
	runApplication<SpringRetryResilience4jApplication>(*args)
}

@RestController
class DemoController(
	val restTemplate: RestTemplate
) {

	@GetMapping("/consumer")
	@Retry(name = "order-service", fallbackMethod = "serviceNotAvailable")
	fun demo() : String? {
		println("Time")
		println(Date())
		return restTemplate.getForObject(
			"http://localhost:8080/demo",
			String::class.java
		)
	}

	fun serviceNotAvailable(e: Exception)  = "service Not Available"

}
