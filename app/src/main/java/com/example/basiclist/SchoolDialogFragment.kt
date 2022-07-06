package com.example.basiclist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.basiclist.model.School

class SchoolDialogFragment(private val school : School) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
    }



    private fun setupView(view: View) {
        view.findViewById<TextView>(R.id.name).text = school.district
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            dismiss()
        }
    }
}