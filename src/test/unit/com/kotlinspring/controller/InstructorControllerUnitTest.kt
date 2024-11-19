package com.kotlinspring.controller

import com.kotlinspring.controller.util.instructorDTO
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [InstructorController::class])
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {
    @Autowired
    private lateinit var instructorService: InstructorService

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMockk: InstructorService

    @Test
    fun createInstructor() {
        val instructorName = "John Doe"
        val instructorDTO = InstructorDTO(null, instructorName)

        every { instructorServiceMockk.createInstructor(any()) } returns instructorDTO(id=1)

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

    @Test
    fun createInstructor_validation() {
        val instructorDTO = instructorDTO(name = "")
        val errorMessage = "instructorDTO.name must not be blank"

        every { instructorServiceMockk.createInstructor(any()) } returns instructorDTO(id=1)

        val response = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMessage, response)
    }
}