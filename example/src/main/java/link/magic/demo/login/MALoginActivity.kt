package link.magic.demo.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import link.magic.DemoApp
import link.magic.android.Magic
import link.magic.android.extension.oauth.oauth
import link.magic.android.extension.oauth.requestConfiguration.OAuthConfiguration
import link.magic.android.extension.oauth.requestConfiguration.OAuthProvider
import link.magic.android.extension.oauth.response.OAuthResponse
import link.magic.android.extension.oidc.openid
import link.magic.android.extension.oidc.requestConfiguration.OpenIdConfiguration
import link.magic.android.modules.auth.requestConfiguration.LoginWithEmailOTPConfiguration
import link.magic.android.modules.auth.requestConfiguration.LoginWithMagicLinkConfiguration
import link.magic.android.modules.auth.requestConfiguration.LoginWithSMSConfiguration
import link.magic.android.modules.auth.response.DIDToken
import link.magic.android.modules.user.response.IsLoggedInResponse
import link.magic.demo.R
import link.magic.demo.UtilActivity

class MALoginActivity : UtilActivity(), AdapterView.OnItemSelectedListener {
    private val providersList: List<String> = OAuthProvider.values().map {
        it.toString()
    }
    lateinit var magic: Any
    var selectedListIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        magic = (applicationContext as DemoApp).magic
        setContentView(R.layout.activity_ma_login)

        val emailButton: Button = findViewById<Button>(R.id.email_login_btn)
        emailButton.setOnClickListener{
            loginWithEmail(it)
        }
        val emailOTPButton: Button = findViewById<Button>(R.id.email_otp_login_btn)
        emailOTPButton.setOnClickListener{
            loginWithEmailOTP(it)
        }
        val smsButton: Button = findViewById<Button>(R.id.sms_login_btn)
        smsButton.setOnClickListener {
            loginWithSMS(it)
        }
        val socialLoginButton : Button = findViewById<Button>(R.id.social_login_btn)
        socialLoginButton.setOnClickListener {
            loginViaSocialProviders(it)
        }
        val openIdLoginButton : Button = findViewById<Button>(R.id.openId_login_btn)
        openIdLoginButton.setOnClickListener {
            loginWithOpenId(it)
        }

        /**
         * Provider Spinner
         */
        val spinner: Spinner = findViewById<Spinner>(R.id.provider_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            providersList
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        selectedListIndex = pos
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun isLoggedIn() {
        toastAsync("Logging in...")
        val result = (magic as Magic).user.isLoggedIn()
        result.whenComplete { isLoggedInResponse: IsLoggedInResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (isLoggedInResponse != null && isLoggedInResponse.result) {
                toastAsync("Logged In")
                startTabActivity()
            }
        }
    }

    /**
     * Login With Magic Link
     */
    private fun loginWithEmail(v: View) {
        val email = findViewById<EditText>(R.id.email_input)
        val configuration = LoginWithMagicLinkConfiguration(email.text.toString())
        val result = (magic as Magic).auth.loginWithMagicLink(this, configuration)
        toastAsync("Logging in...")
        result.whenComplete { token: DIDToken?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (token != null && !token.hasError()) {
                Log.d("login", token.result)
                startTabActivity()
            } else {
                Log.d("login", "Unable to login")
            }
        }
    }

    /**
     * Login With Magic Link
     */
    private fun loginWithSMS(v: View) {
        val phoneNumber = findViewById<EditText>(R.id.phone_number_input)
        val configuration = LoginWithSMSConfiguration(phoneNumber.text.toString())
        val result = (magic as Magic).auth.loginWithSMS(this, configuration)
        toastAsync("Logging in...")
        result.whenComplete { token: DIDToken?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (token != null && !token.hasError()) {
                Log.d("login", token.result)
                startTabActivity()
            } else {
                Log.d("login", "Unable to login")
            }
        }
    }

    /**
     * Login With Magic Link
     */
    private fun loginWithEmailOTP(v: View) {
        val email = findViewById<EditText>(R.id.email_input)
        val configuration = LoginWithEmailOTPConfiguration(email.text.toString())
        val result = (magic as Magic).auth.loginWithEmailOTP(this, configuration)
        toastAsync("Logging in...")
        result.whenComplete { token: DIDToken?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (token != null && !token.hasError()) {
                Log.d("login", token.result)
                startTabActivity()
            } else {
                Log.d("login", "Unable to login")
            }
        }
    }


    /**
     * OAuth Extension
     */
    private fun loginViaSocialProviders(view: View) {
        val configuration = OAuthConfiguration(OAuthProvider.values()[selectedListIndex], "link.magic.demo://callback")
        val data = (magic as Magic).oauth.loginWithPopup(this, configuration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        try {
            val result = (magic as Magic).oauth.getResult(data)

            result.whenComplete { response: OAuthResponse?, error: Throwable? ->
                if (error != null) {
                    Log.d("error", error.localizedMessage)
                }
                if (response != null && !response.hasError()) {
                    response.result.magic.idToken?.let { Log.d("login", it) }
                    startTabActivity()
                } else {
                    Log.d("login", "Not Logged in")
                }
            }
        } catch(err: Error) {
            Log.d("error", err.toString())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * oidc Extension
     */
    private fun loginWithOpenId(v: View) {
        val providerId = findViewById<EditText>(R.id.provider_id_text)
        val jwt = findViewById<EditText>(R.id.jwt_text)
        val configuration = OpenIdConfiguration(jwt.text.toString(), providerId.text.toString())
        val did = (magic as Magic).openid.loginWithOIDC(configuration)
        did.whenComplete { response: DIDToken?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null && !response.hasError()) {
                response.result?.let { Log.d("login", it) }

            } else {
                Log.d("login", "OpenID Not Logged in")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
