package link.magic.android

import com.google.gson.Gson
import com.google.gson.JsonParser
import link.magic.android.modules.bitcoin.BitcoinConfiguration
import link.magic.android.modules.bitcoin.BitcoinSignTransactionResponse
import link.magic.android.modules.bitcoin.BitcoinTransactionInput
import link.magic.android.modules.bitcoin.BitcoinTransactionOutput
import link.magic.android.modules.bitcoin.BitcoinTransactionParams
import link.magic.android.modules.extension.ExtensionConfigurations
import link.magic.android.modules.multichain.TypedArray
import link.magic.android.modules.solana.SolanaConfiguration
import link.magic.android.modules.solana.SolanaSerializeConfig
import link.magic.android.modules.solana.SolanaSignMessageParams
import link.magic.android.modules.solana.SolanaSignMessageResponse
import link.magic.android.modules.solana.SolanaSignTransactionResponse
import link.magic.android.modules.solana.SolanaTransactionParams
import link.magic.android.modules.solana.SolanaTransactionType
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MultichainExtensionTest {

    private val gson = Gson()
    private val flag = "MAGIC_PAYLOAD_FLAG_TYPED_ARRAY"

    private fun json(text: String) = JsonParser.parseString(text)

    // Typed arrays

    @Test
    fun typedArrayEncodesBytesAsUnsignedDecimals() {
        val bytes = byteArrayOf(1, 2, -1, 0)
        assertEquals(
            json("""{"constructor":"Uint8Array","data":"1,2,255,0","flag":"$flag"}"""),
            gson.toJsonTree(TypedArray(bytes))
        )
    }

    @Test
    fun typedArrayDecodesBytes() {
        val decoded = gson.fromJson("""{"constructor":"Uint8Array","data":"99,53,247","flag":"$flag"}""", TypedArray::class.java)
        assertArrayEquals(byteArrayOf(99, 53, -9), decoded.bytes)
    }

    @Test
    fun typedArrayDecodesBufferAndEmptyData() {
        val buffer = gson.fromJson("""{"constructor":"Buffer","data":"7","flag":"$flag"}""", TypedArray::class.java)
        assertArrayEquals(byteArrayOf(7), buffer.bytes)
        val empty = gson.fromJson("""{"constructor":"Uint8Array","data":"","flag":"$flag"}""", TypedArray::class.java)
        assertEquals(0, empty.bytes.size)
    }

    @Test(expected = Exception::class)
    fun typedArrayRejectsOutOfRangeBytes() {
        gson.fromJson("""{"constructor":"Uint8Array","data":"1,256","flag":"$flag"}""", TypedArray::class.java)
    }

    @Test(expected = Exception::class)
    fun typedArrayRejectsMissingFlag() {
        gson.fromJson("""{"constructor":"Uint8Array","data":"1"}""", TypedArray::class.java)
    }

    // Extension config

    @Test
    fun solanaConfigurationEncodesRpcUrlAndChainType() {
        assertEquals(
            json("""{"rpcUrl":"https://api.devnet.solana.com","chainType":"SOLANA"}"""),
            gson.toJsonTree(SolanaConfiguration("https://api.devnet.solana.com"))
        )
    }

    @Test
    fun bitcoinConfigurationEncodesNetworkUnderOptions() {
        assertEquals(
            json("""{"rpcUrl":"https://blockstream.info/testnet/api","chainType":"BITCOIN","options":{"network":"testnet"}}"""),
            gson.toJsonTree(BitcoinConfiguration("https://blockstream.info/testnet/api", "testnet"))
        )
    }

    @Test
    fun extensionParamsAreKeyedByNameAndLastOneWins() {
        val params = ExtensionConfigurations.toParams(
            listOf(SolanaConfiguration("https://first"), BitcoinConfiguration("https://btc", "testnet"), SolanaConfiguration("https://second"))
        )
        assertEquals(
            json("""{"solana":{"rpcUrl":"https://second","chainType":"SOLANA"},"bitcoin":{"rpcUrl":"https://btc","chainType":"BITCOIN","options":{"network":"testnet"}}}"""),
            gson.toJsonTree(params)
        )
    }

    @Test
    fun noExtensionsLeavesOutTheParam() {
        assertNull(ExtensionConfigurations.toParams(emptyList()))
    }

    // Solana params

    @Test
    fun legacyTransactionIsSentWithTypeLegacy() {
        val params = SolanaTransactionParams(SolanaTransactionType.Legacy, TypedArray(byteArrayOf(1, 2)), SolanaSerializeConfig(requireAllSignatures = false, verifySignatures = false))
        assertEquals(
            json("""{"type":"legacy","serialized":{"constructor":"Uint8Array","data":"1,2","flag":"$flag"},"serializeConfig":{"requireAllSignatures":false,"verifySignatures":false}}"""),
            gson.toJsonTree(params)
        )
    }

    @Test
    fun v0TransactionIsSentWithTypeZeroAndNoConfig() {
        val params = SolanaTransactionParams(SolanaTransactionType.V0, TypedArray(byteArrayOf(3)), null)
        assertEquals(
            json("""{"type":0,"serialized":{"constructor":"Uint8Array","data":"3","flag":"$flag"}}"""),
            gson.toJsonTree(params)
        )
    }

    @Test
    fun signMessageSendsAStringOrTypedArray() {
        assertEquals(json("""{"message":"hello"}"""), gson.toJsonTree(SolanaSignMessageParams("hello")))
        assertEquals(
            json("""{"message":{"constructor":"Uint8Array","data":"104,105","flag":"$flag"}}"""),
            gson.toJsonTree(SolanaSignMessageParams(TypedArray("hi".toByteArray())))
        )
    }

    @Test
    fun signMessageKeepsPercentSignsAndNewlines() {
        val message = "uri: https://a.com/?q=a%2Fb\nsecond line 100%"
        val encoded = gson.toJson(SolanaSignMessageParams(message))
        assertEquals(message, JsonParser.parseString(encoded).asJsonObject.get("message").asString)
    }

    // Responses

    @Test
    fun signedTransactionResponseDecodesRawTransaction() {
        val response = gson.fromJson(
            """{"jsonrpc":"2.0","id":1,"result":{"rawTransaction":{"constructor":"Uint8Array","data":"9,8,7","flag":"$flag"}}}""",
            SolanaSignTransactionResponse::class.java
        )
        assertArrayEquals(byteArrayOf(9, 8, 7), response.result.rawTransaction)
    }

    @Test
    fun signMessageResponseDecodesSignature() {
        val response = gson.fromJson(
            """{"jsonrpc":"2.0","id":1,"result":{"constructor":"Uint8Array","data":"1,2,3","flag":"$flag"}}""",
            SolanaSignMessageResponse::class.java
        )
        assertArrayEquals(byteArrayOf(1, 2, 3), response.signature)
    }

    // Bitcoin

    @Test
    fun bitcoinParamsUseTxNumKeyAndOmitMissingAddress() {
        val params = BitcoinTransactionParams(
            listOf(BitcoinTransactionInput("ab", 1, 0.001)),
            listOf(BitcoinTransactionOutput("tb1qexample", 0.0009))
        )
        assertEquals(
            json("""{"inputs":[{"txid":"ab","tx_num":1,"value":0.001}],"outputs":[{"address":"tb1qexample","value":0.0009}]}"""),
            gson.toJsonTree(params)
        )
    }

    @Test
    fun bitcoinResponseDecodesSignedTransaction() {
        val response = gson.fromJson(
            """{"jsonrpc":"2.0","id":2,"result":{"signedTransaction":"0200","transactionHash":"ff"}}""",
            BitcoinSignTransactionResponse::class.java
        )
        assertEquals("0200", response.result.signedTransaction)
        assertEquals("ff", response.result.transactionHash)
    }
}
