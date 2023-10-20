package com.codex.models

import com.codex.enums.ContactType
import dev.morphia.annotations.Entity

@Entity
data class Contact(
    var content: String,
    var active: Boolean,
    var contactType: ContactType = ContactType.NONE
)