package com.codex.shared


data class APIResponse<T>(
    val systemCode: String,
    val code: String,
    val message: String,
    val data: T?
)




