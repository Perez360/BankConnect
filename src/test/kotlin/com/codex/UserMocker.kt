package com.codex

import com.codex.dtos.CreateUserDTO
import com.codex.dtos.UpdateUserDTO
import com.codex.enums.ContactType
import com.codex.enums.Gender
import com.codex.enums.UserStatus
import com.codex.models.Contact
import com.codex.models.User
import org.apache.commons.lang3.RandomStringUtils
import org.bson.types.ObjectId
import java.time.LocalDate
import kotlin.random.Random

class UserMocker {
    companion object {
        fun mockedUser(): User {
            return User(
                id = ObjectId(),
                firstName = RandomStringUtils.randomAlphabetic(10),
                middleName = RandomStringUtils.randomAlphabetic(10),
                lastName = RandomStringUtils.randomAlphabetic(10),
                placeOfBirth = RandomStringUtils.randomAlphabetic(10),
                homeTown = RandomStringUtils.randomAlphabetic(10),
                gender = Gender.values().random(),
                status = UserStatus.values().random(),
                password = RandomStringUtils.random(kotlin.random.Random.nextInt(8, 20)),
                msisdn = RandomStringUtils.randomAlphabetic(10),
                contacts = mutableListOf(
                    Contact(
                        content = RandomStringUtils.random(Random.nextInt(20)),
                        contactType = ContactType.values().random(),
                        active = Random.nextBoolean()
                    )
                ),
                dateOfBirth = LocalDate.of(Random.nextInt(1, 12), Random.nextInt(1, 12), Random.nextInt(1, 28))
            )
        }

        fun mockedAddUserDTO(): CreateUserDTO {
            return CreateUserDTO(
                firstName = RandomStringUtils.randomAlphabetic(10),
                middleName = RandomStringUtils.randomAlphabetic(10),
                lastName = RandomStringUtils.randomAlphabetic(10),
                placeOfBirth = RandomStringUtils.randomAlphabetic(10),
                homeTown = RandomStringUtils.randomAlphabetic(10),
                gender = Gender.values().random(),
                status = UserStatus.values().random(),
                password = RandomStringUtils.random(Random.nextInt(8, 20)),
                msisdn = RandomStringUtils.randomAlphabetic(10),
                contacts = mutableListOf(
                    Contact(
                        content = RandomStringUtils.random(Random.nextInt(20)),
                        contactType = ContactType.values().random(),
                        active = Random.nextBoolean()
                    )
                ),
                dateOfBirth = LocalDate.of(Random.nextInt(1, 12), Random.nextInt(1, 12), Random.nextInt(1, 28))
                    .toString()
            )
        }

        fun mockedUpdateUserDTO(): UpdateUserDTO {
            return UpdateUserDTO(
                id = ObjectId().toHexString(),
                firstName = RandomStringUtils.randomAlphabetic(10),
                middleName = RandomStringUtils.randomAlphabetic(10),
                lastName = RandomStringUtils.randomAlphabetic(10),
                placeOfBirth = RandomStringUtils.randomAlphabetic(10),
                homeTown = RandomStringUtils.randomAlphabetic(10),
                gender = Gender.values().random(),
                status = UserStatus.values().random(),
                password = RandomStringUtils.random(Random.nextInt(8, 20)),
                msisdn = RandomStringUtils.randomAlphabetic(10),
                contacts = mutableListOf(
                    Contact(
                        content = RandomStringUtils.random(Random.nextInt(20)),
                        contactType = ContactType.values().random(),
                        active = Random.nextBoolean()
                    )
                ),
                dateOfBirth = LocalDate.of(Random.nextInt(1, 12), Random.nextInt(1, 12), Random.nextInt(1, 28))
                    .toString()
            )
        }
    }
}
