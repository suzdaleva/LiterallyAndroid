package com.silentspring.uikit.components

import android.annotation.SuppressLint
import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.silentspring.uikit.utils.orFalse


//ToDo усовершенствовать WebView, если это будет необходимо
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    url: String,
    onBackClick: () -> Unit
) {

    var webView: WebView? by remember { mutableStateOf(null) }

    BackHandler {
        if (webView?.canGoBack().orFalse()) webView?.goBack() else onBackClick()
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                setBackgroundColor(Color.TRANSPARENT)
                webViewClient = WebViewClient()
                loadUrl(url)
                webView = this
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}