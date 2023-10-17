package com.codex.models

import com.codex.enums.ContactType
import dev.morphia.annotations.Embedded

@Embedded
data class Contact(
    var content: String? = null,
    var active: Boolean,
    var contactType: ContactType = ContactType.NONE
)