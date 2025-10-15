package com.silentspring.uikit.components

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.annotation.DrawableRes
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.silentspring.uikit.R

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit,
) {

    BasicButton(
        modifier = modifier,
        leadingIcon = leadingIcon,
        text = text,
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(29.dp),
        border = BorderStroke(width = 1.dp, color = primaryWhite),
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = primaryWhite,
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = primaryWhite
        )
    )
}


@Composable
fun GlowButton(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    BasicButton(
        modifier = modifier.drawBehind {

            val paint = Paint().apply {
                color = AndroidColor.WHITE
                style = Paint.Style.FILL
            }
            paint.maskFilter = BlurMaskFilter(7.dp.toPx(), BlurMaskFilter.Blur.NORMAL)

            drawContext.canvas.nativeCanvas.drawRoundRect(
                0f, 0f, size.width, size.height, 30.dp.toPx(), 30.dp.toPx(), paint
            )
        },
        leadingIcon = leadingIcon,
        text = text,
        onClick = onClick,
        shape = RoundedCornerShape(29.dp),
        enabled = enabled,
        border = null,
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = primaryBlack,
            containerColor = primaryWhite,
            disabledContainerColor = primaryGrey,
            disabledContentColor = primaryBlack
        )
    )
}


@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
    border: BorderStroke?,
    shape: Shape = RoundedCornerShape(27.dp),
    enabled: Boolean,
    colors: ButtonColors,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        border = border,
        colors = colors,
        enabled = enabled,
        contentPadding = PaddingValues(15.dp)

    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {

            leadingIcon()

            Text(
                text = text,
                style = LiterallyTypography.titleMedium.copy(color = colors.contentColor)
            )
        }
    }
}


@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    backgroundColor: Color,
    borderColor: Color,
    borderWidth: Dp,
    iconTint: Color
) {
    IconButton(
        modifier = modifier
            .border(width = borderWidth, color = borderColor, shape = CircleShape)
            .clip(CircleShape)
            .size(32.dp)
            .background(backgroundColor),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(iconRes),
            tint = iconTint,
            contentDescription = null
        )
    }
}

@Composable
fun OutlinedRoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int
) {
    RoundButton(
        modifier = modifier,
        onClick = onClick,
        iconRes = iconRes,
        backgroundColor = Color.Transparent,
        borderColor = primaryWhite,
        borderWidth = 1.dp,
        iconTint = primaryWhite
    )
}

@Composable
fun FilledRoundButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int
) {
    RoundButton(
        modifier = modifier,
        onClick = onClick,
        iconRes = iconRes,
        backgroundColor = primaryWhite,
        borderColor = Color.Transparent,
        borderWidth = 0.dp,
        iconTint = primaryBlack
    )
}


@Preview
@Composable
fun ButtonsPreview() {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.icon_google),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            },
            onClick = {},
            text = "Sign up with Google"
        )

        GlowButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            text = "Sign up with Google"
        )
    }
}
