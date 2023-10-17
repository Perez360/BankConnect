package com.codex.models

import com.codex.enums.Gender
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import dev.morphia.annotations.*
import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull
import java.time.LocalDate

@Entity("users")
@JsonIgnoreProperties(value = ["version", "id"], allowGetters = true)
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
    val id: ObjectId ,
    @Property("fName")
    var firstName: String,
    @Property("mName")
    var middleName: String,
    @Property("lName")
    var lastName: String,
    @Property("pob")
    var placeOfBirth: String,
    var homeTown: String,
    @NotNull
    val gender: Gender,
    @Property("dob")
    var dateOfBirth: LocalDate,
    val contacts: List<Contact>,
    var msisdn: String,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String,
    @Version
    var version: Long,
)

//data class CreateUserDTO(
//    val firstName: String,
//    val middleName: String,
//    val lastName: String,
//    val placeOfBirth: String,
//    val homeTown: String,
//    val dateOfBirth: String,
//    val password: String
//)


data class FilterUserRequest(
    var fName: String? = null,
    var mName: String? = null,
    var lName: String? = null,
    var placeOfBirth: String? = null,
    var homeTown: String? = null,
    val dob: String? = null,
    val page: Int = 1,
    val size: Int = 10
) {
    companion object {
        fun fromMap(map: Map<String, Any>): FilterUserRequest {
            val fName: String? by map.withDefault { null }
            val mName: String? by map.withDefault { null }
            val lName: String? by map.withDefault { null }
            val placeOfBirth: String? by map.withDefault { null }
            val homeTown: String? by map.withDefault { null }
            val dob: String? by map.withDefault { null }
            val page: Int? by map.withDefault { 1 }
            val size: Int? by map.withDefault { 10 }

            return FilterUserRequest(
                fName = fName,
                mName = mName,
                lName = lName,
                placeOfBirth = placeOfBirth,
                homeTown = homeTown,
                dob = dob,
                page = page!!,
                size = size!!
            )
        }
    }
}