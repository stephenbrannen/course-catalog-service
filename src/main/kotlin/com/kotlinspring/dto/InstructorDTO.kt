package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank

class InstructorDTO(
    var id: Int?,
    @get:NotBlank(message = "instructorDTO.name must not be blank")
    var name: String,
)