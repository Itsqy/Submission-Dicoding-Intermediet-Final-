package com.rifqi.testpaging3.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.rifqi.testpaging3.R

import com.rifqi.testpaging3.databinding.FragmentRegisBinding


class RegisFragment : Fragment() {
    private var _binding: FragmentRegisBinding? = null
    private val binding get() = _binding

    private val regisViewModel: RegisViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCustomView()
        binding?.apply {
            btnRegis.setOnClickListener { initRegis() }
            tvTosignin.setOnClickListener {
                view.findNavController().navigate(R.id.action_regisFragment_to_loginFragment)
            }

        }
        showLoading()


        regisViewModel.isError.observe(viewLifecycleOwner) { error ->
            regisViewModel.showMessage.observe(viewLifecycleOwner) { message ->
                if (error == "false") {
                    view.findNavController().navigate(R.id.action_regisFragment_to_loginFragment)
                    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
                    _binding = null
                } else {
                    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startCustomView() {
        binding?.apply {
            edtEmailRegis.addTextChangedListener(object : TextWatcher {
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
            edtPassRegis.addTextChangedListener(object : TextWatcher {
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
        }
    }

    private fun setUpCustomView() {
        binding?.apply {
            val pass = edtPassRegis.text.toString()
            val email = edtEmailRegis.text.toString()
            btnRegis.isEnabled =
                pass != null && email != null && pass.length >= 8 && Patterns.EMAIL_ADDRESS.matcher(
                    email
                ).matches()


        }
    }

    private fun showLoading() {
        binding?.apply {
            regisViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    pbRegis.visibility = View.VISIBLE
                } else {
                    pbRegis.visibility = View.INVISIBLE
                }

            }
        }
    }

    private fun initRegis() {
        binding?.apply {
            val name = edtNameRegis.text.toString()
            val email = edtEmailRegis.text.toString()
            val pass = edtPassRegis.text.toString()
            Log.d("edtPassRegis", email)


            regisViewModel.register(name, email, pass)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}