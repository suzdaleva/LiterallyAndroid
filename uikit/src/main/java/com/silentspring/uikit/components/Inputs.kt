package com.silentspring.uikit.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silentspring.uikit.R
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.silentspring.uikit.utils.clearFocusOnKeyboardDismiss

@Composable
fun OutlinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    supportingText: String? = null,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {

    BaseTextField(
        modifier = modifier,
        text = text,
        labelText = labelText,
        supportingText = supportingText,
        enabled = enabled,
        onValueChange = onValueChange,
        singleLine = singleLine,
        isError = isError,
        trailingIcon = {
            IconButton(onClick = { onValueChange("").takeIf { !isError } }) {
                Icon(
                    painter = painterResource(R.drawable.icon_clear.takeIf { !isError }
                        ?: R.drawable.icon_attention),
                    tint = primaryWhite.takeIf { !isError } ?: errorColor,
                    contentDescription = null
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}


@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    supportingText: String? = null,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    BaseTextField(
        modifier = modifier,
        text = text,
        labelText = labelText,
        supportingText = supportingText,
        enabled = enabled,
        onValueChange = onValueChange,
        singleLine = singleLine,
        isError = isError,
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = painterResource(R.drawable.icon_eye_open.takeIf { !isPasswordVisible }
                        ?: R.drawable.icon_eye_closed),
                    tint = primaryWhite,
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(
            mask = '\u25CF'
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}


@Composable
private fun BaseTextField(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    supportingText: String? = null,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isError: Boolean = false,
    trailingIcon: @Composable () -> Unit,
    showTrailingIconPersistently: Boolean = false,
    shape: Shape = RoundedCornerShape(27.dp),
    textStyle: TextStyle = LiterallyTypography.bodyLarge,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val color by remember(isError) {
        mutableStateOf(primaryWhite.takeIf { !isError } ?: errorColor)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()


    OutlinedTextField(
        modifier = modifier.clearFocusOnKeyboardDismiss(),
        value = text,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = textStyle,
        label = { Text(text = labelText) },
        supportingText = supportingText?.let {
            {
                Text(
                    modifier = Modifier,
                    text = it,
                    style = LiterallyTypography.bodyLarge.copy(
                        fontSize = 12.sp,
                        color = color
                    )
                )
            }
        },
        trailingIcon = if ((text.isNotEmpty() && (isFocused || isError)) || showTrailingIconPersistently) trailingIcon else null,
        isError = isError,
        shape = shape,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = outlinedInputColors(),
        interactionSource = interactionSource
    )
}


@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    supportingText: String? = null,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isError: Boolean = false,
    trailingIcon: @Composable () -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BaseTextField(
        modifier = modifier,
        text = text,
        labelText = labelText,
        supportingText = supportingText,
        enabled = enabled,
        onValueChange = onValueChange,
        singleLine = singleLine,
        isError = isError,
        trailingIcon = trailingIcon,
        showTrailingIconPersistently = true,
        textStyle = LiterallyTypography.titleSmall.copy(fontSize = 16.sp),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}


@Preview
@Composable
private fun OutlinedTextFieldPreview() {
    OutlinedTextField(
        text = "Hello",
        labelText = "E-mail",
        supportingText = "Username must contain an uppercase letter, a lowercase letter and a numeric character",
        isError = true,
        onValueChange = {}
    )
}


@Composable
private fun outlinedInputColors() = OutlinedTextFieldDefaults.colors(
    //Text
    focusedTextColor = primaryWhite,
    unfocusedTextColor = primaryWhite,
    disabledTextColor = primaryWhite,
    errorTextColor = primaryWhite,

    //Cursor
    cursorColor = primaryWhite,
    errorCursorColor = errorColor,

    //Selection
    selectionColors = null,

    //Border
    focusedBorderColor = primaryWhite,
    unfocusedBorderColor = primaryWhite,
    disabledBorderColor = primaryWhite,
    errorBorderColor = errorColor,

    //Label
    focusedLabelColor = primaryWhite,
    unfocusedLabelColor = primaryWhite,
    disabledLabelColor = primaryWhite,
    errorLabelColor = errorColor,

    //SupportingText
    focusedSupportingTextColor = primaryWhite,
    unfocusedSupportingTextColor = primaryWhite,
    disabledSupportingTextColor = primaryWhite,
    errorSupportingTextColor = errorColor,

    //Prefix
    focusedPrefixColor = primaryWhite,
    unfocusedPrefixColor = primaryWhite,
    disabledPrefixColor = primaryWhite,
    errorPrefixColor = errorColor,

    //Suffix
    focusedSuffixColor = primaryWhite,
    unfocusedSuffixColor = primaryWhite,
    disabledSuffixColor = primaryWhite,
    errorSuffixColor = errorColor
)