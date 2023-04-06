package com.rifqi.testpaging3.login


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController

import com.rifqi.testpaging3.UserPrefferences
import com.rifqi.testpaging3.ViewModelFactory

import com.rifqi.testpaging3.R
import com.rifqi.testpaging3.databinding.FragmentLoginBinding

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(UserPrefferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.apply {


            edtPassLogin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setUpCustomView()
                }

                override fun afterTextChanged(s: Editable?) {
                    setUpCustomView()
                }
            })
            edtUsernameLogin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    setUpCustomView()
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setUpCustomView()
                }

                override fun afterTextChanged(s: Editable?) {
                    setUpCustomView()

                }
            })
            btnLogin.setOnClickListener { inputData() }

            tvTosignup.setOnClickListener { toSignUp() }
        }
        setMessage()
        showLoading()


    }

    private fun setUpCustomView() {
        binding?.apply {
            val pass = edtPassLogin.text.toString()
            val email = edtUsernameLogin.text.toString().trim()
            Log.d("edtPass", email)

            btnLogin.isEnabled =
                pass != null && email != null && pass.length >= 6 && Patterns.EMAIL_ADDRESS.matcher(
                    email
                ).matches()


        }
    }

    private fun inputData() {

        binding?.apply {
            val pass = edtPassLogin.text.toString()
            val email = edtUsernameLogin.text.toString()
            loginViewModel.login(email, pass)
        }
    }

    private fun showLoading() {
        binding?.apply {
            loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    pbLogin.visibility = View.VISIBLE
                } else {
                    pbLogin.visibility = View.INVISIBLE
                }

            }
        }
    }


    private fun toSignUp() {
        view?.findNavController()?.navigate(R.id.action_loginFragment_to_regisFragment)
    }

    private fun setMessage() {

        loginViewModel.isError.observe(viewLifecycleOwner) { error ->
            loginViewModel.showMessage.observe(viewLifecycleOwner) { message ->
                if (error == "false") {
                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_menuActivity)
                    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                } else {
                    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        activity?.finish()
    }
}