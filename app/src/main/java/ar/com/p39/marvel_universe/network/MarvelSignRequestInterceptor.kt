package ar.com.p39.marvel_universe.network

import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MarvelSignRequestInterceptor(private val apiKey: String, private val apiSecret: String) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val ts = System.currentTimeMillis().toString()
        val hash = md5(ts + apiSecret + apiKey)

        var request = chain.request()
        val url = request.url.newBuilder()
        .addQueryParameter("ts", ts)
        .addQueryParameter("apikey", apiKey)
        .addQueryParameter("hash", hash)
            .build()

        request = request.newBuilder().url(url).build()

        return chain.proceed(request)
    }

    private fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest: MessageDigest = MessageDigest
                .getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest: ByteArray = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}