package com.alvarengadev.navigationcomponentapp.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alvarengadev.navigationcomponentapp.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider.NewInstanceFactory().create(LoginViewModel::class.java)

        viewModel.authenticationStateEvent.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState is LoginViewModel.AuthenticationState.InvalidAuthentication) {
                val validationStrings: Map<String, TextInputLayout> = initValidationFields()
                authenticationState.fields.forEach {fieldError ->
                    validationStrings[fieldError.first]?.error = getString(fieldError.second)
                }
            }
        })

        btn_sign_in.setOnClickListener {
            val username = input_layout_username.editText!!.text.toString()
            val password = input_layout_password.editText!!.text.toString()

            viewModel.authentication(username, password)
        }
    }

    private fun initValidationFields() = mapOf(
        LoginViewModel.INPUT_USERNAME.first to input_layout_username,
        LoginViewModel.INPUT_PASSWORD.first to input_layout_password
    )
}
