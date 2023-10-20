package com.codex.util.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import org.bson.types.ObjectId


class CustomModule : SimpleModule() {
    init {
        addSerializer(ObjectId::class.java, ObjectIdSerializer())
        addDeserializer(ObjectId::class.java, ObjectIdDeserializer())
    }
}

