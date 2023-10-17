package com.codex.util.validators

class UserFilterValidator {
    companion object {
        fun validate(map: MutableMap<String, Any>) {
            val dateCreated: String by map
            dateCreated?.let { DateTimeOperations.validateLocalDateAndParse(it) }
        }
    }
}