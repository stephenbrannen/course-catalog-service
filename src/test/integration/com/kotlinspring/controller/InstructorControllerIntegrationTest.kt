package com.kotlinspring.controller

import com.kotlinspring.controller.util.PostgreSQLContainerInitializer
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.repository.InstructorRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
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
class InstructorControllerIntegrationTest : PostgreSQLContainerInitializer() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var instructorRepository: InstructorRepository

    // unneeded because of PostgreSQLContainerInitializer
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

    @BeforeEach
    fun setUp() {
        instructorRepository.deleteAll()
    }

    @Test
    fun createInstructor() {
        val instructorName = "John Doe"
        val instructorDTO = InstructorDTO(null, instructorName)

        val savedInstructorDTO = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue(savedInstructorDTO!!.id != null)
        Assertions.assertEquals(instructorName, savedInstructorDTO.name)
    }
}