package ar.com.p39.marvel_universe.network

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test

class MarvelSignRequestInterceptorTest {
    private val mockWebServer = MockWebServer()

    @Test
    @Throws(Exception::class)
    fun testHttpInterceptor() {
        // GIVEN
        val apiKey = "1234"
        val secret = "abcd"

        mockWebServer.enqueue(MockResponse())
        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(MarvelSignRequestInterceptor(apiKey, secret, "1")).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        // THEN
        val request: RecordedRequest = mockWebServer.takeRequest()
        val url = request.requestUrl!!

        // THEN
        assertNotNull(url.queryParameter("ts"))
        assertEquals("1234", url.queryParameter("apikey"))
        assertEquals("ffd275c5130566a2916217b101f26150", url.queryParameter("hash"))
    }
}