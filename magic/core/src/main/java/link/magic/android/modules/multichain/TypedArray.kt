package link.magic.android.modules.multichain

import androidx.annotation.Keep
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * Binary data in the typed-array wire format that mgbox uses in both directions.
 *
 * JSON has no binary type, so mgbox sends a `Uint8Array` as
 * `{"constructor":"Uint8Array","data":"99,53,247","flag":"MAGIC_PAYLOAD_FLAG_TYPED_ARRAY"}`.
 * It turns this back into a real `Uint8Array` before the relayer sees it, and
 * encodes typed arrays in responses (e.g. Solana signatures) the same way.
 */
@Keep
@JsonAdapter(TypedArrayAdapter::class)
class TypedArray(val bytes: ByteArray) {

    companion object {
        internal const val FLAG = "MAGIC_PAYLOAD_FLAG_TYPED_ARRAY"
    }

    override fun equals(other: Any?): Boolean = other is TypedArray && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int = bytes.contentHashCode()
}

@Keep
internal class TypedArrayAdapter : TypeAdapter<TypedArray>() {

    override fun write(out: JsonWriter, value: TypedArray?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.beginObject()
        out.name("constructor").value("Uint8Array")
        out.name("data").value(value.bytes.joinToString(",") { (it.toInt() and 0xff).toString() })
        out.name("flag").value(TypedArray.FLAG)
        out.endObject()
    }

    override fun read(reader: JsonReader): TypedArray? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        var data: String? = null
        var flag: String? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "data" -> data = reader.nextString()
                "flag" -> flag = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        if (flag != TypedArray.FLAG) throw IllegalArgumentException("Not a typed array")
        // `Buffer` and `Uint8Array` both arrive here. Their bytes are joined the same way.
        val joined = data ?: throw IllegalArgumentException("Typed array has no data")
        if (joined.isEmpty()) return TypedArray(ByteArray(0))
        val bytes = joined.split(",").map { part ->
            val value = part.toIntOrNull()
            if (value == null || value !in 0..255) throw IllegalArgumentException("Invalid byte: $part")
            value.toByte()
        }
        return TypedArray(bytes.toByteArray())
    }
}
