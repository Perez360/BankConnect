package com.codex.util

import org.bson.types.ObjectId
import uk.co.jemos.podam.api.AttributeMetadata
import uk.co.jemos.podam.api.DataProviderStrategy
import uk.co.jemos.podam.typeManufacturers.TypeManufacturer
import java.lang.reflect.Type

/**
 * Used to generate date ahead of today
 * */

class ObjectIdTypeManufacturer : TypeManufacturer<ObjectId> {
    override fun getType(
        p0: DataProviderStrategy?,
        p1: AttributeMetadata?,
        p2: MutableMap<String, Type>?
    ): ObjectId {
        return ObjectId()
    }
}