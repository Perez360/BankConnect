package com.codex.util

import com.codex.domain.*
import com.codex.models.User
import com.codex.shared.APIResponse

fun <T> wrapSuccessInResponse(data: T): APIResponse<T> {
    if (UserRequestContext.getUserRequest()?.currentLanguage == "fr")
        return APIResponse(CODE_SERVICE_SUCCESS, CODE_SUCCESS, "Succès", data)
    return APIResponse(CODE_SERVICE_SUCCESS, CODE_SUCCESS, "Success", data)
}


fun <T> wrapFailureInResponse(message: String): APIResponse<T> {
    return APIResponse(CODE_SERVICE_FAILURE, CODE_FAILURE, message, null)
}



