package com.codex.controllers

import com.codex.UserMocker.Companion.mockedAddUserDTO
import com.codex.UserMocker.Companion.mockedUpdateUserDTO
import com.codex.UserMocker.Companion.mockedUser
import com.codex.controllers.impl.UserControllerImpl
import com.codex.domain.CODE_FAILURE
import com.codex.domain.CODE_SERVICE_FAILURE
import com.codex.domain.CODE_SERVICE_SUCCESS
import com.codex.domain.CODE_SUCCESS
import com.codex.enums.ErrorCode
import com.codex.exceptions.ServiceException
import com.codex.models.FilterUserRequest
import com.codex.models.User
import com.codex.models.toPaginationModel
import com.codex.repos.UserRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerImplTest {
    private lateinit var log: Logger
    private lateinit var underTest: UserController
    private lateinit var userRepoMockk: UserRepo
    private lateinit var di: DI
    private lateinit var listOfMockedUsers: List<User>

    @BeforeEach
    fun setUp() {
        log = LoggerFactory.getLogger(this::class.java)
        listOfMockedUsers = listOf(mockedUser(), mockedUser(), mockedUser(), mockedUser())


        userRepoMockk = mockk(relaxed = true)
        di = DI {
            bindSingleton { userRepoMockk }
        }
        underTest = UserControllerImpl(di)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should create a new user with a given user detail`() {
        //GIVEN
        val newUser = mockedAddUserDTO()
        val savedUser = mockedUser()
        every { userRepoMockk.create(any()) } returns savedUser

        //WHEN
        val expected = underTest.createUser(newUser)
        verify { userRepoMockk.create(any()) }

        //THEN
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
        Assertions.assertThat(expected).isNotNull

    }

    @Test
    fun `it should throw ServiceException when creating a user`() {
        //GIVEN
        val newUser = mockedAddUserDTO()
        every { userRepoMockk.create(any()) } throws ServiceException(
            ErrorCode.values().random(),
            "Failed to create user"
        )
        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.createUser(newUser)
        }
        verify { userRepoMockk.create(any()) }

    }

    @Test
    fun `it should get a user with a given id`() {
        //GIVEN
        val userID = ObjectId()
        val oneUser = mockedUser()
        every { userRepoMockk.get(userID) } returns oneUser

        //WHEN
        val expected = underTest.getUserById(userID.toHexString())
        verify { userRepoMockk.get(userID) }

        //THEN
        Assertions.assertThat(expected).isNotNull
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)

    }

    @Test
    fun `it cannot get a user with a given id because user does not exists in DB`() {
        //GIVEN
        val userID = ObjectId()
        every { userRepoMockk.get(userID) } returns null

        //WHEN
        val expected = underTest.getUserById(userID.toHexString())
        verify { userRepoMockk.get(userID) }

        //THEN
        Assertions.assertThat(expected.data).isNull()
        Assertions.assertThat(expected.code).isEqualTo(CODE_FAILURE)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_FAILURE)
    }

    @Test
    fun `it should throw ServiceException when getting a user using given id`() {
        //GIVEN
        val userID = ObjectId()
        every { userRepoMockk.get(userID) } throws ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "Error fetching  user"
        )

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.getUserById(userID.toHexString())
        }

        verify { userRepoMockk.get(userID) }
    }


    @Test
    fun `it should list some users`() {
        //GIVEN
        val page = 1
        val size = 100
        every { userRepoMockk.list(page, size) } returns listOfMockedUsers.toPaginationModel(page, size)

        //WHEN
        val expected = underTest.listUsers(page, size)
        verify { userRepoMockk.list(page, size) }

        //THEN
        Assertions.assertThat(expected.data?.data).isEqualTo(listOfMockedUsers)
        Assertions.assertThat(expected.data?.totalElements).isEqualTo(listOfMockedUsers.size)
        Assertions.assertThat(expected.data?.size).isEqualTo(size)
        Assertions.assertThat(expected.data?.page).isEqualTo(page)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }


    @Test
    fun `it should list no users, thus show be empty list`() {
        //GIVEN
        val page = 1
        val size = 100
        every { userRepoMockk.list(page, size) } returns emptyList<User>().toPaginationModel(page, size)

        //WHEN
        val expected = underTest.listUsers(page, size)
        verify { userRepoMockk.list(page, size) }

        //THEN
        Assertions.assertThat(expected.data?.data).isEqualTo(emptyList<User>())
        Assertions.assertThat(expected.data?.totalElements).isEqualTo(0)
        Assertions.assertThat(expected.data?.size).isEqualTo(size)
        Assertions.assertThat(expected.data?.page).isEqualTo(page)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it should throw ServiceException when listing users`() {
        //GIVEN
        val page = 1
        val size = 100
        every { userRepoMockk.list(page, size) } throws ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "Unable to list users"
        )

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.listUsers(page, size)
        }

        verify { userRepoMockk.list(page, size) }
    }

    @Test
    fun `it should filter no users`() {
        //GIVEN
        val filterRequest = FilterUserRequest.fromMap(mapOf("firstName" to RandomStringUtils.randomAlphabetic(10)))
        every { userRepoMockk.filter(filterRequest) } returns emptyList<User>().toPaginationModel(
            filterRequest.page, filterRequest.size
        )

        //WHEN
        val expected = underTest.filterUsers(filterRequest)
        verify { userRepoMockk.filter(filterRequest) }

        //THEN
        Assertions.assertThat(expected.data?.data).isEqualTo(emptyList<User>())
        Assertions.assertThat(expected.data?.data?.size).isEqualTo(0)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it should filter some users`() {
        //GIVEN
        val oneUser = listOfMockedUsers.first()
        val filterRequest = FilterUserRequest.fromMap(mapOf("middleName" to oneUser.middleName))
        every { userRepoMockk.filter(filterRequest) } returns listOfMockedUsers.toPaginationModel(
            filterRequest.page, filterRequest.size
        )

        //WHEN
        val expected = underTest.filterUsers(filterRequest)
        verify { userRepoMockk.filter(filterRequest) }

        //THEN
        Assertions.assertThat(expected.data?.totalElements).isEqualTo(listOfMockedUsers.size)
        Assertions.assertThat(expected.data?.data?.first()).isEqualTo(oneUser)
        Assertions.assertThat(expected.data?.data?.size).isEqualTo(listOfMockedUsers.size)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }


    @Test
    fun `it should throw ServiceException when filtering  users`() {
        //GIVEN
        val filterRequest = FilterUserRequest.fromMap(mapOf("lastName" to RandomStringUtils.randomAlphabetic(10)))
        every { userRepoMockk.filter(filterRequest) } throws ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "Unable to filter users"
        )

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.filterUsers(filterRequest)
        }

        verify { userRepoMockk.filter(filterRequest) }
    }


    @Test
    fun `it should update an user with a new given user info`() {
        //GIVEN
        val updatedUserData = mockedUpdateUserDTO()
        val savedUser = mockedUser()
        every { userRepoMockk.exists(any()) } returns true
        every { userRepoMockk.update(any()) } returns savedUser

        //WHEN
        val expected = underTest.updateUser(updatedUserData)
        verify { userRepoMockk.exists(any()) }
        verify { userRepoMockk.update(any()) }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(savedUser)
        Assertions.assertThat(expected.data).isSameAs(savedUser)
        Assertions.assertThat(expected.data?.id).isEqualTo(savedUser.id)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }


    @Test
    fun `it should throws ServiceException in the process of updating`() {
        //GIVEN
        val updatedUserData = mockedUpdateUserDTO()
        every { userRepoMockk.exists(any()) } returns true
        every { userRepoMockk.update(any()) } throws ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR, "Failed to update user"
        )

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.updateUser(updatedUserData)
        }
        verify { userRepoMockk.exists(any()) }
        verify { userRepoMockk.update(any()) }
    }


    @Test
    fun `it should delete a user with a given id`() {
        //GIVEN
        val oneUserID = ObjectId("6531129a47cd2414681c3657")
        val mockedUser = mockedUser()
        every { userRepoMockk.exists(oneUserID) } returns true
        every { userRepoMockk.delete(oneUserID) } returns mockedUser

        //WHEN
        val expected = underTest.deleteUser(oneUserID.toHexString())
        verify { userRepoMockk.exists(oneUserID) }
        verify { userRepoMockk.delete(oneUserID) }

        //THEN
        Assertions.assertThat(expected.data).isNotNull
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it cannot delete an address because the id does not exist`() {
        //GIVEN
        val userID = ObjectId()
        every { userRepoMockk.exists(userID) } returns false
        every { userRepoMockk.get(userID) } returns null

        //WHEN
        val expected = underTest.deleteUser(userID.toHexString())
        verify { userRepoMockk.exists(userID) }
        verify(exactly = 0) { userRepoMockk.get(userID) }

        //THEN
        Assertions.assertThat(expected.code).isEqualTo(CODE_FAILURE)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_FAILURE)
    }

    @Test
    fun `it should delete all users`() {
        //GIVEN
        every { userRepoMockk.deleteAll() } returns 1L

        //WHEN
        val expected = underTest.deleteAllUsers()
        verify { userRepoMockk.deleteAll() }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(true)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it should throw ServiceException when deleting all addresses`() {
        //GIVEN
        every { userRepoMockk.deleteAll() } throws ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR, "Error whiles deleting all users"
        )

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.deleteAllUsers()
        }

        verify { userRepoMockk.deleteAll() }
    }
}