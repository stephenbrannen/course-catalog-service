package com.kotlinspring.entity

import jakarta.persistence.*

@Entity
@Table(name = "Courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int? = null,
    var name : String = "",
    var category : String ="",
)