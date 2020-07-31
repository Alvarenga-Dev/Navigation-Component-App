package com.alvarengadev.navigationcomponentapp.view.register.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.alvarengadev.navigationcomponentapp.R
import com.alvarengadev.navigationcomponentapp.extensions.dismissError
import com.alvarengadev.navigationcomponentapp.view.register.RegisterViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_profile_data.*

class ProfileDataFragment : Fragment() {

    private val registrationViewModel: RegisterViewModel by activityViewModels()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val validationFields = initValidationFields()
        listenToRegistrationViewModelEvents(validationFields)
        registerViewListener()
        registerDeviceBackStackCallback()
    }

    private fun registerViewListener() {
        input_layout_name.editText!!.addTextChangedListener {
            input_layout_name.dismissError()
        }

        input_layout_data_bio.editText!!.addTextChangedListener {
            input_layout_data_bio.dismissError()
        }

        btn_profile_data_next.setOnClickListener {
            val name = input_layout_name.editText!!.text.toString()
            val bioData = input_layout_data_bio.editText!!.text.toString()
            registrationViewModel.collectProfileData(name, bioData)
        }
    }

    private fun initValidationFields() = mapOf(
        RegisterViewModel.INPUT_NAME.first to input_layout_name,
        RegisterViewModel.INPUT_BIO.first to input_layout_data_bio
    )

    private fun listenToRegistrationViewModelEvents(validationFields: Map<String, TextInputLayout>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            if (registrationState is RegisterViewModel.RegistrationState.CollectCredentials) {
                val name = input_layout_name.editText!!.text.toString()
                val directions = ProfileDataFragmentDirections
                    .actionProfileDataFragmentToCredentialsFragment(name)
                findNavController().navigate(directions)
            }

            if (registrationState is RegisterViewModel.RegistrationState.InvalidProfileData) {
                registrationState.fields.forEach {fieldError ->
                    validationFields[fieldError.first]?.error = getString(fieldError.second)
                }
            }
        })
    }

    private fun registerDeviceBackStackCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelRegistration()
        return true
    }

    private fun cancelRegistration() {
        registrationViewModel.userCancelledRegistration()
        navController.popBackStack(R.id.loginFragment, false)
    }

}
