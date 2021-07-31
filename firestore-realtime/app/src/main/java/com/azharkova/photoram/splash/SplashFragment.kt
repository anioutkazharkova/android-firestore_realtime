package com.azharkova.photoram.splash

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.azharkova.photoram.AuthActivity
import com.azharkova.photoram.BaseFragment
import com.azharkova.photoram.R
import com.azharkova.photoram.list.ListActivity
import kotlinx.coroutines.flow.collect

class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInScope {
            viewModel.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    openPosts()
                } else {
                    openLogin()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkAuth()
    }

    fun openLogin() {
        startActivity(Intent(this.activity,AuthActivity::class.java))
    }

    fun openPosts() {
        startActivity(Intent(this.activity, ListActivity::class.java))
    }
}