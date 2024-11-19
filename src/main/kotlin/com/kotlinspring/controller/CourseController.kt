package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid courseDTO: CourseDTO) : CourseDTO = courseService.addCourse(courseDTO)
    // The above is equivalent to the following:
    //    {
    //        return courseService.addCourse(courseDTO)
    //    }

    @GetMapping
    fun retrieveAllCourses(@RequestParam("course_name", required = false) courseName: String?) : List<CourseDTO> = courseService.retrieveAllCourses(courseName)

    //courseId is a path variable
    @PutMapping("/{courseId}")
    fun updateCourse(@PathVariable courseId: Int, @RequestBody courseDTO: CourseDTO) : CourseDTO = courseService.updateCourse(courseId, courseDTO)

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable courseId: Int) = courseService.deleteCourse(courseId)
}