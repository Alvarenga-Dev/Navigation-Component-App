package com.alvarengadev.navigationcomponentapp.view.login

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
import androidx.navigation.fragment.findNavController
import com.alvarengadev.navigationcomponentapp.R
import com.alvarengadev.navigationcomponentapp.extensions.dismissError
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.authenticationStateEvent.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState is LoginViewModel.AuthenticationState.InvalidAuthentication) {
                val validationStrings: Map<String, TextInputLayout> = initValidationFields()
                authenticationState.fields.forEach {fieldError ->
                    validationStrings[fieldError.first]?.error = getString(fieldError.second)
                }
            } else if (authenticationState is LoginViewModel.AuthenticationState.Authenticated) {
                findNavController().popBackStack()
            }
        })

        btn_sign_in.setOnClickListener {
            val username = input_layout_username.editText!!.text.toString()
            val password = input_layout_password.editText!!.text.toString()

            viewModel.authentication(username, password)
        }

        input_layout_username.editText!!.addTextChangedListener {
            input_layout_username.dismissError()
        }

        input_layout_password.editText!!.addTextChangedListener {
            input_layout_password.dismissError()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelAuthentication()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelAuthentication()
        return true
    }

    private fun cancelAuthentication() {
        viewModel.refuseAuthentication()
        findNavController().popBackStack(R.id.startFragment, false)
    }

    private fun initValidationFields() = mapOf(
        LoginViewModel.INPUT_USERNAME.first to input_layout_username,
        LoginViewModel.INPUT_PASSWORD.first to input_layout_password
    )
}
