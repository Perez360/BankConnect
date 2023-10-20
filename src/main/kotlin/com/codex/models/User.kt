package com.codex.models

import com.codex.enums.Gender
import com.codex.enums.UserStatus
import com.codex.util.listeners.UserEntityListener
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dev.morphia.annotations.*
import org.bson.types.ObjectId
import java.time.LocalDate

@Entity("users")
@EntityListeners(value = [UserEntityListener::class])
@JsonIgnoreProperties(value = ["version", "password"], allowGetters = false)
@Indexes(
    Index(
        fields = [
            Field("id"),
            Field("firstName"),
            Field("middleName"),
            Field("lastName"),
            Field("placeOfBirth"),
            Field("homeTown"),
            Field("dateOfBirth"),
        ], options = IndexOptions(background = true)
    )
)
data class User(
    @Id
    var id: ObjectId = ObjectId(),
    @Property("fName")
    var firstName: String,
    @Property("mName")
    var middleName: String,
    @Property("lName")
    var lastName: String,
    @Property("pob")
    var placeOfBirth: String,
    var homeTown: String,
    var gender: Gender,
    val status: UserStatus,
    @Property("dob")
    var dateOfBirth: LocalDate,
    var contacts: MutableList<Contact> = mutableListOf(),
    var msisdn: String,
    var password: String,
    var version: Long = 1L,
)

data class CreateUserDTO(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val placeOfBirth: String,
    val homeTown: String,
    val contacts: MutableList<Contact>,
    val dateOfBirth: String,
    val gender: Gender,
    val status: UserStatus,
    val msisdn: String,
    val password: String
)

data class UpdateUserDTO(
    val id: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val placeOfBirth: String,
    val homeTown: String,
    val contacts: MutableList<Contact>,
    val dateOfBirth: String,
    val gender: Gender,
    val status: UserStatus,
    val msisdn: String,
    val password: String
)


data class FilterUserRequest(
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var gender: String? = null,
    var status: String? = null,
    var placeOfBirth: String? = null,
    var homeTown: String? = null,
    val dateOfBirth: String? = null,
    val page: Int = 1,
    val size: Int = 10
) {
    companion object {
        fun fromMap(map: Map<String, Any?>): FilterUserRequest {
            val firstName: String? by map.withDefault { null }
            val middleName: String? by map.withDefault { null }
            val lastName: String? by map.withDefault { null }
            val status: String? by map.withDefault { null }
            val gender: String? by map.withDefault { null }
            val placeOfBirth: String? by map.withDefault { null }
            val homeTown: String? by map.withDefault { null }
            val dateOfBirth: String? by map.withDefault { null }
            val page: String by map.withDefault { "1" }
            val size: String by map.withDefault { "10" }

            return FilterUserRequest(
                firstName = firstName,
                middleName = middleName,
                lastName = lastName,
                status = status,
                gender = gender,
                placeOfBirth = placeOfBirth,
                homeTown = homeTown,
                dateOfBirth = dateOfBirth,
                page = page.toInt(),
                size = size.toInt()
            )
        }
    }
}