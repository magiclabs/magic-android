package link.magic.android.modules.web3j.signTypedData

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.web3j.signTypedData.request.EIP712TypedData
import link.magic.android.modules.web3j.signTypedData.request.EIP712TypedDataLegacyFields
import link.magic.android.modules.web3j.signTypedData.response.SignTypedData
import org.web3j.protocol.core.Request
import java.lang.reflect.Type


class SignTypedDataExtension(private val rpcProvider: RpcProvider): BaseModule(rpcProvider) {

    private val gson = Gson()

    fun signTypedDataLegacy(context: Context, address: String?, data: List<EIP712TypedDataLegacyFields>): Request<*, SignTypedData> {
        provider.context = context
        return Request(
                "eth_signTypedData",
                listOf(data, address),
                rpcProvider,
                SignTypedData::class.java)
    }

    fun signTypedDataLegacy(context: Context, address: String?, jsonData: String): Request<*, SignTypedData>{
        provider.context = context
        val legacyTypedDataList: Type = object: TypeToken<List<EIP712TypedDataLegacyFields>?>() {}.type
        val data = gson.fromJson<List<EIP712TypedDataLegacyFields>>(jsonData, legacyTypedDataList)
        return Request(
                "eth_signTypedData",
                listOf(data, address),
                rpcProvider,
                SignTypedData::class.java)
    }

    fun signTypedData(context: Context, address: String?, jsonData: String): Request<*, SignTypedData>  {
        provider.context = context
        return Request(
                "eth_signTypedData_v3",
                listOf(address, gson.fromJson(jsonData, EIP712TypedData::class.java)),
                rpcProvider,
                SignTypedData::class.java)
    }


    fun signTypedDataV4(context: Context, address: String?, jsonData: String): Request<*, SignTypedData> {
        provider.context = context
        return Request(
                "eth_signTypedData_v4",
                listOf(address, gson.fromJson(jsonData, EIP712TypedData::class.java)),
                rpcProvider,
                SignTypedData::class.java)
    }
}
