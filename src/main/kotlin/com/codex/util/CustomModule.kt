package com.codex.util

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import org.bson.types.ObjectId


class CustomModule : SimpleModule() {
    override fun <T : Any?> addDeserializer(type: Class<T>?, deser: JsonDeserializer<out T>?): SimpleModule {
        return addSerializer(ObjectId::class.java, ObjectIdSerializer())
            .addDeserializer(ObjectId::class.java, ObjectIdDeserializer())
    }


}