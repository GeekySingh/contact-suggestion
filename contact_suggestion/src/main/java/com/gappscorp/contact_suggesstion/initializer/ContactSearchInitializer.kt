package com.gappscorp.contact_suggesstion.initializer

import android.content.Context
import com.github.tamir7.contacts.Contacts

object ContactSearchInitializer {

    fun initialize(context: Context) {
        Contacts.initialize(context)
    }

}