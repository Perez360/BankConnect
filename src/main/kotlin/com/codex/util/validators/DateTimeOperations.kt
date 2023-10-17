package com.codex.util.validators

import com.codex.domain.INVALID_DATE
import com.codex.exceptions.ServiceException
import com.codex.util.JacksonUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateTimeOperations {
    companion object {
        fun validateLocalDateTimeAndParse(dateTime: String): LocalDateTime {
            try {
                return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(JacksonUtils.dateTimePattern))
            } catch (ex: DateTimeParseException) {
                throw ServiceException(-2, INVALID_DATE)
            }
        }

        fun validateLocalDateAndParse(date: String): LocalDate {
            try {
                return LocalDate.parse(date, DateTimeFormatter.ofPattern(JacksonUtils.datePattern))
            } catch (ex: DateTimeParseException) {
                throw ServiceException(-2, INVALID_DATE)
            }
        }


    }
}