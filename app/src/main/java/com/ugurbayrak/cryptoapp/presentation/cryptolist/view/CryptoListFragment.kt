package com.ugurbayrak.cryptoapp.presentation.cryptolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugurbayrak.cryptoapp.databinding.FragmentCryptoListBinding
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import com.ugurbayrak.cryptoapp.presentation.adapter.CryptoRecyclerAdapter
import com.ugurbayrak.cryptoapp.presentation.cryptolist.CryptoListEvent
import com.ugurbayrak.cryptoapp.presentation.cryptolist.CryptoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CryptoListFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentCryptoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CryptoListViewModel
    private var cryptoRecyclerAdapter = CryptoRecyclerAdapter()
    private var job: Job? = null

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
        viewModel.refreshCryptos()

        binding.cryptoListRecyclerview.adapter = cryptoRecyclerAdapter
        binding.cryptoListRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.cryptoListSearch.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                it?.let {
                    viewModel.onEvent(CryptoListEvent.Search(it.toString()))
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.cryptoListRecyclerview.visibility = View.GONE
            binding.cryptoListError.visibility = View.GONE
            binding.cryptoListProgressBar.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.getCryptosFromAPI()
        }

        collectFlow()
    }

    override fun onResume() {
        super.onResume()
        if(binding.cryptoListSearch.text.isNotEmpty()) {
            viewModel.onEvent(CryptoListEvent.Search(binding.cryptoListSearch.text.toString()))
        }
    }

    private fun collectFlow() {
        viewModel.state.onEach {
            if(it.isLoading) {
                binding.cryptoListRecyclerview.visibility = View.GONE
                binding.cryptoListProgressBar.visibility = View.VISIBLE
            } else if(it.error.isNotEmpty()) {
                Toast.makeText(requireContext(),it.error,Toast.LENGTH_LONG).show()
                binding.cryptoListRecyclerview.visibility = View.VISIBLE
                binding.cryptoListProgressBar.visibility = View.GONE
            } else {
                    cryptoRecyclerAdapter.cryptos = it.cryptos
                    binding.cryptoListRecyclerview.visibility = View.VISIBLE
                    binding.cryptoListProgressBar.visibility = View.GONE
            }

        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}