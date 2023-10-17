package com.codex.util.validators

import com.codex.domain.*
import com.codex.exceptions.ServiceException
import com.codex.models.User

class UserValidator {
    companion object {
        fun validate(user: User) {

            if (user.firstName.isEmpty()) throw ServiceException(-2, INVALID_FIRSTNAME)

            if (user.lastName.isEmpty()) throw ServiceException(-2, INVALID_LASTNAME)

            if (user.homeTown.isEmpty()) throw ServiceException(-2, INVALID_HOMETOWN)

            if (user.placeOfBirth.isEmpty()) throw ServiceException(-2, INVALID_PLACE_OF_BIRTH)

            if (user.password.length < 8 && user.password.contains(" ")) throw ServiceException(-2, INVALID_CREATE_OR_UPDATE_PASSWORD)
        }
    }
}