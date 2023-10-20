package com.codex.models

data class PaginationModel<T>(
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long,
    val data: T
) {
    companion object {
        fun <T> from(page: Int, size: Int, totalPages: Int, totalElements: Long, data: T): PaginationModel<T> {
            return PaginationModel(
                page = page,
                size = size,
                totalPages = totalPages,
                totalElements = totalElements,
                data = data
            )
        }
    }
}