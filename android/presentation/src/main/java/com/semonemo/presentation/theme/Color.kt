package com.semonemo.presentation.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Red = Color(0xFFFF0000)
val Blue1 = Color(0xFF8398A2)
val Blue2 = Color(0xFF526788)
val Blue3 = Color(0xFF267DBC)
val Blue4 = Color(0xFF267D7F)
val GunMetal = Color(0xFF252A34)
val White = Color(0xFFFFFFFF)
val Gray01 = Color(0xFF555555)
val Gray02 = Color(0xFF949494)
val Gray03 = Color(0xFFD9D9D9)
val WhiteGray = Color(0xFFE7E7E7)

val ProgressRed = Color(0xFFFF6961)
val ProgressYellow = Color(0xFFFDFD96)
val ProgressGreen = Color(0xFF77DD77)

val FramePink = Color(0xFFFFCCCD)
val FrameOrange = Color(0xFFFFD085)
val FrameYellow = Color(0xFFFFFF7E)
val FrameGreen = Color(0xFF99CC99)
val FrameBlue = Color(0xFFADC4FF)
val FramePurple = Color(0xFFFFB3FF)

val Orange = Color(0xFFFFA500)

val Main01 =
    Brush.linearGradient(
        colors =
            listOf(
                White,
                Color(0xFFEBF0FF),
            ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f),
    )
val Main02 =
    Brush.horizontalGradient(
        colors =
            listOf(
                Color(0xFF535353),
                Color(0xFF273B71),
            ),
        startX = 0f,
        endX = Float.POSITIVE_INFINITY,
    )

val frameBackGroundColor =
    listOf(
        Color.Black,
        FramePink,
        FrameOrange,
        FrameBlue,
        FramePurple,
        Gray01,
    )