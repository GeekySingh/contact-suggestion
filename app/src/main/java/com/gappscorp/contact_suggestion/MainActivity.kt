package com.gappscorp.contact_suggestion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gappscorp.contact_suggesstion.initializer.ContactSearchInitializer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ContactSearchInitializer.initialize(this)

        csv.setContactSelectionListener{
            Toast.makeText(this, "Name-${it.name}\nNumber-${it.number}", Toast.LENGTH_SHORT).show()
        }
    }
}