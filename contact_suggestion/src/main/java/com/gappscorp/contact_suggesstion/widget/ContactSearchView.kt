package com.gappscorp.contact_suggesstion.widget

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import com.gappscorp.contact_suggesstion.R
import com.gappscorp.contact_suggesstion.callback.ContactCallback
import com.gappscorp.contact_suggesstion.data.model.Contact
import com.gappscorp.contact_suggesstion.extensions.hasContactsPermission
import com.gappscorp.contact_suggesstion.extensions.infoDialog
import com.gappscorp.contact_suggesstion.extensions.requestContactPermission

private const val TAG = "ContactSearchView"

class ContactSearchView : AppCompatAutoCompleteTextView {

    private var _showInfoDialog = true
    private var _infoDialogMessage: String = ""

    private var callback: ((contact: Contact) -> Unit)? = null
    private var contactSuggestionPopup: ContactSuggestionPopup? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(
        context: Context,
        attributeSet: AttributeSet?
    ) {
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.ContactSearchView)
            _showInfoDialog = typedArray.getBoolean(
                R.styleable.ContactSearchView_csv_showInfoDialog,
                _showInfoDialog
            )
            _infoDialogMessage =
                typedArray.getString(R.styleable.ContactSearchView_csv_infoDialogMessage)
                    ?: context.getString(R.string.alert_contact_permission_message)
            typedArray.recycle()

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (_showInfoDialog) {
                        if (!context.hasContactsPermission()) {
                            context.infoDialog(_infoDialogMessage) {
                                when (it) {
                                    true -> askContactPermission()
                                    false -> Log.d(
                                        TAG,
                                        "User has denied asking contacts permission!"
                                    )
                                }
                            }
                        } else {
                            askContactPermission()
                        }
                    } else {
                        askContactPermission()
                    }
                }
            }
        }
    }

    private fun askContactPermission() {
        context.requestContactPermission {
            when (it) {
                true -> beginSearchSuggestion()
                false -> Log.d(TAG, "User has denied granting contacts permission!")
            }
        }
    }

    private fun beginSearchSuggestion() {
        doAfterTextChanged { name: Editable? ->
            if (name.isNullOrEmpty()) {
                contactSuggestionPopup?.dismiss()
            } else {
                if (contactSuggestionPopup == null)
                    initContactSuggestionPopupWindow()
                if (!contactSuggestionPopup?.isShowing!!) {
                    contactSuggestionPopup?.showAsDropDown(this)
                }
                contactSuggestionPopup?.query(name.toString())
                requestFocus()
            }
        }
    }

    private fun initContactSuggestionPopupWindow() {
        if (contactSuggestionPopup == null)
            contactSuggestionPopup = ContactSuggestionPopup(context)
        contactSuggestionPopup?.setContactCallback(object : ContactCallback {
            override fun onContactSelected(contact: Contact) {
                callback?.invoke(contact)
            }
        })
    }

    fun setContactSelectionListener(callback: (contact: Contact) -> Unit) {
        this.callback = callback
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        contactSuggestionPopup?.dismiss()
    }
}