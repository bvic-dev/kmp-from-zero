package com.bvic.followmycrypto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform