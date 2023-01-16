package link.magic.android.extension.oauth.requestConfiguration

data class OAuthConfiguration constructor(var provider: OAuthProvider, var redirectURI: String, var scope: List<String>?, var loginHint: String?) {
    constructor(provider: OAuthProvider, redirectURI: String): this(provider, redirectURI, null, null)
}
