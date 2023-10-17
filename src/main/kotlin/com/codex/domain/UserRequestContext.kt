package com.codex.domain


import com.codex.models.UserRequest


object UserRequestContext {
    private val userRequestThread: ThreadLocal<UserRequest> = ThreadLocal()


    fun getUserRequest(): UserRequest? = userRequestThread.get()
    fun setUserRequest(userRequest: UserRequest) {
        userRequestThread.set(userRequest)
    }

    fun clear() {
        userRequestThread.set(null)
    }
}