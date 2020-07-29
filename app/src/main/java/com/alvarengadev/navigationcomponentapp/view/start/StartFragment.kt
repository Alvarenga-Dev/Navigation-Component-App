package com.alvarengadev.navigationcomponentapp.view.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.alvarengadev.navigationcomponentapp.R
import com.alvarengadev.navigationcomponentapp.extensions.navigateWithAnimation
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_next.setOnClickListener {
            findNavController().navigateWithAnimation(R.id.action_startFragment_to_loginFragment)
        }
    }
}
