package com.bvic.followmycrypto

import androidx.compose.runtime.Composable
import com.bvic.followmycrypto.theme.FollowMyCryptoTheme
import com.bvic.followmycrypto.ui.list.CryptoListScreen

@Composable
fun App() {
    FollowMyCryptoTheme {
        CryptoListScreen()
    }
}
