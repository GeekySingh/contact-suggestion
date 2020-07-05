package com.gappscorp.contact_suggesstion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gappscorp.contact_suggesstion.R
import com.gappscorp.contact_suggesstion.callback.ContactCallback
import com.gappscorp.contact_suggesstion.data.model.Contact
import kotlinx.android.synthetic.main.layout_contact_suggestion.view.*

class ContactSuggestionAdapter :
    RecyclerView.Adapter<ContactSuggestionAdapter.ContactSuggestionViewHolder>() {

    private var callback : ContactCallback? = null
    private val contactList = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactSuggestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_contact_suggestion, parent, false)

        return ContactSuggestionViewHolder(view).apply {
            tvName = view.tvContactName
            tvNumber = view.tvContactNumber
        }
    }

    override fun getItemCount() = contactList.size

    override fun onBindViewHolder(holder: ContactSuggestionViewHolder, position: Int) {
        holder.setData(contactList[position])
    }

    fun filterList(list: List<Contact>) {
        contactList.clear()
        contactList.addAll(list)
        notifyDataSetChanged()
    }

    fun setContactCallback(callback: ContactCallback) {
        this.callback = callback
    }

    inner class ContactSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tvName: TextView
        lateinit var tvNumber: TextView

        fun setData(data: Contact) {
            tvName.text = data.name
            tvNumber.text = data.number

            itemView.setOnClickListener {
                callback?.onContactSelected(data)
            }
        }
    }
}