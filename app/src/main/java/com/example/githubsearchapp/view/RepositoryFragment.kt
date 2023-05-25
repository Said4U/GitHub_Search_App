package com.example.githubsearchapp.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubsearchapp.R
import com.example.githubsearchapp.data.Content
import com.example.githubsearchapp.data.Entry
import com.example.githubsearchapp.databinding.FragmentItemListBinding
import com.example.githubsearchapp.databinding.FragmentRepositoryBinding
import com.example.githubsearchapp.view.adapter.ItemAdapter
import com.example.githubsearchapp.view.adapter.RepositoryAdapter
import com.example.githubsearchapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.util.*

class RepositoryFragment : Fragment(R.layout.fragment_repository), RepositoryAdapter.ItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentRepositoryBinding
    private val stackQueue: Stack<String> = Stack()
    private var currentPath = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    if (stackQueue.isNotEmpty()) {
                        currentPath = stackQueue.pop()

                        lifecycleScope.launch {
                            viewModel.getContent(
                                owner = arguments?.getString("owner").toString(),
                                repo = arguments?.getString("repo").toString(),
                                path = currentPath
                            )
                        }
                    } else parentFragmentManager.popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoryBinding.inflate(inflater, container, false)

        binding.repoRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.apply {
            lifecycleScope.launch {
                getContent(
                    owner = arguments?.getString("owner").toString(),
                    repo = arguments?.getString("repo").toString(),
                    path = currentPath
                )
            }


            content.observe(viewLifecycleOwner) { content ->
                binding.repoRecyclerView.adapter = RepositoryAdapter(content, this@RepositoryFragment,
                    requireContext()
                )
            }

            binding.nameRepo.text = arguments?.getString("repo").toString()

            return binding.root
        }
    }

    private fun setCurrentFragment(url: String) {
        val bundle = Bundle()
        bundle.putString("url", url)
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.placeholder, WebFragment.newInstance(bundle))
            addToBackStack("commit")
            commit()
        }
    }

    companion object {
        fun newInstance(bundle: Bundle) : RepositoryFragment{

            val repositoryFragment = RepositoryFragment()
            repositoryFragment.arguments = bundle
            return repositoryFragment
        }
    }

    override fun onItemClick(path: String, type: String, url: String) {
        if (type == "dir"){
            stackQueue.push(currentPath)
            currentPath = path

            lifecycleScope.launch {
                viewModel.getContent(
                    owner = arguments?.getString("owner").toString(),
                    repo = arguments?.getString("repo").toString(),
                    path = path
                )
            }
        } else {
            setCurrentFragment(url)
        }
    }

}