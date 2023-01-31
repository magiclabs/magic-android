package link.magic.android.modules.web3j.signTypedData.request

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@Keep
class EIP712TypedData (
         @param:JsonProperty(value = "types") val types: HashMap<String, List<Entry>>,
         @param:JsonProperty(value = "primaryType") val primaryType: String,
         @param:JsonProperty(value = "message") val message: Any,
         @param:JsonProperty(value = "domain") val domain: EIP712Domain) {

     @Keep
     class Entry @JsonCreator constructor(
            @param:JsonProperty(value = "name") val name: String,
            @param:JsonProperty(value = "type") val type: String)
     @Keep
     class EIP712Domain @JsonCreator constructor(
            @param:JsonProperty(value = "name") val name: String,
            @param:JsonProperty(value = "version") val version: String,
            @param:JsonProperty(value = "chainId") val chainId: String,
            @param:JsonProperty(value = "verifyingContract") val verifyingContract: String)
 }
