package com.lynas

import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.util.Date

@SpringBootApplication
class SpringRetryResilience4jApplication {

	@Bean
	fun restTemplate() = RestTemplate()

	@Bean
	fun retryConfigCustomizer(): RetryConfigCustomizer {
		return RetryConfigCustomizer.of("orderservice"){
			it.maxAttempts(10)
			it.waitDuration(Duration.ofSeconds(10))
			it.retryExceptions(Exception::class.java)
		}
	}
}



@RestController
class DemoController(
	val restTemplate: RestTemplate
) {

	@GetMapping("/consumer")
	@Retry(name = "orderservice", fallbackMethod = "serviceNotAvailable")
	fun demo() : String? {
		println(Date())
		return restTemplate.getForObject(
			"http://localhost:8080/demo",
			String::class.java
		)
	}

	fun serviceNotAvailable(e: Exception)  = "service Not Available"

}

fun main(args: Array<String>) {
	runApplication<SpringRetryResilience4jApplication>(*args)
}