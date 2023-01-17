package link.magic.android.utils
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import java.lang.reflect.Type
import java.util.*


class GsonExtension {

    fun serializeSkipFields(vararg skipList: String) : Gson {

        /* Deserialize CAP Enum to its value in lower case */
        val paramsListType: Type = object : TypeToken<List<Any>>() {}.type
        val paramsSerializer = JsonSerializer(fun(list: List<Any>, _: Type, context: JsonSerializationContext): JsonElement {
                val jsonArray = JsonArray()

                for (item in list) {
                    when (item) {

                        is Transaction -> {
                            val jsonElement: JsonElement = context.serialize(item, item.javaClass)
                            val jsonObject = jsonElement.asJsonObject

                            /* Convert gasPrice and gas to bigInt hex */
                            if (item.gas != null) jsonObject.add("gas", JsonParser().parse(item.gas))
                            if (item.gasPrice != null) jsonObject.add("gasPrice", JsonParser().parse(item.gasPrice))
                            jsonArray.add(jsonObject)
                        }
                        is DefaultBlockParameterName -> jsonArray.add(item.value)
                        else -> {
                            val jsonElement: JsonElement = context.serialize(item, item.javaClass)
                            jsonArray.add(jsonElement)
                        }
                    }
                }

                return jsonArray
            })

        return GsonBuilder()
                    .addSerializationExclusionStrategy(object : ExclusionStrategy {
                        override fun shouldSkipField(f: FieldAttributes): Boolean {
                            val fieldName = f.name.toLowerCase(Locale.ROOT)

                            // Remove field name that can't be serialized in Request
                            return skipList.contains(fieldName)
                        }

                        override fun shouldSkipClass(aClass: Class<*>?): Boolean {
                            return false
                        }
                    })
                    .registerTypeAdapter(paramsListType, paramsSerializer)
                    .disableHtmlEscaping()
                    .create()
        }
}

