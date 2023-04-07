package com.rifqi.testpaging3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.rifqi.testpaging3.databinding.FragmentSplash2Binding
import com.rifqi.testpaging3.login.dataStore


class SplashFragment : Fragment() {

    private var _binding: FragmentSplash2Binding? = null
    private val binding get() = _binding
    private val authViewModel: AuthViewModel by viewModels() {
        ViewModelFactory(UserPrefferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplash2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getUser().observe(viewLifecycleOwner) {
            if (it.userLogin) {
                Thread(Runnable {
                    try {
                        Thread.sleep(1500)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    activity?.runOnUiThread {

                        findNavController().navigate(R.id.action_splashFragment_to_menuActivity)


                        activity?.finish()

                    }
                }).start()
            } else {
                try {
                    Thread.sleep(1500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                activity?.runOnUiThread {
                    view.findNavController()
                        .navigate(R.id.action_splashFragment_to_loginFragment)


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