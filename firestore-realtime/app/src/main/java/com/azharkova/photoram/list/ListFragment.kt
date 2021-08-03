package com.azharkova.photoram.list

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azharkova.photoram.BaseFragment
import com.azharkova.photoram.PostItemActivity
import com.azharkova.photoram.R
import com.azharkova.photoram.SettingsActivity
import com.azharkova.photoram.adapter.PostsAdapter
import com.azharkova.photoram.databinding.ListFragmentBinding
import com.azharkova.photoram.item.CreatePostActivity
import kotlinx.coroutines.flow.collect

class ListFragment : BaseFragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private val viewModel: ListDbViewModel by viewModels()
    private lateinit var binding: ListFragmentBinding

    private var adapter = PostsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.posts_menu)
        binding.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        adapter.onLikeClick = {
            viewModel.changeLike(it)
        }
        adapter.onItemClick = {
            openPost(viewModel.selectPost(it))
        }
        binding.rvPosts.adapter = adapter
        binding.rvPosts.layoutManager = LinearLayoutManager(this.activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInScope {
            viewModel.posts.collect {
                adapter.setupItems(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startListenPosts()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopListen()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.posts_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this.activity,SettingsActivity::class.java))
            }
            R.id.action_add -> {
                startActivity(Intent(this.activity,CreatePostActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun openPost(id: String) {
        val intent = Intent(this.activity,PostItemActivity::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
    }
}