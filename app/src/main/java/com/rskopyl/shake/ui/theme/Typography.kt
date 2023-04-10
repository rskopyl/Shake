package com.rskopyl.shake.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rskopyl.shake.R

private val IbmPlexSerif = FontFamily(
    Font(R.font.ibm_plex_serif_medium, FontWeight.Medium),
    Font(R.font.ibm_plex_serif_semibold, FontWeight.SemiBold),
    Font(R.font.ibm_plex_serif_bold, FontWeight.Bold)
)
private val IbmPlexMono = FontFamily(
    Font(R.font.ibm_plex_mono_light, FontWeight.Light),
    Font(R.font.ibm_plex_mono_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_mono_medium, FontWeight.Medium)
)

val ShakeTypography = Typography(
    titleLarge = TextStyle(
        fontSize = 24.sp,
        fontFamily = IbmPlexSerif,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = IbmPlexSerif,
        fontWeight = FontWeight.SemiBold
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = IbmPlexMono,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = IbmPlexMono,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = IbmPlexMono,
        fontWeight = FontWeight.Light
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = IbmPlexMono,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = TextStyle(
        fontSize = 13.sp,
        fontFamily = IbmPlexSerif,
        fontWeight = FontWeight.Medium
    )
)