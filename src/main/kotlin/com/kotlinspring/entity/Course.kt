package com.kotlinspring.entity

@Entity
data class Course(
    val id : Int?,
    val name : String,
    val category : String,
)