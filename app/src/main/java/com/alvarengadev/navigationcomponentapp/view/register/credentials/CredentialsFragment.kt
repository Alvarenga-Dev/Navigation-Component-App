package com.alvarengadev.navigationcomponentapp.view.register.credentials

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
import androidx.navigation.fragment.navArgs
import com.alvarengadev.navigationcomponentapp.R
import com.alvarengadev.navigationcomponentapp.extensions.dismissError
import com.alvarengadev.navigationcomponentapp.view.login.LoginViewModel
import com.alvarengadev.navigationcomponentapp.view.register.RegisterViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_credendials.*

class CredentialsFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val registerViewModel: RegisterViewModel by activityViewModels()

    private val args: CredentialsFragmentArgs by navArgs()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_credendials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        tv_choose_credentials_name.text = getString(R.string.choose_credentials_text_name, args.name)

        val invalidFields = initValidationFields()
        listenToRegistrationStateEvent(invalidFields)
        registerViewListeners()
        registerDeviceBackStack()
    }

    private fun initValidationFields() = mapOf(
        RegisterViewModel.INPUT_USERNAME.first to input_layout_create_username,
        RegisterViewModel.INPUT_PASSWORD.first to input_layout_create_password
    )

    private fun listenToRegistrationStateEvent(validationFields: Map<String, TextInputLayout>) {
        registerViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            when (registrationState) {
                is RegisterViewModel.RegistrationState.RegistrationCompleted -> {
                    val token = registerViewModel.authToken
                    val username = input_layout_create_username.editText!!.text.toString()

                    loginViewModel.authenticateToken(token, username)
                    navController.popBackStack(R.id.profileFragment, false)
                }
                is RegisterViewModel.RegistrationState.InvalidCredentials -> {
                    registrationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }

    private fun registerViewListeners() {
        btn_choose_credentials_next.setOnClickListener {
            val username = input_layout_create_username.editText!!.text.toString()
            val password = input_layout_create_password.editText!!.text.toString()

            registerViewModel.createCredentials(username, password)
        }

        input_layout_create_username.editText!!.addTextChangedListener {
            input_layout_create_username.dismissError()
        }

        input_layout_create_username.editText!!.addTextChangedListener {
            input_layout_create_password.dismissError()
        }
    }

    private fun registerDeviceBackStack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelRegistration()
        return super.onOptionsItemSelected(item)
    }

    private fun cancelRegistration() {
        registerViewModel.userCancelledRegistration()
        navController.popBackStack(R.id.loginFragment, false)
    }
}
