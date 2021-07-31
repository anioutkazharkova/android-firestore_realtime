package com.azharkova.photoram.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.azharkova.photoram.R
import androidx.fragment.app.viewModels
import com.azharkova.photoram.BaseFragment
import com.azharkova.photoram.RegisterActivity
import com.azharkova.photoram.databinding.AuthFragmentBinding
import com.azharkova.photoram.list.ListActivity
import kotlinx.coroutines.flow.collect

class AuthFragment : BaseFragment() {

    companion object {
        fun newInstance() = AuthFragment()
    }

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: AuthFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = AuthFragmentBinding.inflate(inflater, container, false)
        binding.also {
            it.btnSignIn.setOnClickListener {
                val email = binding.emailTextEdit.text.toString()
                val password = binding.passwordTextEdit.text.toString()
                viewModel.login(email,password)
            }
            it.btnSignUp.setOnClickListener {
                openRegister()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInScope {
            viewModel.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    openPosts()
                }
            }
        }
    }

    fun openRegister() {
        startActivity(Intent(this.activity,RegisterActivity::class.java))
    }

    fun openPosts() {
        startActivity(Intent(this.activity, ListActivity::class.java))
    }

}