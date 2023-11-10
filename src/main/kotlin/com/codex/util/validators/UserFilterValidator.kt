package com.codex.util.validators

import com.codex.enums.ErrorCode
import com.codex.enums.Gender
import com.codex.enums.UserStatus
import com.codex.exceptions.ServiceException

class UserFilterValidator {
    companion object {
        fun validate(map: MutableMap<String, Any?>) {
            val status: String? by map.withDefault { null }
            val gender: String? by map.withDefault { null }
            val dateCreated: String? by map.withDefault { null }
            gender?.let {
                try {
                    Gender.valueOf(it)
                } catch (ex: Exception) {
                    throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid gender provided")
                }
            }
            status?.let {
                try {
                    UserStatus.valueOf(it)
                } catch (ex: Exception) {
                    throw ServiceException(ErrorCode.BAD_REQUEST, "Invalid status provided")
                }
            }
            dateCreated?.let { DateTimeOperations.validateLocalDateAndParse(it) }
        }
    }
}