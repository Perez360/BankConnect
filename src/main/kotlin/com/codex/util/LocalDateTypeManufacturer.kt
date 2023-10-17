package com.codex.util

import uk.co.jemos.podam.api.AttributeMetadata
import uk.co.jemos.podam.api.DataProviderStrategy
import uk.co.jemos.podam.typeManufacturers.TypeManufacturer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * Used to generate date ahead of today
 * */

class LocalDateTypeManufacturer : TypeManufacturer<LocalDate> {
    override fun getType(
        p0: DataProviderStrategy?,
        p1: AttributeMetadata?,
        p2: MutableMap<String, Type>?
    ): LocalDate {

        val dateToString = LocalDate.now()
            .plusDays(Random.nextLong(7))
            .format(DateTimeFormatter.ofPattern(JacksonUtils.datePattern))

        return LocalDate.parse(dateToString)
    }
}