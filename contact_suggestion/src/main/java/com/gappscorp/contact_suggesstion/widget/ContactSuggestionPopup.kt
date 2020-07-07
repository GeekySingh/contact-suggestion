package com.gappscorp.contact_suggesstion.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappscorp.contact_suggesstion.R
import com.gappscorp.contact_suggesstion.adapter.ContactSuggestionAdapter
import com.gappscorp.contact_suggesstion.callback.ContactCallback
import com.gappscorp.contact_suggesstion.data.mapper.ContactMapper
import com.gappscorp.contact_suggesstion.extensions.toDp
import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts
import kotlinx.android.synthetic.main.layout_contact_list.view.*

class ContactSuggestionPopup : PopupWindow {

    private val mapper = ContactMapper()
    private var dataAdapter = ContactSuggestionAdapter()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs, 0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        isOutsideTouchable = true
        contentView = View.inflate(context, R.layout.layout_contact_list, null).apply {
            with(rvContactList) {
                layoutManager = LinearLayoutManager(context)
                adapter = dataAdapter
            }
        }

        width = WindowManager.LayoutParams.MATCH_PARENT
        height = context.toDp(200)
    }

    fun setContactCallback(callback: ContactCallback) {
        dataAdapter.setContactCallback(object : ContactCallback {
            override fun onContactSelected(contact: com.gappscorp.contact_suggesstion.data.model.Contact) {
                // dismiss this window and give result to parent callback
                dismiss()
                callback.onContactSelected(contact)
            }
        })
    }

    fun query(name: String) {
        Contacts.getQuery().whereContains(Contact.Field.DisplayName, name).find().run {
            dataAdapter.filterList(mapper.mapList(this))
        }
    }
}