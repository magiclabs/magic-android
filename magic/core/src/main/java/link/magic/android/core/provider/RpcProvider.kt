package link.magic.android.core.provider

import android.content.Context
import android.util.Log
import android.util.Log.DEBUG
import com.google.gson.Gson
import link.magic.android.core.relayer.message.OutboundMessageType
import link.magic.android.core.relayer.message.RequestData
import link.magic.android.core.relayer.urlBuilder.URLBuilder
import link.magic.android.utils.GsonExtension
import link.magic.android.utils.Number
import io.reactivex.Flowable
import link.magic.android.MagicCore
import org.web3j.protocol.Web3jService
import org.web3j.protocol.core.BatchRequest
import org.web3j.protocol.core.BatchResponse
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import org.web3j.protocol.websocket.events.Notification
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.CompletableFuture
import link.magic.android.core.relayer.WebViewWrapper


/**
 * RpcProvider
 * @param
 *
 */
class RpcProvider internal constructor(initialContext: Context, val urlBuilder: URLBuilder) : Web3jService {

    /**
     * Construct Relayer to send payloads to WebView
     */
    internal var overlay = WebViewWrapper(initialContext, urlBuilder)

    /**
     * get and setter Context for UI views to be displayed
     */
    var context: Context = overlay.mMutableContext.baseContext
    set(newCtx) {
        overlay.setContext(newCtx)
        field = newCtx
    }


    @Throws(IOException::class)
    override fun <T : Response<*>> send(

            requestPayload: Request<*, *>, responseType: Class<T>): T {
         try {
             Log.println(DEBUG, "MagicSDK-Warning", "It's highly recommended to send payloads using sendAsync()")
             return sendAsync(requestPayload, responseType).get()
        } catch (e: Exception) {
            throw RuntimeException("Unexpected exception", e.cause)
        }
    }

    /**
     * Perform a synchronous JSON-RPC batch requests.
     *
     * @param batchRequest requests to perform
     * @return deserialized JSON-RPC responses
     * @throws IOException thrown if failed to perform a batch request
     */
    override fun sendBatch(batchRequest: BatchRequest?): BatchResponse {
        throw UnsupportedOperationException(String.format(
                "Magic-SDK: Service %s does not support sendBatch",
                this.javaClass.simpleName))
    }

    /**
     * Performs an asynchronous JSON-RPC batch requests.
     *
     * @param batchRequest batch request to perform
     * @return CompletableFuture that will be completed when a result is returned or if a request
     * has failed
     */
    override fun sendBatchAsync(batchRequest: BatchRequest?): CompletableFuture<BatchResponse> {
        throw UnsupportedOperationException(String.format(
                "Magic-SDK: Service %s does not support sendBatchAsync",
                this.javaClass.simpleName))
    }

    /**
     * Send request to webview
     */
    override fun <T : Response<*>> sendAsync(
            requestPayload: Request<*, *>, responseType: Class<T>): CompletableFuture<T> {

        /* Overwrite id with random Long number */
        val newRandomId = Number().generateRandomId()

        val result = CompletableFuture<T>()
        val rpcPayload = RequestForSerialization(requestPayload.method, requestPayload.params, newRandomId)
        val request = RequestData(rpcPayload, OutboundMessageType.MAGIC_HANDLE_REQUEST, urlBuilder.encodedParams);

        // Serialize class to Json Object
        val message =  GsonExtension()
            .serialize()
                .toJson(request)
                .replace("\\n", "")

        if (MagicCore.debugEnabled) {
            Log.d("Magic", "Prepare Message: $message")
        }

        /* send the promise to webview queue waiting for dispatch.
        When the payload result comes back the handler will resolve the result asynchronously */
        overlay.enqueue(message, newRandomId, fun (responseString: String) {
                    val data = Gson().fromJson(responseString, responseType)
                    result.complete(data)
                })
        return result
    }

    /**
     * Not supported yet.
     */
    override fun <T : Notification<*>?> subscribe(
            request: Request<*, *>?,
            unsubscribeMethod: String,
            responseType: Class<T>
    ): Flowable<T> {
        throw UnsupportedOperationException(String.format(
                "Magic-SDK: Service %s does not support subscriptions",
                this.javaClass.simpleName))
    }

    @Throws(IOException::class)
    override fun close() {
        throw UnsupportedOperationException(String.format(
                "Magic-SDK: Service %s does not support Close function",
                this.javaClass.simpleName))
    }
}
