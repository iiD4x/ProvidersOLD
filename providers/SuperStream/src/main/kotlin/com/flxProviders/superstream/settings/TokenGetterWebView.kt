package com.flxProviders.superstream.settings

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebView
import com.flixclusive.core.util.network.Crypto
import com.flixclusive.core.util.network.okhttp.UserAgentManager
import com.flixclusive.provider.settings.ProviderSettings

private const val GET_TOKEN_URL_ENCODED
    = "aHR0cHM6Ly93d3cuZmViYm94LmNvbS9sb2dpbi9nb29nbGU/anVtcD0lMkY="

@SuppressLint("SetJavaScriptEnabled", "ViewConstructor")
internal class TokenGetterWebView(
    context: Context,
    settings: ProviderSettings,
    onTokenReceived: () -> Unit
) : WebView(context) {
    private val cookieManager = CookieManager.getInstance()
    val verticalScrollRange: Int
        get() = computeVerticalScrollRange() - height

    init {
        this.settings.javaScriptEnabled = true
        this.settings.domStorageEnabled = true

        webViewClient = TokenGetterWebViewClient(
            settings = settings,
            onTokenReceived = onTokenReceived
        )

        loadTokenUrl()
    }

    fun loadTokenUrl() {
        stopLoading()
        loadUrl("about:blank")
        clearCache(true)
        clearHistory()
        clearFormData()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()

        this.settings.userAgentString = UserAgentManager.getRandomUserAgent()

        loadUrl(Crypto.base64Decode(GET_TOKEN_URL_ENCODED))

    }
}