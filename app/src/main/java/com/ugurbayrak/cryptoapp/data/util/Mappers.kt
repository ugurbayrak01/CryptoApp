package com.ugurbayrak.cryptoapp.data.util

import android.graphics.Color
import com.ugurbayrak.cryptoapp.data.remote.dto.CryptoResponse
import com.ugurbayrak.cryptoapp.domain.model.Crypto
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun CryptoResponse.toCrypto() : List<Crypto> {
    return data.map {
        Crypto(
            circulatingSupply = formatNumber(it.circulatingSupply, " ${it.symbol}"),
            maxSupply = formatNumber(it.maxSupply, " ${it.symbol}"),
            name = it.name,
            symbol = it.symbol,
            totalSupply = formatNumber(it.totalSupply, " ${it.symbol}"),
            marketCap = formatNumber(it.quote.USD.marketCap, "$", false),
            percentChange1h = formatNumber(it.quote.USD.percentChange1h, "%"),
            percentChange1hColor = setColor(it.quote.USD.percentChange1h),
            percentChange24h = formatNumber(it.quote.USD.percentChange24h, "%"),
            percentChange24hColor = setColor(it.quote.USD.percentChange24h),
            price = formatNumber(it.quote.USD.price, "$", false),
            volume24h = formatNumber(it.quote.USD.volume24h, "$", false),
            percentVolumeMarketCap = formatNumber(it.quote.USD.volume24h / it.quote.USD.marketCap * 100, "%"),
            logoUrl = getLogoUrl(it.id),
            detailLogoUrl = getDetailLogoUrl(it.id)
        )
    }
}

private fun getLogoUrl(id: Int) = "https://s2.coinmarketcap.com/static/img/coins/32x32/${id}.png"

private fun getDetailLogoUrl(id: Int) = "https://s2.coinmarketcap.com/static/img/coins/128x128/${id}.png"

private fun formatNumber(number: Number?, symbol: String?, appendToEnd: Boolean = true): String {
    if (number == null || number == 0.0) {
        return "--"
    }

    val formatter: NumberFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
    val formattedNumber = formatter.format(number)

    return if (symbol.isNullOrEmpty()) {
        formattedNumber
    } else {
        if (appendToEnd) {
            "$formattedNumber$symbol"
        } else {
            "$symbol$formattedNumber"
        }
    }
}

private fun setColor(number: Double) : Int {
    return if(number < 0) Color.parseColor("#C02020") else Color.parseColor("#20C020")
}
