package com.kotlinspring.controller

import com.kotlinspring.controller.util.PostgreSQLContainerInitializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
//@Testcontainers
class GreetingControllerIntegrationTest : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    // Unneeded because of
    //    companion object {
    //
    //        @Container
    //        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
    //            withDatabaseName("testdb")
    //            withUsername("postgres")
    //            withPassword("secret")
    //        }
    //
    //        @JvmStatic
    //        @DynamicPropertySource
    //        fun properties(registry: DynamicPropertyRegistry) {
    //            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
    //            registry.add("spring.datasource.username", postgresDB::getUsername)
    //            registry.add("spring.datasource.password", postgresDB::getPassword)
    //        }
    //    }

    @Test
    fun retrieveGreeting() {
        val name = "Dilip"

        val result = webTestClient.get()
            .uri("/v1/greetings/{name}", name)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(String::class.java)
            .returnResult()

        Assertions.assertEquals("$name, Hello from default profile", result.responseBody)
    }
}