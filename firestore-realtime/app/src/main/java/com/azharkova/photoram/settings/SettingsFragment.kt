package com.azharkova.photoram.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.azharkova.photoram.AuthActivity
import com.azharkova.photoram.BaseFragment
import com.azharkova.photoram.R
import com.azharkova.photoram.databinding.AuthFragmentBinding
import com.azharkova.photoram.databinding.SettingsFragmentBinding
import kotlinx.coroutines.flow.collect

class SettingsFragment : BaseFragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var binding: SettingsFragmentBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        binding.also {
            it.btnLogout.setOnClickListener {
                viewModel.logout()
                openOrReturn()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInScope {
            viewModel.userData.collect {
                binding.tvEmail.text = it?.email
                binding.tvName.text = it?.name
            }
        }
    }

    fun openOrReturn() {
        val intent = Intent(this.activity,AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.activity?.finish()
    }
}