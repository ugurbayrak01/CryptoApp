package com.ugurbayrak.cryptoapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ugurbayrak.cryptoapp.R
import com.ugurbayrak.cryptoapp.databinding.CryptoListRowBinding
import com.ugurbayrak.cryptoapp.domain.model.Crypto

class CryptoRecyclerAdapter() : RecyclerView.Adapter<CryptoRecyclerAdapter.CryptoViewHolder>() {

    class CryptoViewHolder(var binding: CryptoListRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Crypto>() {
        override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
            return oldItem == newItem
        }

    }
    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var cryptos: List<Crypto>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = DataBindingUtil.inflate<CryptoListRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.crypto_list_row,
            parent,
            false
        )
        return CryptoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cryptos.size
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.binding.crypto = cryptos[position]
    }
}