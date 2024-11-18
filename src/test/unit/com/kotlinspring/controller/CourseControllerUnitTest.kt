package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    private lateinit var courseService: CourseService

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development")

        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id=1)

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue(savedCourseDTO!!.id != null)
    }

    @Test
    fun addCourse_validation() {
        val courseDTO = CourseDTO(null, "", "")
        val errorMessage = "courseDTO.category must not be blank, courseDTO.name must not be blank"

        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id=1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)
    }

    @Test
    fun addCourse_runtimeException() {
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development")
        val errorMessage = "Unexpected error occurred"

        every { courseServiceMockk.addCourse(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)
    }

    @Test
    fun retrieveAllCourses() {

        every { courseService.retrieveAllCourses() } returns listOf(
            courseDTO(id=1),
            courseDTO(id=2),
            courseDTO(id=3)
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {
        val course = Course(1,"Build RestFul APis using SpringBoot and Kotlin", "Development")
        val updatedCourseName = "Build Restful APIs using SpringBoot and Kotlin1"
        val updatedCourseDTO = CourseDTO(null, updatedCourseName, "Development")

        every { courseServiceMockk.updateCourse(any(), any()) } returns courseDTO(id=1, name=updatedCourseName)

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/${course.id}")
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(updatedCourseName, updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {
        val course = Course(1,"Build RestFul APis using SpringBoot and Kotlin", "Development")

        every { courseServiceMockk.deleteCourse(any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/courses/${course.id}")
            .exchange()
            .expectStatus().isNoContent
    }
}