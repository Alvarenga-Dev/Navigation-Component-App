package com.alvarengadev.navigationcomponentapp.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alvarengadev.navigationcomponentapp.R
import com.alvarengadev.navigationcomponentapp.extensions.navigateWithAnimation
import com.alvarengadev.navigationcomponentapp.view.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.authenticationStateEvent.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState is LoginViewModel.AuthenticationState.Authenticated) {
                tv_welcome_profile.text = getString(R.string.profile_text_username, loginViewModel.username)
            }

            if (authenticationState is LoginViewModel.AuthenticationState.NotAuthenticated) {
                findNavController().navigateWithAnimation(R.id.loginFragment)
            }
        })
    }
}
