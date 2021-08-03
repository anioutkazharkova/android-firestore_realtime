package com.azharkova.photoram.ui.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azharkova.photoram.BaseFragment
import com.azharkova.photoram.R
import com.azharkova.photoram.adapter.CommentsAdapter
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.databinding.PostItemFragmentBinding
import com.azharkova.photoram.databinding.SettingsFragmentBinding
import com.azharkova.photoram.item.CreatePostActivity
import com.azharkova.photoram.util.Dialog
import com.azharkova.photoram.util.loadImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

class PostItemFragment : BaseFragment() {

    companion object {
        fun newInstance(id: String): PostItemFragment {
            val bundle = Bundle()
            bundle.putString("id",id)
            val f = PostItemFragment()
            f.arguments = bundle
            return f
        }
    }

    private val viewModel: PostItemDbViewModel by viewModels()
    private lateinit var binding: PostItemFragmentBinding

    private val adapter = CommentsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.getString("id")?.let {
            viewModel.id = it
        }
        binding = PostItemFragmentBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.post_item_menu)
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_edit) {
                openEdit(viewModel.id)
            } else {
                requestDelete()
            }
            true
        }
       binding.toolbar.setNavigationOnClickListener {
           activity?.onBackPressed()
       }
        binding.rvComment.adapter = adapter
        binding.rvComment.layoutManager = LinearLayoutManager(this.activity)
        return binding.root
    }

    fun openEdit(id: String) {
        val intent = Intent(requireActivity(), CreatePostActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    fun requestDelete() {
        viewModel.deletePost()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.commentTextEdit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.sendComment(v.text.toString())
                hide_keyboard(requireContext())
                v.text = ""
                v.clearFocus()
            }
            true
        }
        doInScope {
            viewModel.post.collect {
                if (it?.imageLink.isNullOrEmpty()) {
                    binding.image.visibility = View.GONE
                } else {
                    binding.image.visibility = View.VISIBLE
                  it?.imageLink?.let {
                      binding.image.loadImage(it)
                  }
                }
                binding.toolbar.title = it?.date
                binding.tvDate.text = it?.date
                binding.tvText.text = it?.postText
                binding.tvUserName.text = it?.userName
            }
        }
        doInScope {
            viewModel.comments.collect {
                adapter.setupItems(it)
            }
        }
        doInScope {
            viewModel.isDeleted.collect {
                if (it) {
                    activity?.finish()
                }
            }
        }

        doInScope {
            viewModel.errorb.collect {
                if (it.isNotEmpty()) {
                    Dialog.instance.show(it, requireActivity())
                }
            }
        }

    }

    private fun hide_keyboard(context: Context) {
        val inputManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(0, 0)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPost()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopListenComments()
    }

}