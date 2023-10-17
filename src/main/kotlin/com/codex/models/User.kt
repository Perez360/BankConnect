package com.codex.models

import dev.morphia.annotations.*
import org.bson.types.ObjectId
import java.time.LocalDate

@Entity("users")
@Indexes(
    Index(
        fields = [
            Field("id"),
            Field("name"),
            Field("placeOfBirth"),
            Field("homeTown"),
            Field("dateOfBirth"),
        ], options = IndexOptions(background = true)
    )
)
data class User(
    @Id
    val id: ObjectId,
    var name: String,
    @Property("pob")
    var placeOfBirth: String,
    var homeTown: String,
    @Property("dob")
    var dateOfBirth: LocalDate,
    var version: Long,
)


data class FilterUserRequest(
    val name: String? = null,
    var placeOfBirth: String? = null,
    var homeTown: String? = null,
    val dob: LocalDate? = null,
    val page: Int = 1,
    val size: Int = 10
)