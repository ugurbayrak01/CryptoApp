package com.ugurbayrak.cryptoapp.presentation.cryptodetail.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ugurbayrak.cryptoapp.databinding.FragmentCryptoDetailBinding
import com.ugurbayrak.cryptoapp.presentation.cryptodetail.CryptoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CryptoDetailFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentCryptoDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CryptoDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCryptoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CryptoDetailViewModel::class.java]

        arguments?.let {
            val id = CryptoDetailFragmentArgs.fromBundle(it).id
            viewModel.getCryptoFromSQLite(id)
            collectFlow()
        }
    }

    private fun collectFlow() {
        viewModel.state.onEach {
            if(it.error.isNotEmpty()) {
                Toast.makeText(requireContext(),it.error,Toast.LENGTH_LONG).show()
            } else {
                it.crypto?.let {crypto ->
                    _binding?.let {
                        binding.crypto = crypto
                    }
                    (activity as AppCompatActivity).supportActionBar?.title = crypto.name
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}