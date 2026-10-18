package link.magic.android.modules.extension

/**
 * Configuration for an official Magic extension, such as Solana or Bitcoin.
 *
 * Pass these to the `Magic` constructor. Each one is sent to the relayer under
 * `ext.<name>` in the URL params, the same as extensions in magic-js.
 *
 * Gson writes the fields of the implementing class as the value, so `name` must
 * not have a backing field.
 */
interface ExtensionConfiguration {
    /** The extension name, e.g. `solana`. Matches the `name` field in magic-js. */
    val name: String
}

internal object ExtensionConfigurations {
    /**
     * Maps each config to `{ "<name>": <config> }`. A later config with the same
     * name replaces an earlier one, as in magic-js. `null` when there are none,
     * so the `ext` param is left out of the URL.
     */
    fun toParams(extensions: List<ExtensionConfiguration>): Map<String, Any>? {
        if (extensions.isEmpty()) return null
        val params = LinkedHashMap<String, Any>()
        extensions.forEach { params[it.name] = it }
        return params
    }
}
