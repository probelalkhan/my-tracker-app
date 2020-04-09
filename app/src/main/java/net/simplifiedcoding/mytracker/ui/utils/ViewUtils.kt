package net.simplifiedcoding.mytracker.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import net.simplifiedcoding.mytracker.R
import net.simplifiedcoding.mytracker.ui.home.SurveyCallback

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}


fun Context.showInputDialog(@LayoutRes layoutID: Int, callback: SurveyCallback) {
    val view = LayoutInflater.from(this).inflate(
        layoutID,
        null
    )
    val editTextName = view.findViewById<EditText>(R.id.edit_text_survey_name)
    AlertDialog.Builder(this).apply {
        setTitle(getString(R.string.survey_name))
        setView(view)
        setPositiveButton(getString(R.string.ok)) { _, _ ->
            callback.onSurveyStarted(
                editTextName.text.toString().trim()
            )
        }
    }.create().show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun Context.showDialog(
    title: String?, msg: String?, positiveLabel: String?,
    positiveOnClick: DialogInterface.OnClickListener?,
    negativeLabel: String?, negativeOnClick: DialogInterface.OnClickListener?,
    isCancelAble: Boolean
) {
    val builder =
        androidx.appcompat.app.AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setCancelable(isCancelAble)
    builder.setMessage(msg)
    builder.setPositiveButton(positiveLabel, positiveOnClick)
    builder.setNegativeButton(negativeLabel, negativeOnClick)
    val alert = builder.create()
    alert.show()
}