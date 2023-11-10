package com.codex.util.validators

import com.codex.enums.ErrorCode
import com.codex.exceptions.ServiceException
import com.codex.models.User

class UserValidator {
    companion object {
        fun validate(user: User) {

            if (user.firstName.isEmpty()) throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid first name provided, this field must not be empty")

            if (user.lastName.isEmpty()) throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid last name provided, this field must not be empty")

            if (user.homeTown.isEmpty()) throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid home town provided, this field must not be empty")

            if (user.placeOfBirth.isEmpty()) throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid place of birth provided, this field must not be empty")

            if (user.password.length < 8 && user.password.contains(" ")) throw ServiceException(ErrorCode.BAD_REQUEST, "Password field must not have spaces and also should contain 8 or more characters ")
        }
    }
}