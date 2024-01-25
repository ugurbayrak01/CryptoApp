package com.ugurbayrak.cryptoapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ugurbayrak.cryptoapp.R
import com.ugurbayrak.cryptoapp.databinding.CryptoListRowBinding
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import com.ugurbayrak.cryptoapp.presentation.cryptolist.view.CryptoListFragmentDirections

class CryptoRecyclerAdapter() : RecyclerView.Adapter<CryptoRecyclerAdapter.CryptoViewHolder>(), CryptoClickListener {

    class CryptoViewHolder(var binding: CryptoListRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Crypto>() {
        override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
            return oldItem.id == newItem.id
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
        holder.binding.root.tag = cryptos[position].id
        holder.binding.listener = this
    }

    override fun onCryptoClicked(view: View) {
        val id = view.tag.toString().toInt()
        val action = CryptoListFragmentDirections.actionCryptoListFragmentToCryptoDetailFragment(id)
        Navigation.findNavController(view).navigate(action)
    }
}