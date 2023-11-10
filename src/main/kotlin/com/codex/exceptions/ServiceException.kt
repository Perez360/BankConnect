package com.codex.exceptions

import com.codex.enums.ErrorCode


class ServiceException(override val message: String?, val code: ErrorCode, override val cause: Throwable?) :
    RuntimeException(message, cause) {
    constructor(code: ErrorCode, message: String?) : this(message, code, null)
    constructor(code: ErrorCode, message: String?, cause: Throwable?) : this(message, code, cause)
    constructor(cause: Throwable?) : this(cause?.toString(), ErrorCode.INTERNAL_SERVER_ERROR, cause)
    constructor() : this("We really fucked up", ErrorCode.INTERNAL_SERVER_ERROR, null)
}
