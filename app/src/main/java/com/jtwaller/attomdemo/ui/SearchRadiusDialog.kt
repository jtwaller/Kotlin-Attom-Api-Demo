package com.jtwaller.attomdemo.ui

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.widget.SeekBar
import com.jtwaller.attomdemo.MainActivity
import com.jtwaller.attomdemo.R
import com.jtwaller.attomdemo.format
import kotlinx.android.synthetic.main.search_radius_dialog.view.*

class SearchRadiusDialog: DialogFragment() {

    var searchRadius = 1.0
    val minRadius = .5
    val maxRadius = 3

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            if (activity !is MainActivity) {
                throw IllegalStateException("Dialog cannot be created outside of MainActivity")
            }

            val builder = AlertDialog.Builder(it)

            val view = activity.layoutInflater.inflate(R.layout.search_radius_dialog, null)

            view.radius_seek_bar.progress =
                    (100 * (searchRadius - minRadius) / (maxRadius - minRadius)).toInt()

            view.radius_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    searchRadius = p1/100.0*(maxRadius - minRadius) + minRadius
                    view.radius_value_text.text = searchRadius.format(2)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    // nil
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    // nil
                }
            })

            builder.setView(view)
                    .setPositiveButton(R.string.set,
                            DialogInterface.OnClickListener { dialog, id ->
                                // Dismiss
                            })
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                // Dismiss
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}