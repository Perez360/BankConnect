package com.codex.util.converters

import com.codex.util.JacksonUtils
import uk.co.jemos.podam.api.AttributeMetadata
import uk.co.jemos.podam.api.DataProviderStrategy
import uk.co.jemos.podam.typeManufacturers.TypeManufacturer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * Used to generate date ahead of today
 * */

class LocalDateTimeTypeManufacturer : TypeManufacturer<LocalDateTime> {
    override fun getType(
        p0: DataProviderStrategy?,
        p1: AttributeMetadata?,
        p2: MutableMap<String, Type>?
    ): LocalDateTime {

        val dateToString = LocalDateTime.now()
            .plusDays(Random.nextLong(7))
            .plusHours(Random.nextLong(24))
            .plusMinutes(Random.nextLong(60))
            .plusSeconds(Random.nextLong(60))
            .format(DateTimeFormatter.ofPattern(JacksonUtils.dateTimePattern))

        return LocalDateTime.parse(dateToString)
    }
}