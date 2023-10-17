package com.codex.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.bson.types.ObjectId

class ObjectIdDeserializer : JsonDeserializer<ObjectId>() {
    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): ObjectId {
        return ObjectId(p0?.getValueAsString("id"))
    }

}