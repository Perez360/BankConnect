package com.codex.exceptions

import com.codex.enums.SystemErrorCode


class ServiceException(override val message: String?, val code: SystemErrorCode, override val cause: Throwable?) :
    RuntimeException(message, cause) {
    constructor(code: SystemErrorCode, message: String?) : this(message, code, null)
    constructor(code: SystemErrorCode, message: String?, cause: Throwable?) : this(message, code, cause)
    constructor(cause: Throwable?) : this(cause?.toString(), SystemErrorCode.INTERNAL_SERVER_ERROR, cause)
    constructor() : this("We really fucked up", SystemErrorCode.INTERNAL_SERVER_ERROR, null)
}
