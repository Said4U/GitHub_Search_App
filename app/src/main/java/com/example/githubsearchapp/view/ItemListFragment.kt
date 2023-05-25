package com.example.githubsearchapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubsearchapp.R
import com.example.githubsearchapp.data.Item
import com.example.githubsearchapp.databinding.FragmentItemListBinding
import com.example.githubsearchapp.view.adapter.ItemAdapter
import com.example.githubsearchapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class ItemListFragment : Fragment(R.layout.fragment_item_list), ItemAdapter.ItemClickListener {

    private var itemList = mutableListOf<Item>()
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentItemListBinding
    private var queryText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemListBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initObservers()

        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                initObservers()
                queryText = query.toString()
                if (queryText.length < 3) {
                    Toast.makeText(requireContext(), "Необходимо ввести 3 и более символов", Toast.LENGTH_SHORT).show()
                } else {
                    requestData(name = queryText)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.tryBtn.setOnClickListener {
            requestData(queryText)
        }

        if (itemList.isNotEmpty()) binding.recyclerView.adapter = ItemAdapter(itemList, this@ItemListFragment)

        return binding.root
    }

    fun initObservers() {

        viewModel.apply {
            users.observe(viewLifecycleOwner) { users ->

                if (users.items.isEmpty()){
                    binding.errorText.visibility = View.VISIBLE
                    binding.tryBtn.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                } else {
                    itemList.addAll(users.items)
                    itemList.shuffle()
                    binding.recyclerView.adapter = ItemAdapter(itemList, this@ItemListFragment)
                }

            }

            repo.observe(viewLifecycleOwner) { repo ->

                if (repo.items.isEmpty()){
                    binding.errorText.visibility = View.VISIBLE
                    binding.tryBtn.visibility = View.VISIBLE
                } else {
                    itemList.addAll(repo.items)
                    itemList.shuffle()
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.adapter = ItemAdapter(itemList, this@ItemListFragment)
                }
                binding.progressBar.visibility = View.GONE
                binding.searchView.inputType = InputType.TYPE_CLASS_TEXT


            }
        }
    }


    fun requestData(name: String) {
        binding.errorText.visibility = View.GONE
        binding.tryBtn.visibility = View.GONE

        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.searchView.clearFocus()
        binding.searchView.inputType = InputType.TYPE_NULL

        itemList.clear()

        lifecycleScope.launch {
            viewModel.getUsers(name)
            viewModel.getRepo(name)
        }

    }

    private fun setCurrentFragment(owner: String, repo: String) {
        val bundle = Bundle()
        bundle.putString("owner", owner)
        bundle.putString("repo", repo)
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.placeholder, RepositoryFragment.newInstance(bundle))
            addToBackStack("go")
            commit()
        }
    }

    override fun onItemClick(id: Int?, url: String?, isUser: Boolean, owner: String, repo: String) {
        viewModel.users.removeObservers(viewLifecycleOwner)
        viewModel.repo.removeObservers(viewLifecycleOwner)
        if (isUser) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } else {
            setCurrentFragment(owner, repo)
        }
    }

}