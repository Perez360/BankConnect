package com.codex.dtos

import com.codex.enums.Gender
import com.codex.enums.UserStatus
import com.codex.models.Contact

data class UpdateUserDTO(
    val id: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val placeOfBirth: String,
    val homeTown: String,
    val contacts: MutableList<Contact>,
    val dateOfBirth: String,
    val gender: Gender,
    val status: UserStatus,
    val msisdn: String,
    val password: String
)
