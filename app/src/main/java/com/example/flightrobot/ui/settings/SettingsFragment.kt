package com.example.flightrobot.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bitvale.switcher.SwitcherX
import com.example.flightrobot.R
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.table_row_header_text_data.*
import rxhttp.RxHttp


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)


        return root
    }

}