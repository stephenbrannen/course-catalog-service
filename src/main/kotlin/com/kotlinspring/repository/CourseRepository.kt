package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
    fun findByNameContaining(name: String): List<Course> // This is a custom query method using Spring Data JPA

    @Query(value = "SELECT * FROM Courses WHERE name LIKE %?1%", nativeQuery = true)
    fun findCoursesByName(name: String): List<Course>  // This is a custom query method using SQL directly
}