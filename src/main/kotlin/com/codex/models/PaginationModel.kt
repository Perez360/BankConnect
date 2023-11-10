package com.codex.models

import kotlin.math.ceil

data class PaginationModel<T>(
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Int,
    val data: List<T>
)

fun <E> List<E>.toPaginationModel(page: Int, size: Int): PaginationModel<E> {
    return PaginationModel(
        page = page,
        size = size,
        totalPages = ceil(this.size / 10.0).toInt(),
        totalElements = this.size,
        data = this
    )
}