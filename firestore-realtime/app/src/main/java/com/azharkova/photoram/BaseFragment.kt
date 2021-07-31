package com.azharkova.photoram

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

open class BaseFragment : Fragment() {
    protected fun doInScope(
        state: Lifecycle.State = Lifecycle.State.STARTED,
        action: suspend () -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(state) {
                action()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}