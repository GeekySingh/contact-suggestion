package com.gappscorp.contact_suggesstion.callback

import com.gappscorp.contact_suggesstion.data.model.Contact

interface ContactCallback {
    fun onContactSelected(contact: Contact)
}