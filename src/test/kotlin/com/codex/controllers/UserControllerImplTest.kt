package com.codex.controllers

import com.codex.controllers.impl.UserControllerImpl
import com.codex.domain.CODE_FAILURE
import com.codex.domain.CODE_SERVICE_FAILURE
import com.codex.domain.CODE_SERVICE_SUCCESS
import com.codex.domain.CODE_SUCCESS
import com.codex.exceptions.ServiceException
import com.codex.models.CreateUserDTO
import com.codex.models.FilterUserRequest
import com.codex.models.UpdateUserDTO
import com.codex.models.User
import com.codex.repos.UserDAO
import com.codex.util.converters.LocalDateTimeTypeManufacturer
import com.codex.util.converters.LocalDateTypeManufacturer
import com.codex.util.converters.ObjectIdTypeManufacturer
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl
import java.time.LocalDate
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerImplTest {
    private lateinit var log: Logger
    private lateinit var factory: PodamFactory
    private lateinit var underTest: UserController
    private lateinit var userDAOMockk: UserDAO
    private lateinit var di: DI
    private lateinit var listOfUsers: List<User>

    @BeforeEach
    fun setUp() {
        log = LoggerFactory.getLogger(this::class.java)
        factory = PodamFactoryImpl()
        factory.strategy.addOrReplaceTypeManufacturer(LocalDate::class.java, LocalDateTypeManufacturer())
        factory.strategy.addOrReplaceTypeManufacturer(LocalDateTime::class.java, LocalDateTimeTypeManufacturer())
        factory.strategy.addOrReplaceTypeManufacturer(ObjectId::class.java, ObjectIdTypeManufacturer())
        listOfUsers = factory.manufacturePojoWithFullData(List::class.java, User::class.java) as List<User>


        userDAOMockk = mockk(relaxed = true)
        di = DI {
            bindSingleton { userDAOMockk }
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
        val newUser = factory.manufacturePojoWithFullData(CreateUserDTO::class.java)
        val savedUser = factory.manufacturePojoWithFullData(User::class.java)
        every { userDAOMockk.create(any()) } returns savedUser

        //WHEN
        val expected = underTest.createUser(newUser)
        verify { userDAOMockk.create(any()) }

        //THEN
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
        Assertions.assertThat(expected).isNotNull

    }

    @Test
    fun `it should throw ServiceException when creating a user`() {
        //GIVEN
        val newUser = factory.manufacturePojoWithFullData(CreateUserDTO::class.java)
        every { userDAOMockk.create(any()) } throws
                ServiceException(-4, "Failed to create user")
        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.createUser(newUser)
        }
        verify { userDAOMockk.create(any()) }

    }

    @Test
    fun `it should get a user with a given id`() {
        //GIVEN
        val userID = ObjectId()
        val oneUser = factory.manufacturePojoWithFullData(User::class.java)
        every { userDAOMockk.get(userID) } returns oneUser

        //WHEN
        val expected = underTest.getUserById(userID.toHexString())
        verify { userDAOMockk.get(userID) }

        //THEN
        Assertions.assertThat(expected).isNotNull
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)

    }

    @Test
    fun `it cannot get a user with a given id because user does not exists in DB`() {
        //GIVEN
        val userID = ObjectId()
        every { userDAOMockk.get(userID) } returns null

        //WHEN
        val expected = underTest.getUserById(userID.toHexString())
        verify { userDAOMockk.get(userID) }

        //THEN
        Assertions.assertThat(expected.data).isNull()
        Assertions.assertThat(expected.code).isEqualTo(CODE_FAILURE)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_FAILURE)
    }

    @Test
    fun `it should throw ServiceException when getting a user using given id`() {
        //GIVEN
        val userID = ObjectId()
        every { userDAOMockk.get(userID) } throws
                ServiceException(-1, "Error fetching  user")

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.getUserById(userID.toHexString())
        }

        verify { userDAOMockk.get(userID) }
    }


    @Test
    fun `it should list some users`() {
        //GIVEN
        val page = 1
        val size = 100
        every { userDAOMockk.list(page, size) } returns listOfUsers

        //WHEN
        val expected = underTest.listUsers(page, size)
        verify { userDAOMockk.list(page, size) }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(listOfUsers)
        Assertions.assertThat(expected.data?.size).isEqualTo(listOfUsers.size)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }


    @Test
    fun `it should list no users, thus show be empty list`() {
        //GIVEN
        val page = 1
        val size = 100
        every { userDAOMockk.list(page, size) } returns emptyList()

        //WHEN
        val expected = underTest.listUsers(page, size)
        verify { userDAOMockk.list(page, size) }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(emptyList<User>())
        Assertions.assertThat(expected.data?.size).isEqualTo(0)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it should throw ServiceException when listing users`() {
        //GIVEN
        val page = 1
        val size = 100
        every { userDAOMockk.list(page, size) } throws
                ServiceException(-4, "Unable to list users")

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.listUsers(page, size)
        }

        verify { userDAOMockk.list(page, size) }
    }

    @Test
    fun `it should filter no users`() {
        //GIVEN
        val filterRequest = factory.manufacturePojoWithFullData(FilterUserRequest::class.java)
        every { userDAOMockk.filter(filterRequest) } returns emptyList()

        //WHEN
        val expected = underTest.filterUsers(filterRequest)
        verify { userDAOMockk.filter(filterRequest) }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(emptyList<User>())
        Assertions.assertThat(expected.data?.size).isEqualTo(0)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it should filter some users`() {
        //GIVEN
        val filterRequest = factory.manufacturePojoWithFullData(FilterUserRequest::class.java)
        every { userDAOMockk.filter(filterRequest) } returns listOfUsers

        //WHEN
        val expected = underTest.filterUsers(filterRequest)
        verify { userDAOMockk.filter(filterRequest) }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(listOfUsers)
        Assertions.assertThat(expected.data?.size).isEqualTo(listOfUsers.size)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }


    @Test
    fun `it should throw ServiceException when filtering  users`() {
        //GIVEN
        val filterRequest = factory.manufacturePojoWithFullData(FilterUserRequest::class.java)
        every { userDAOMockk.filter(filterRequest) } throws
                ServiceException(-4, "Unable to filter users")

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.filterUsers(filterRequest)
        }

        verify { userDAOMockk.filter(filterRequest) }
    }


    @Test
    fun `it should update an user with a new given user info`() {
        //GIVEN
        val updatedUserData = factory.manufacturePojoWithFullData(UpdateUserDTO::class.java)
        val savedUser = factory.manufacturePojoWithFullData(User::class.java)
        every { userDAOMockk.exists(any()) } returns true
        every { userDAOMockk.update(any()) } returns savedUser

        //WHEN
        val expected = underTest.updateUser(updatedUserData)
        verify { userDAOMockk.exists(any()) }
        verify { userDAOMockk.update(any()) }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(updatedUserData)
        Assertions.assertThat(expected.data).isSameAs(updatedUserData)
        Assertions.assertThat(expected.data?.id).isEqualTo(updatedUserData.id)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }


    @Test
    fun `it should throws ServiceException in the process of updating`() {
        //GIVEN
        val updatedUserData = factory.manufacturePojoWithFullData(UpdateUserDTO::class.java)
        every { userDAOMockk.exists(any()) } returns true
        every { userDAOMockk.update(any()) } throws ServiceException(-4, "Failed to update user")

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.updateUser(updatedUserData)
        }
        verify { userDAOMockk.exists(any()) }
        verify { userDAOMockk.update(any()) }
    }


    @Test
    fun `it should delete a user with a given id`() {
        //GIVEN
        val oneUserID = ObjectId("6531129a47cd2414681c3657")
        every { userDAOMockk.exists(oneUserID) } returns true
        every { userDAOMockk.delete(oneUserID) } returns 1L

        //WHEN
        val expected = underTest.deleteUser(oneUserID.toHexString())
        verify { userDAOMockk.exists(oneUserID) }
        verify { userDAOMockk.delete(oneUserID) }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(true)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it cannot delete an address because the id does not exist`() {
        //GIVEN
        val userID = ObjectId()
        every { userDAOMockk.exists(userID) } returns false
        every { userDAOMockk.get(userID) } returns null

        //WHEN
        val expected = underTest.deleteUser(userID.toHexString())
        verify { userDAOMockk.exists(userID) }
        verify(exactly = 0) { userDAOMockk.get(userID) }

        //THEN
        Assertions.assertThat(expected.code).isEqualTo(CODE_FAILURE)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_FAILURE)
    }

    @Test
    fun `it should delete all users`() {
        //GIVEN
        every { userDAOMockk.deleteAll() } returns 1L

        //WHEN
        val expected = underTest.deleteAllUsers()
        verify { userDAOMockk.deleteAll() }

        //THEN
        Assertions.assertThat(expected.data).isEqualTo(true)
        Assertions.assertThat(expected.code).isEqualTo(CODE_SUCCESS)
        Assertions.assertThat(expected.systemCode).isEqualTo(CODE_SERVICE_SUCCESS)
    }

    @Test
    fun `it should throw ServiceException when deleting all addresses`() {
        //GIVEN
        every { userDAOMockk.deleteAll() } throws ServiceException(-4, "Error whiles deleting all users")

        //THEN
        assertThrows<ServiceException> {
            //WHEN
            underTest.deleteAllUsers()
        }

        verify { userDAOMockk.deleteAll() }
    }
}