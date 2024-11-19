package com.kotlinspring.entity

import jakarta.persistence.*

@Entity
@Table(name = "Instructors")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Int? = null,
    var name : String = "",
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var courses : List<Course> = mutableListOf()
)