package com.ugurbayrak.cryptoapp.presentation.cryptolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugurbayrak.cryptoapp.databinding.FragmentCryptoListBinding
import com.ugurbayrak.cryptoapp.presentation.adapter.CryptoRecyclerAdapter
import com.ugurbayrak.cryptoapp.presentation.cryptolist.CryptoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CryptoListFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentCryptoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CryptoListViewModel
    private var cryptoRecyclerAdapter = CryptoRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCryptoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CryptoListViewModel::class.java]
        viewModel.getCryptos()

        binding.cryptoListRecyclerview.adapter = cryptoRecyclerAdapter
        binding.cryptoListRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        collectFlow()
    }

    private fun collectFlow() {
        viewModel.state.onEach {
            if(it.isLoading) {
                binding.cryptoListRecyclerview.visibility = View.GONE
                binding.cryptoListProgressBar.visibility = View.VISIBLE
                binding.cryptoListError.visibility = View.GONE
            } else if(it.error.isNotEmpty()) {
                binding.cryptoListRecyclerview.visibility = View.GONE
                binding.cryptoListProgressBar.visibility = View.GONE
                binding.cryptoListError.visibility = View.VISIBLE
                binding.cryptoListError.text = it.error
            } else {
                cryptoRecyclerAdapter.cryptos = it.cryptos
                binding.cryptoListRecyclerview.visibility = View.VISIBLE
                binding.cryptoListProgressBar.visibility = View.GONE
                binding.cryptoListError.visibility = View.GONE
            }

        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}