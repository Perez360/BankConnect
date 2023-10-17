package com.codex.models

import com.codex.domain.DEFAULT_LANG

data class UserRequest(
    val currentLanguage: String = DEFAULT_LANG,
    val currentClientVersion: String,
    val currentOS: String,
    val currentModelNumber: String
)