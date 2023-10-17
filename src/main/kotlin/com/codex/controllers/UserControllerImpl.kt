package com.codex.controllers

import com.codex.exceptions.ServiceException
import com.codex.models.FilterUserRequest
import com.codex.models.Pagination
import com.codex.models.User
import com.codex.repos.UserDAO
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
    private val userDAO: UserDAO by di.instance<UserDAO>()
    override fun createUser(user: User): APIResponse<User> {
//        validating user details
        UserValidator.validate(user)

//        Encrypting password
        user.password = PasswordOperations.encrypt(user.password)


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

    override fun updateUser(updatedUser: User): APIResponse<User> {
        //Validating user details
        UserValidator.validate(updatedUser)

        val isUserExists = userDAO.exists(updatedUser.id)
        if (!isUserExists) return wrapFailureInResponse("User does not exist with this ID: ${updatedUser.id}")

        val oneUser = userDAO.update(updatedUser)
            ?: throw ServiceException(-4, "Failed to update user")
        return wrapSuccessInResponse(oneUser)
    }

    override fun listUsers(page: Int?, size: Int?): APIResponse<Pagination<List<User>>> {
        val startIndex = page ?: 1
        val endIndex = size ?: 10
        val listOfUsers = userDAO.list(startIndex, endIndex)
        return wrapSuccessInResponse(Pagination.parginate(startIndex, endIndex, listOfUsers))
    }

    override fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<Pagination<List<User>>> {
        val listOfUsers = userDAO.filter(filterUserRequest)
        return wrapSuccessInResponse(Pagination.parginate(filterUserRequest.page, filterUserRequest.size, listOfUsers))
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
        if (deleteCount < 1) throw ServiceException(-4, "Failed to delete all users")
        return wrapSuccessInResponse(true)
    }
}