package com.silentspring.feature.login.presentation.screens.credentials.state

import androidx.annotation.StringRes
import com.silentspring.feature.login.R

enum class AuthorizationType(
    @StringRes val titleRes: Int,
    @StringRes val buttonTextRes: Int,
    @StringRes val googleButtonTextRes: Int,
    @StringRes val footerTextRes: Int,
    @StringRes val footerActionTextRes: Int
) {
    SIGN_UP(
        titleRes = R.string.sign_up_title,
        buttonTextRes = R.string.sign_up_button_text,
        googleButtonTextRes = R.string.sign_up_with_google_button_text,
        footerTextRes = R.string.already_have_account,
        footerActionTextRes = R.string.sign_in_button_text
    ),
    SIGN_IN(
        titleRes = R.string.sign_in_title,
        buttonTextRes = R.string.sign_in_button_text,
        googleButtonTextRes = R.string.sign_in_with_google_button_text,
        footerTextRes = R.string.do_not_have_account,
        footerActionTextRes = R.string.sign_up_button_text
    );

    fun switchType(): AuthorizationType {
        return if (this == SIGN_UP) SIGN_IN else SIGN_UP
    }
}