package com.codex.repos

import com.codex.UserMocker.Companion.mockedUser
import com.codex.core.config.Configuration
import com.codex.core.config.MongoDatabaseFactory
import com.codex.enums.ErrorCode
import com.codex.exceptions.ServiceException
import com.codex.models.FilterUserRequest
import com.codex.models.User
import dev.morphia.query.UpdateException
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var underTest: UserRepo
    private var listOfSavedUsers: MutableList<User> = mutableListOf()

    init {
        Configuration.loadSystemProperties()
        MongoDatabaseFactory.connect()
    }

    @BeforeEach
    internal fun setUp() {
        underTest = UserService(MongoDatabaseFactory.getDataStore())

        val newUsers: List<User> = listOf(mockedUser(), mockedUser(), mockedUser(), mockedUser(), mockedUser())
        newUsers.forEach {
            log.info("UnSaved Users: $it")
            val savedUser = underTest.create(it)
                ?: throw ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save user")
            log.info("Saved Users: $savedUser")
            listOfSavedUsers.add(savedUser)
        }
    }

    @AfterEach
    internal fun cleanUp() {
        underTest.deleteAll()
        listOfSavedUsers.clear()
    }

    @Test
    fun `it should create a user`() {
        //GIVEN
        val oneUser = mockedUser()

        //WHEN
        val expected = underTest.create(oneUser)
        log.info("it should create a user: $expected")

        //THEN
        Assertions.assertThat(expected).isEqualTo(oneUser)
    }


    @Test
    fun `it should get one user from DB with a give userID`() {
        //GIVEN
        val oneUser = listOfSavedUsers.first()

        //WHEN
        val expected = underTest.get(oneUser.id)
        log.info("it should get one user from DB with a given userID: $expected")

        //THEN
        Assertions.assertThat(expected?.id).isEqualTo(oneUser.id)
    }

    @Test
    fun `it cannot get one user from DB with a give userID`() {
        //GIVEN
        val oneUser = mockedUser()

        //WHEN
        val expected = underTest.get(oneUser.id)
        log.info("it should get one user from DB with a given userID: $expected")

        //THEN
        Assertions.assertThat(expected).isNull()
    }

    @Test
    fun `it should update a when user is in DB`() {
        //GIVEN
        val oneUser = listOfSavedUsers.first()
        oneUser.middleName = "Kwame"

        //WHEN
        val expected = underTest.update(oneUser)
        log.info("it should update when user is not it DB with: $expected")

        //THEN
        Assertions.assertThat(expected).isNotNull
        Assertions.assertThat(expected?.id).isEqualTo(oneUser.id)
    }


    @Test
    fun `it should throw UpdateException when user is not in DB`() {
        //GIVEN
        val oneUser = mockedUser()

        //THEN
        assertThrows<UpdateException> {
            //WHEN
            val expected = underTest.update(oneUser)
            log.info("it should throw UpdateException when user is not it DB with: $expected")
        }
    }

    @Test
    fun `it can list some users`() {
        //GIVEN
        val page = 1
        val size = 10
        log.info("Users to be listed: $listOfSavedUsers")
        //WHEN
        val expected = underTest.list(page, size)
        log.info("it can list some users, given the page and size : $expected")

        //THEN
        Assertions.assertThat(expected.data).isNotEmpty
        Assertions.assertThat(expected.totalElements).isEqualTo(listOfSavedUsers.size)
    }


    @Test
    fun `it should filter for some users`() {
        //GIVEN
        val oneUserToFilterFor = listOfSavedUsers.first()
        val filterUserRequest = FilterUserRequest.fromMap(mapOf("firstName" to oneUserToFilterFor.firstName , "status" to oneUserToFilterFor.status.name))

        //WHEN
        val expected = underTest.filter(filterUserRequest)
        log.info("it can list some users, given the page and size : $expected")

        //THEN
        Assertions.assertThat(expected.data).isNotEmpty
        Assertions.assertThat(expected.data.first().id).isEqualTo(oneUserToFilterFor.id)
        Assertions.assertThat(expected.data.first()).isInstanceOf(User::class.java)

    }

    @Test
    fun `it should not filter for any users, thus empty list`() {
        //GIVEN
        val oneUserToFilterFor = mockedUser()
        val filterUserRequest = FilterUserRequest.fromMap(mapOf("firstName" to RandomStringUtils.randomAlphabetic(5)))

        //WHEN
        val expected = underTest.filter(filterUserRequest)
        log.info("it should not filter for any users, thus empty list : $expected")

        //THEN
        Assertions.assertThat(expected.data).isEmpty()
        Assertions.assertThat(expected.totalElements).isEqualTo(0)
        Assertions.assertThat(expected.totalPages).isEqualTo(0)
    }

    @Test
    fun `it can delete when user exists in DB`() {
        //GIVEN
        val oneUser = listOfSavedUsers.first()

        //WHEN
        val expected = underTest.delete(oneUser.id)
        log.info("it can delete when user exists in DB : $expected")

        //THEN
        Assertions.assertThat(expected).isNotNull
    }

    @Test
    fun `it cannot delete when user does not exists in DB`() {
        //GIVEN
        val oneUserID = ObjectId()

        //WHEN
        val expected = underTest.delete(oneUserID)
        log.info("it cannot delete when user does not exist in DB : $expected")

        //THEN
        Assertions.assertThat(expected).isNull()
    }

    @Test
    fun `it should delete all`() {
        //GIVEN
        //WHEN
        val expected = underTest.deleteAll()
        log.info("it should delete all: $expected")

        //THEN
        Assertions.assertThat(expected).isGreaterThanOrEqualTo(1L)
    }
}