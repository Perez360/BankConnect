package com.codex.controllers.impl

import com.codex.controllers.UserController
import com.codex.core.config.UserCacheManager
import com.codex.dtos.CreateUserDTO
import com.codex.dtos.LoginDTO
import com.codex.dtos.Session
import com.codex.dtos.UpdateUserDTO
import com.codex.enums.ErrorCode
import com.codex.exceptions.ServiceException
import com.codex.models.FilterUserRequest
import com.codex.models.PaginationModel
import com.codex.models.User
import com.codex.repos.SessionRepo
import com.codex.repos.UserRepo
import com.codex.shared.APIResponse
import com.codex.util.PasswordOperations
import com.codex.util.validators.UserValidator
import com.codex.util.wrapFailureInResponse
import com.codex.util.wrapSuccessInResponse
import org.bson.types.ObjectId
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class UserControllerImpl(override val di: DI) : UserController, DIAware {
    private val userRepo: UserRepo by di.instance()
    private val sessionRepo: SessionRepo by di.instance()
    override fun createUser(createUserDTO: CreateUserDTO): APIResponse<User> {

        val user: User = User.fromCreateUserDTO(createUserDTO)

        UserValidator.validate(user)
        user.password = PasswordOperations.encrypt(createUserDTO.password)

        val newUser = userRepo.create(user)
            ?: throw ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save user")
        return wrapSuccessInResponse(newUser)
    }

    override fun getUserById(id: String): APIResponse<User> {
        val userID = ObjectId(id)

        val cachedUser = UserCacheManager.get(userID)
        if (cachedUser != null) {
            return wrapSuccessInResponse(cachedUser)
        }

        val oneUser = userRepo.get(userID)
            ?: return wrapFailureInResponse("No user found with this id: $id")

        return wrapSuccessInResponse(oneUser)
    }

    override fun updateUser(updateUser: UpdateUserDTO): APIResponse<User> {
        val id = ObjectId(updateUser.id)
        val isUserExists = userRepo.exists(id)
        if (!isUserExists) return wrapFailureInResponse("User does not exist with this ID: ${updateUser.id}")

        val mappedUser: User = User.fromUpdateUserDTO(updateUser)
        //        Validating user details
        UserValidator.validate(mappedUser)

        val oneUser = userRepo.get(ObjectId())
        if (oneUser == mappedUser) return wrapSuccessInResponse(oneUser)

        val updatedUser = userRepo.update(mappedUser)
            ?: throw ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to update user")
        return wrapSuccessInResponse(updatedUser)
    }

    override fun login(loginDTO: LoginDTO): APIResponse<Session> {
        val user = userRepo.get(loginDTO.email)
            ?: throw ServiceException(ErrorCode.UNAUTHORIZED, "Username or password is not valid")
        val session = sessionRepo.create(user.id)

        return wrapSuccessInResponse(session)
    }

    override fun listUsers(page: Int?, size: Int?): APIResponse<PaginationModel<User>> {
        val page1 = page ?: 1
        val size1 = size ?: 10
        val listOfUsers = userRepo.list(page1, size1)
        return wrapSuccessInResponse(listOfUsers)
    }

    override fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<PaginationModel<User>> {
        val listOfUsers = userRepo.filter(filterUserRequest)
        return wrapSuccessInResponse(listOfUsers)
    }

    override fun deleteUser(id: String): APIResponse<User> {
        val userID = ObjectId(id)
        val isUserExists = userRepo.exists(userID)
        if (!isUserExists) return wrapFailureInResponse("User does not exist with this ID: $id")

        val deletedUser = userRepo.delete(userID)
            ?: throw ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to delete user")

        UserCacheManager.invalidate(userID)
        return wrapSuccessInResponse(deletedUser)
    }

    override fun deleteAllUsers(): APIResponse<Boolean> {
        val deleteCount = userRepo.deleteAll()
        if (userRepo.count() > deleteCount) throw ServiceException(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "Failed to delete all users"
        )
        UserCacheManager.invalidateAll()
        return wrapSuccessInResponse(true)
    }
}