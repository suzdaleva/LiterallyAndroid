package com.silentspring.uikit.components

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.silentspring.uikit.R

val urbanistFont = FontFamily(
    listOf(
        Font(R.font.urbanist_medium, FontWeight.Medium),
        Font(R.font.urbanist_regular, FontWeight.Normal),
        Font(R.font.urbanist_light, FontWeight.Light)
    )
)
val redditSansFont = FontFamily(
    listOf(
        Font(R.font.reddit_sans_medium, FontWeight.Medium)
    )
)

val encodeSansSemiExtendedFont = FontFamily(
    listOf(
        Font(R.font.encode_sans_semi_expanded_regular, FontWeight.Normal)
    )
)

val redHatTextMediumFont = FontFamily(
    listOf(
        Font(R.font.red_hat_text_regular, FontWeight.Normal),
        Font(R.font.red_hat_text_medium, FontWeight.Medium)
    )
)

val LiterallyTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = redditSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 36.sp,
        color = primaryWhite
    ),
    titleLarge = TextStyle(
        fontFamily = encodeSansSemiExtendedFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        color = primaryWhite
    ),
    titleMedium = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = primaryWhite
    ),
    titleSmall = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Light,
        fontSize = 13.sp,
        color = primaryWhite
    ),
    bodyLarge = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = primaryWhite
    ),
    bodyMedium = TextStyle(
        fontFamily = redHatTextMediumFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        color = primaryWhite
    ),
    bodySmall = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = primaryWhite
    ),
    labelSmall = TextStyle(
        fontFamily = redHatTextMediumFont,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        color = primaryWhite
    )
)