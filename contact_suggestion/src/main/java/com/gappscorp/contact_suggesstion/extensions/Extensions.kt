package com.gappscorp.contact_suggesstion.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.gappscorp.contact_suggesstion.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

fun Context.hasContactsPermission() = ContextCompat.checkSelfPermission(
    this,
    Manifest.permission.READ_CONTACTS
) == PackageManager.PERMISSION_GRANTED

fun Context.requestContactPermission(callback: (granted: Boolean) -> Unit) {
    Dexter.withContext(this)
        .withPermission(Manifest.permission.READ_CONTACTS)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                callback.invoke(true)
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                callback.invoke(false)
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest,
                token: PermissionToken
            ) {
                token.continuePermissionRequest()
            }
        }).check()
}

fun Context.infoDialog(message: String, callback: (allwed: Boolean) -> Unit) {
    AlertDialog.Builder(this)
        .setTitle(R.string.alert_title_info)
        .setMessage(message)
        .setPositiveButton(
            R.string.alert_ok
        ) { dialog, _ ->
            dialog.dismiss()
            callback.invoke(true)
        }
        .setNegativeButton(
            R.string.alert_cancel
        ) { dialog, _ ->
            dialog.dismiss()
            callback.invoke(false)
        }
        .show()
}