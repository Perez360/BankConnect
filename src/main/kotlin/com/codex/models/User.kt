package com.codex.models

import com.codex.dtos.CreateUserDTO
import com.codex.dtos.UpdateUserDTO
import com.codex.enums.Gender
import com.codex.enums.UserStatus
import com.codex.util.listeners.UserEntityListener
import com.codex.util.validators.DateTimeOperations
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
            Field("fName"),
            Field("mName"),
            Field("lName"),
            Field("pob"),
            Field("homeTown"),
            Field("dob"),
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
) {
    companion object {
        fun fromCreateUserDTO(createUserDTO: CreateUserDTO): User {
            return User(
                id = ObjectId(),
                firstName = createUserDTO.firstName,
                lastName = createUserDTO.lastName,
                middleName = createUserDTO.middleName,
                homeTown = createUserDTO.homeTown,
                dateOfBirth = DateTimeOperations.validateLocalDateAndParse(createUserDTO.dateOfBirth),
                placeOfBirth = createUserDTO.placeOfBirth,
                contacts = createUserDTO.contacts,
                status = createUserDTO.status,
                msisdn = createUserDTO.msisdn,
                password = createUserDTO.password,
                gender = createUserDTO.gender
            )
        }

        fun fromUpdateUserDTO(updateUserDTO: UpdateUserDTO): User {
            return User(
                id = ObjectId(updateUserDTO.id),
                firstName = updateUserDTO.firstName,
                lastName = updateUserDTO.lastName,
                middleName = updateUserDTO.middleName,
                homeTown = updateUserDTO.homeTown,
                dateOfBirth = DateTimeOperations.validateLocalDateAndParse(updateUserDTO.dateOfBirth),
                placeOfBirth = updateUserDTO.placeOfBirth,
                contacts = updateUserDTO.contacts,
                status = updateUserDTO.status,
                msisdn = updateUserDTO.msisdn,
                password = updateUserDTO.password,
                gender = updateUserDTO.gender
            )
        }
    }
}


data class FilterUserRequest(
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var gender: String? = null,
    var status: String? = UserStatus.ACTIVE.name,
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
            val placeOfBirth: String? by map.withDefault { null }
            val homeTown: String? by map.withDefault { null }
            val status: String? by map.withDefault { UserStatus.ACTIVE.name }
            val gender: String? by map.withDefault { null }
            val dateOfBirth: String? by map.withDefault { null }
            val page: Int by map.withDefault { 1 }
            val size: Int by map.withDefault { 10 }

            return FilterUserRequest(
                firstName = firstName,
                middleName = middleName,
                lastName = lastName,
                status = status,
                gender = gender,
                placeOfBirth = placeOfBirth,
                homeTown = homeTown,
                dateOfBirth = dateOfBirth,
                page = page,
                size = size
            )
        }
    }
}