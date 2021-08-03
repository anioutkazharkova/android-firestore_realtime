package com.azharkova.photoram.item

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azharkova.photoram.R
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent

import android.content.DialogInterface
import android.database.Cursor
import android.provider.MediaStore
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.azharkova.photoram.BaseFragment
import com.azharkova.photoram.databinding.CreatePostFragmentBinding
import com.azharkova.photoram.list.ListActivity
import com.azharkova.photoram.ui.main.PostItemFragment
import com.azharkova.photoram.util.Dialog
import com.azharkova.photoram.util.getBytes
import com.azharkova.photoram.util.loadImage
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.flow.collect


class CreatePostFragment : BaseFragment() {

    companion object {
        fun newInstance(id: String?) :CreatePostFragment {
            val bundle = Bundle()
            bundle.putString("id",id)
            val f = CreatePostFragment()
            f.arguments = bundle
            return f
        }
    }

    private lateinit var binding: CreatePostFragmentBinding
    private val viewModel: CreatePostDbViewModel by viewModels()

    private lateinit var imagePicker: ImagePicker.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.getString("id")?.let {
            viewModel.id = it
        }
        imagePicker = ImagePicker.with(requireActivity())
        binding = CreatePostFragmentBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.image.setOnClickListener {
            openImagePicker()
        }

        binding.btnPost.setOnClickListener {
            val text = binding.postTextEdit.text.toString()
            if (text.isNotEmpty()) {
                viewModel.publish(text)
            }
        }

        doInScope {
            viewModel.isCreated.collect {
                if (it) {
                    requireActivity().finish()
                }
            }
        }
        doInScope {
            viewModel.isChanged.collect {
                if (it) {
                    val intent = Intent(requireActivity(),ListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    this.activity?.finish()
                }
            }
        }
        doInScope {
            viewModel.post.collect {
                if (it != null) {
                    if (it?.imageLink.isNullOrEmpty()) {
                        binding.image.visibility = View.GONE
                    } else {
                        binding.image.visibility = View.VISIBLE
                        it?.imageLink?.let {
                            binding.image.loadImage(it)
                        }
                    }
                    binding.toolbar.title = it?.date
                    binding.postTextEdit.setText(it?.postText.orEmpty())
                }
            }
        }
        doInScope {
            viewModel.error.collect { if (it.isNotEmpty()) {
                Dialog.instance.show(it,requireActivity())
            }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPost()
    }

    fun openImagePicker() {
        imagePicker.createIntent { intent ->
            startForProfileImageResult.launch(intent)
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                binding.image.setImageURI(fileUri)
                viewModel.imageBytes = binding.image.getBytes()
            }
        }
}