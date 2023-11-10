package com.codex.shared


data class APIResponse<T>(
     val systemCode: String,
     val code: String,
     val message: String,
    val data: T?
)

open class BaseResponse(
    open val systemCode: String,
    open val code: String,
    open val message: String,
)




//class APIResponse<T>(
//    override val systemCode: String,
//    override val code: String,
//    override val message: String,
//    val data: T
//) : BaseResponse(
//    systemCode, code, message
//)

