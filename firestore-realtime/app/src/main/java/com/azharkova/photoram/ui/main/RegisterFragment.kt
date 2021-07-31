package com.azharkova.photoram.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.azharkova.photoram.BaseFragment
import com.azharkova.photoram.FirebaseAuthHelper
import com.azharkova.photoram.R
import com.azharkova.photoram.databinding.RegisterFragmentBinding
import com.azharkova.photoram.list.ListActivity
import kotlinx.coroutines.flow.collect

class RegisterFragment : BaseFragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: RegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            register()
        }
        doInScope {
            viewModel.newUser.collect {
                if (it != null) {
                    openPosts()
                }
            }
        }
    }

    fun register() {
        val name = binding.nameTextEdit.text.toString()
        val email = binding.emailTextEdit.text.toString()
        val password = binding.passwordTextEdit.text.toString()

       viewModel.register(name,email,password)
    }

    fun openPosts() {
        startActivity(Intent(this.activity, ListActivity::class.java))
    }
}