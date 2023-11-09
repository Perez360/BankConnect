package com.codex.util.converters

import com.codex.dtos.CreateUserDTO
import com.codex.models.User
import com.codex.util.validators.DateTimeOperations
import org.modelmapper.Converter
import org.modelmapper.spi.MappingContext

class CreateUserToUserConverter : Converter<CreateUserDTO, User> {
    override fun convert(p0: MappingContext<CreateUserDTO, User>?): User? {
        val source = p0?.source
        if (source != null) {
            return User(
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