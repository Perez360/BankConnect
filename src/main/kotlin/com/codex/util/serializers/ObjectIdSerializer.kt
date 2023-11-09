package com.codex.util.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId

class ObjectIdSerializer : JsonSerializer<ObjectId>() {
    override fun serialize(p0: ObjectId?, p1: JsonGenerator?, p2: SerializerProvider?) {
        p1?.writeString(p0?.toString())
    }
}