package com.gappscorp.contact_suggesstion.data.mapper

import com.github.tamir7.contacts.Contact

class ContactMapper {

    fun mapList(source: List<Contact>): List<com.gappscorp.contact_suggesstion.data.model.Contact> {
        val finalList = mutableListOf<com.gappscorp.contact_suggesstion.data.model.Contact>()
        source.forEach {
            finalList.addAll(map(it))
        }
        return finalList.distinctBy { it.number }
    }

    private fun map(source: Contact): List<com.gappscorp.contact_suggesstion.data.model.Contact> {
        return source.phoneNumbers.map {
            com.gappscorp.contact_suggesstion.data.model.Contact(
                source.displayName,
                it.number.replace(" ", "")
            )
        }
    }
}