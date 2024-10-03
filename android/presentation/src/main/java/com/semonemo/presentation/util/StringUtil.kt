package com.semonemo.presentation.util

import android.net.Uri
import androidx.core.net.toUri
import com.semonemo.presentation.BuildConfig
import java.math.BigInteger

fun String.toUriOrDefault(): Uri = Uri.decode(this).let { Uri.parse(it) } ?: "".toUri()

fun String.urlToIpfs() = BuildConfig.IPFS_READ_URL + "ipfs/" + this

fun BigInteger.toPrice() = this * BigInteger.TEN.pow(18)