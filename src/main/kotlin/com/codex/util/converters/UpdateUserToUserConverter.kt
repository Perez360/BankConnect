package com.codex.util.converters

import com.codex.models.UpdateUserDTO
import com.codex.models.User
import com.codex.util.validators.DateTimeOperations
import dev.morphia.mapping.experimental.MorphiaReference
import org.bson.types.ObjectId
import org.modelmapper.Converter
import org.modelmapper.spi.MappingContext

class UpdateUserToUserConverter : Converter<UpdateUserDTO, User> {
    override fun convert(p0: MappingContext<UpdateUserDTO, User>?): User? {
        val source = p0?.source
        if (source != null) {
            return User(
                id = ObjectId(source.id),
                firstName = source.firstName,
                middleName = source.middleName,
                lastName = source.lastName,
                placeOfBirth = source.placeOfBirth,
                homeTown = source.homeTown,
                gender = source.gender,
                status = source.status,
                dateOfBirth = DateTimeOperations.validateLocalDateAndParse(source.dateOfBirth),
                contacts = source.contacts,
                msisdn = source.msisdn,
                password = source.password,
            )
        }
        return null
    }
}