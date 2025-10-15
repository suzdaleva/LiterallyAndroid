package com.silentspring.uikit.components

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun GlowSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    enabled: Boolean = true
) {
    Switch(
        modifier = modifier.drawBehind {
            drawRoundRect(
                color = primaryWhite,
                topLeft = Offset(0f, size.height/2 - 16.dp.toPx()),
                size = Size(52.dp.toPx(), 32.dp.toPx()),
                cornerRadius = CornerRadius(26.dp.toPx(), 26.dp.toPx()),
                style = Stroke(1.dp.toPx())
            )
        },
        checked = checked,
        enabled = enabled,
        onCheckedChange = onCheckedChange,
        thumbContent = {
           Spacer(
               modifier = Modifier
                   .drawBehind {
                       val paint = Paint().apply {
                           color = android.graphics.Color.WHITE
                           style = Paint.Style.FILL
                       }
                       paint.maskFilter = BlurMaskFilter(3.dp.toPx(), BlurMaskFilter.Blur.NORMAL)
                       if (checked) {
                           drawContext.canvas.nativeCanvas.drawCircle(
                               center.x, center.y, size.height / 2, paint
                           )
                       } else {
                           drawCircle(
                               color = primaryWhite,
                               style = Stroke(1.dp.toPx())
                           )
                       }
                   }
                   .size(34.dp)
                   .clip(CircleShape)
                   .background(primaryWhite.takeIf { checked } ?: Color.Transparent)
           )
        },
        colors = switchColors
    )
}


private val switchColors = SwitchColors(
    checkedThumbColor = Color.Transparent,
    checkedTrackColor = Color.Transparent,
    checkedBorderColor = Color.Transparent,
    checkedIconColor = Color.Transparent,
    uncheckedThumbColor = Color.Transparent,
    uncheckedTrackColor = Color.Transparent,
    uncheckedBorderColor = Color.Transparent,
    uncheckedIconColor = Color.Transparent,
    disabledCheckedThumbColor = Color.Transparent,
    disabledCheckedTrackColor = Color.Transparent,
    disabledCheckedBorderColor = Color.Transparent,
    disabledCheckedIconColor = Color.Transparent,
    disabledUncheckedThumbColor = Color.Transparent,
    disabledUncheckedTrackColor = Color.Transparent,
    disabledUncheckedBorderColor = Color.Transparent,
    disabledUncheckedIconColor = Color.Transparent
)

@Preview
@Composable
private fun GlowSwitchPreview() {
    GlowSwitch(
        checked = false,
        onCheckedChange = {}
    )
}