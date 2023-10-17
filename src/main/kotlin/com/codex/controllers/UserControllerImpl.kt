package com.codex.controllers

import com.codex.exceptions.ServiceException
import com.codex.models.FilterUserRequest
import com.codex.models.User
import com.codex.repos.UserDAO
import com.codex.shared.APIResponse
import com.codex.util.wrapFailureInResponse
import com.codex.util.wrapSuccessInResponse
import org.bson.types.ObjectId
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class UserControllerImpl(override val di: DI) : UserController, DIAware {
    private val userDAO: UserDAO by di.instance<UserDAO>()
    override fun createUser(user: User): APIResponse<User> {
        val newUser = userDAO.create(user)
            ?: throw ServiceException(-4, "Failed to save user")
        return wrapSuccessInResponse(newUser)
    }

    override fun getUserById(id: ObjectId): APIResponse<User> {
        val oneUser = userDAO.get(id)
            ?: return wrapFailureInResponse("Failed to fetch user by ID: $id")
        return wrapSuccessInResponse(oneUser)
    }

    override fun updateUser(updatedUser: User): APIResponse<User> {
        val isUserExists = userDAO.exists(updatedUser.id)
        if (!isUserExists) return wrapFailureInResponse("User does not exist with this ID: ${updatedUser.id}")

        val oneUser = userDAO.update(updatedUser)
            ?: throw ServiceException(-4, "Failed to update user")
        return wrapSuccessInResponse(oneUser)
    }

    override fun listUsers(page: Int, size: Int): APIResponse<List<User>> {
        val listOfUsers = userDAO.list(page, size)
        return wrapSuccessInResponse(listOfUsers)
    }

    override fun filterUsers(filterUserRequest: FilterUserRequest): APIResponse<List<User>> {
        val listOfUsers = userDAO.filter(filterUserRequest)
        return wrapSuccessInResponse(listOfUsers)
    }

    override fun deleteUser(id: ObjectId): APIResponse<Boolean> {
        val isUserExists = userDAO.exists(id)
        if (!isUserExists) return wrapFailureInResponse("User does not exist with this ID: $id")

        val deleteCount = userDAO.delete(id)
        if (deleteCount < 1) throw ServiceException(-4, "Failed to delete user")
        return wrapSuccessInResponse(true)
    }

    override fun deleteAllUsers(): APIResponse<Boolean> {
        val deleteCount = userDAO.deleteAll()
        if (deleteCount < 1) throw ServiceException(-4, "Failed to delete all users")
        return wrapSuccessInResponse(true)
    }
}