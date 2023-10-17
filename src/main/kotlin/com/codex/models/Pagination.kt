package com.codex.models

data class Pagination<T>(
    val page: Int,
    val size: Int,
    val data: T
) {
    companion object {
        fun <T> parginate(page: Int, size: Int, data: T): Pagination<T> {

            return Pagination(
                page = page,
                size = size,
                data = data
            )
        }
    }
}