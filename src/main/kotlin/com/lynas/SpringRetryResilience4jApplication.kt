package com.lynas

import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration

@SpringBootApplication
class SpringRetryResilience4jApplication {

	@Bean
	fun restTemplate() = RestTemplate()

	@Bean
	fun webClient() : WebClient {
		return WebClient.builder()
			.build()
	}
}



@RestController
class DemoController(
	val restTemplate: RestTemplate,
	val webClient: WebClient
) {

	@GetMapping("/consumer2")
	fun demo2() : String? {
		val data = runBlocking {
			webClient.get()
				.uri("http://localhost:8080/demo")
				.retrieve()
				.bodyToMono(String::class.java)
				.retryWhen(Retry.fixedDelay(5L, Duration.ofSeconds(5)))
				.block()

		}
		return data
	}

	fun serviceNotAvailable(e: Exception)  = "service Not Available"

}

fun main(args: Array<String>) {
	runApplication<SpringRetryResilience4jApplication>(*args)
}