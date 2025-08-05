package com.example.test.data.network

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.test.MyApplication
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import org.chromium.net.CronetException
import org.chromium.net.UploadDataProvider
import org.chromium.net.apihelpers.UploadDataProviders
import org.json.JSONObject
import java.nio.ByteBuffer

interface NetworkCallback {
    fun onSuccess(result: Any?)
    fun onError(errorMessage: String)
}

class WebService(apiEndPoint: String) {
    private val TIMEOUT = 10000L


    var mURL: String
    var mParams: JSONObject?= null


    var timeoutHandler: Handler? = null

    var request: UrlRequest? = null
    var callback: NetworkCallback? = null

    init {
        mURL = "https://jsonplaceholder.typicode.com/${apiEndPoint}"
    }

    //region GET
    fun get(onSuccess: NetworkCallback) {
        callback = onSuccess
        sendAPIRequest(HttpMethod.GET)
    }


    private fun sendAPIRequest(httpMethod: HttpMethod, jsonObject: JSONObject? = JSONObject()) {
        mParams = jsonObject
        this.request = MyApplication.cronetEngine?.newUrlRequestBuilder(
            mURL,
            MyUrlRequestCallback(),
            MyApplication.cronetCallbackExecutorService
        )?.addHeader("Content-Type", "application/json")
            ?.setHttpMethod(httpMethod.name)?.setPriority(UrlRequest.Builder.REQUEST_PRIORITY_HIGHEST)
            ?.setUploadDataProvider(generateUploadDataProvider(jsonObject), MyApplication.cronetCallbackExecutorService)
            ?.build()

        if (this.request == null) {
            callback?.onError("Something went wrong")
        } else {
            request?.start()

            //Cronet API doesn't support timeout out of the box. All network calls
            //are unending. So we need to manually handle timeout.
            setupTimeoutHandler()
        }
    }

    private fun generateUploadDataProvider(jsonObject: JSONObject?): UploadDataProvider? {
        jsonObject?.toString()?.takeIf { it.isNotBlank() }?.let { string ->
            convertStringToBytes(string)?.let { byteArray ->
                return@generateUploadDataProvider UploadDataProviders.create(byteArray)
            }
        }
        return null
    }

    private fun convertStringToBytes(payload: String): ByteArray? {
        val bytes: ByteArray
        val byteBuffer = ByteBuffer.wrap(payload.toByteArray())
        if (byteBuffer.hasArray()) {
            bytes = byteBuffer.array()
        } else {
            bytes = ByteArray(byteBuffer.remaining())
            byteBuffer[bytes]
        }
        return bytes
    }

    private fun cleanupTimeoutHandler() {
        if (timeoutHandler != null) {
            timeoutHandler?.removeCallbacks(timeoutRunnable)
            timeoutHandler = null
        }
    }

    private fun setupTimeoutHandler() {
        cleanupTimeoutHandler()
        Looper.myLooper()?.let { looper ->
            timeoutHandler = Handler(looper)
            timeoutHandler?.postDelayed(timeoutRunnable, TIMEOUT)
        }
    }

    private val timeoutRunnable by lazy {
        Runnable {
            cleanupTimeoutHandler()
            request?.cancel()
            request = null
        }
    }

    inner class MyUrlRequestCallback : UrlRequest.Callback() {
        var responseBody: String? = null
        var httpStatusCode: Int = 0

        override fun onRedirectReceived(request: UrlRequest?, info: UrlResponseInfo?, newLocationUrl: String?) {
            request?.followRedirect()
        }

        override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
            responseBody = ""
            request?.read(ByteBuffer.allocateDirect(102400))
        }

        override fun onReadCompleted(request: UrlRequest?, info: UrlResponseInfo?, byteBuffer: ByteBuffer?) {
            httpStatusCode = info?.httpStatusCode ?: HttpStatusCode.INTERNAL_SERVER_ERROR.value

            byteBuffer?.flip()

            byteBuffer?.let {
                val byteArray = ByteArray(it.remaining())
                it.get(byteArray)
                String(byteArray, Charsets.UTF_8)
            }?.apply {
                responseBody += this
            }

            byteBuffer?.clear()
            request?.read(byteBuffer)
        }

        override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
            cleanupTimeoutHandler()
            httpStatusCode = info?.httpStatusCode ?:  HttpStatusCode.INTERNAL_SERVER_ERROR.value
            Log.e("API", "URL: ${mURL}")
            Log.e("API", "Response: $responseBody")
            Log.e("API", "httpStatusCode: $httpStatusCode")

            callback?.onSuccess(responseBody)
        }

        override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException?) {
            cleanupTimeoutHandler()
            httpStatusCode = info?.httpStatusCode ?: HttpStatusCode.INTERNAL_SERVER_ERROR.value
            Log.e("API", "URL: ${mURL}")
            Log.e("API", "Response: $responseBody")
            Log.e("API", "httpStatusCode: $httpStatusCode")

            callback?.onError("Something went wrong")
        }

        override fun onCanceled(request: UrlRequest?, info: UrlResponseInfo?) {
            super.onCanceled(request, info)

            callback?.onError("Something went wrong")
        }
    }
}