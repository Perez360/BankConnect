package com.codex.controllers.impl

import com.codex.controllers.UserController
import com.codex.exceptions.ServiceException
import com.codex.models.*
import com.codex.repos.UserDAO
import com.codex.shared.APIResponse
import com.codex.util.Mapper
import com.codex.util.PasswordOperations
import com.codex.util.validators.UserValidator
import com.codex.util.wrapFailureInResponse
import com.codex.util.wrapSuccessInResponse
import org.bson.types.ObjectId
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class UserControllerImpl(override val di: DI) : UserController, DIAware {
    private val userDAO: UserDAO by di.instance<UserDAO>()
    override fun createUser(createUserDTO: CreateUserDTO): APIResponse<User> {

        val user: User = Mapper.convert<CreateUserDTO, User>(createUserDTO)

//        validating user details
        UserValidator.validate(user)
//        Encrypting password
        user.password = PasswordOperations.encrypt(createUserDTO.password)

        val newUser = userDAO.create(user)
            ?: throw ServiceException(-4, "Failed to save user")
        return wrapSuccessInResponse(newUser)
    }

    override fun getUserById(id: String): APIResponse<User> {
        val userID = ObjectId(id)
        val oneUser = userDAO.get(userID)
            ?: return wrapFailureInResponse("No user found with this id: $id")
        return wrapSuccessInResponse(oneUser)
    }

    override fun updateUser(updateUserDTO: UpdateUserDTO): APIResponse<User> {

        val isUserExists = userDAO.exists(ObjectId(updateUserDTO.id))
        if (!isUserExists) return wrapFailureInResponse("User does not exist with this ID: ${updateUserDTO.id}")

        val mappedUser: User = Mapper.convert<UpdateUserDTO, User>(updateUserDTO)
        //        Validating user details
        UserValidator.validate(mappedUser)

        val oneUser = userDAO.get(ObjectId())
        if (oneUser == mappedUser) return wrapSuccessInResponse(oneUser)

        val updatedUser = userDAO.update(mappedUser)
            ?: throw ServiceException(-4, "Failed to update user")
        return wrapSuccessInResponse(updatedUser)
    }

    override fun listUsers(page: Int?, size: Int?): APIResponse<PaginationModel<List<User>>> {
        val listOfUsers = userDAO.list(page, size)
        return wrapSuccessInResponse(listOfUsers)
    }

    override fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<PaginationModel<List<User>>> {
        val listOfUsers = userDAO.filter(filterUserRequest)
        return wrapSuccessInResponse(listOfUsers)
    }

    override fun deleteUser(id: String): APIResponse<Boolean> {
        val userID = ObjectId(id)
        val isUserExists = userDAO.exists(userID)
        if (!isUserExists) return wrapFailureInResponse("User does not exist with this ID: $id")

        val deleteCount = userDAO.delete(userID)
        if (deleteCount < 1) throw ServiceException(-4, "Failed to delete user")
        return wrapSuccessInResponse(true)
    }

    override fun deleteAllUsers(): APIResponse<Boolean> {
        val deleteCount = userDAO.deleteAll()
        if (userDAO.count() > deleteCount) throw ServiceException(-4, "Failed to delete all users")
        return wrapSuccessInResponse(true)
    }
}