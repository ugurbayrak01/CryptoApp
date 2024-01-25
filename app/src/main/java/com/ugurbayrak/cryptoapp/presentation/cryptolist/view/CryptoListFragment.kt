package com.ugurbayrak.cryptoapp.presentation.cryptolist.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugurbayrak.cryptoapp.databinding.FragmentCryptoListBinding
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
    private var isFocused: Boolean = false
    private var isEmpty: Boolean = true

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

        _binding?.let {
            binding.cryptoListRecyclerview.adapter = cryptoRecyclerAdapter
            binding.cryptoListRecyclerview.layoutManager = LinearLayoutManager(requireContext())

            binding.swipeRefreshLayout.setOnRefreshListener {
                binding.cryptoListRecyclerview.visibility = View.GONE
                binding.cryptoListProgressBar.visibility = View.VISIBLE
                binding.swipeRefreshLayout.isRefreshing = false
                cancelSearch()
                viewModel.getCryptosFromAPI()
            }
            searchCrypto()
        }
        collectFlow()
    }

    private fun collectFlow() {
        viewModel.state.onEach {
            if(it.isLoading) {
                _binding?.let {
                    binding.cryptoListRecyclerview.visibility = View.GONE
                    binding.cryptoListProgressBar.visibility = View.VISIBLE
                }
            } else if(it.error.isNotEmpty()) {
                Toast.makeText(requireContext(),it.error,Toast.LENGTH_LONG).show()

                _binding?.let {
                    binding.cryptoListRecyclerview.visibility = View.VISIBLE
                    binding.cryptoListProgressBar.visibility = View.GONE
                }
            } else {
                    _binding?.let{ binding ->
                        cryptoRecyclerAdapter.cryptos = it.cryptos
                        binding.cryptoListRecyclerview.visibility = View.VISIBLE
                        binding.cryptoListProgressBar.visibility = View.GONE
                    }
            }

        }.launchIn(lifecycleScope)
    }

    private fun searchCrypto() {
        binding.cryptoListSearch.apply {
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    return@setOnEditorActionListener true
                }
                false
            }
        }

        binding.cryptoListSearch.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                it?.let {
                    isEmpty = it.isEmpty()
                    viewModel.onEvent(CryptoListEvent.Search(it.toString()))

                    if(!isEmpty && isFocused) {
                        binding.cryptoListClear.visibility = View.VISIBLE
                        binding.cryptoListCancel.visibility = View.VISIBLE
                    } else if(isEmpty && isFocused) {
                        binding.cryptoListClear.visibility = View.INVISIBLE
                        binding.cryptoListCancel.visibility = View.VISIBLE
                    } else if(!isEmpty) {
                        binding.cryptoListClear.visibility = View.INVISIBLE
                        binding.cryptoListCancel.visibility = View.VISIBLE
                    } else {
                        binding.cryptoListClear.visibility = View.INVISIBLE
                        binding.cryptoListCancel.visibility = View.GONE
                    }
                }
            }
        }

        binding.cryptoListSearch.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.cryptoListCancel.visibility = View.VISIBLE
                isFocused = true

                if(!isEmpty) {
                    binding.cryptoListClear.visibility = View.VISIBLE
                }
            } else {
                binding.cryptoListCancel.visibility = View.GONE
                binding.cryptoListClear.visibility = View.INVISIBLE
                isFocused = false
            }
        }

        binding.cryptoListClear.setOnClickListener {
            binding.cryptoListSearch.text.clear()
        }

        binding.cryptoListCancel.setOnClickListener {
            cancelSearch()
        }
    }

    private fun cancelSearch() {
        binding.cryptoListSearch.text.clear()
        binding.cryptoListSearch.clearFocus()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            binding.cryptoListSearch.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}