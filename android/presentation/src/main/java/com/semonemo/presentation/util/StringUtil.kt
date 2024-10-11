package com.semonemo.presentation.util

import android.net.Uri
import androidx.core.net.toUri
import com.semonemo.presentation.BuildConfig
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toUriOrDefault(): Uri = Uri.decode(this).let { Uri.parse(it) } ?: "".toUri()

fun String.urlToIpfs() = BuildConfig.IPFS_READ_URL + "ipfs/" + this

fun BigInteger.toPrice() = this * BigInteger.TEN.pow(18)

val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

fun getTodayDate() = LocalDate.now().format(dateFormatter)