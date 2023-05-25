package com.example.githubsearchapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import com.example.githubsearchapp.R
import com.example.githubsearchapp.databinding.FragmentItemListBinding
import com.example.githubsearchapp.databinding.FragmentWebBinding
import com.example.githubsearchapp.viewmodel.MainViewModel


class WebFragment : Fragment(R.layout.fragment_web) {

    private lateinit var binding: FragmentWebBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater, container, false)

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        binding.webView.loadUrl(arguments?.getString("url").toString())

        return binding.root
    }

    companion object {
        fun newInstance(bundle: Bundle) : WebFragment{
            val webFragment = WebFragment()
            webFragment.arguments = bundle
            return webFragment
        }
    }

}