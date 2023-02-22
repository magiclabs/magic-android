package link.magic.demo.magic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import link.magic.android.Magic
import link.magic.android.modules.user.requestConfiguration.GenerateIdTokenConfiguration
import link.magic.android.modules.user.requestConfiguration.GetIdTokenConfiguration
import link.magic.android.modules.user.requestConfiguration.UpdateEmailConfiguration
import link.magic.android.modules.user.response.*
import link.magic.demo.tabs.MainTabActivity
import link.magic.demo.R


class MAFragment: Fragment() {

    private lateinit var tabActivity: MainTabActivity
    private lateinit var magic: Magic

    private lateinit var inflatedView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        tabActivity = requireActivity() as MainTabActivity
        magic = tabActivity.magic as Magic

        inflatedView =  inflater.inflate(R.layout.tab_ma, container, false)

        val getIdToken: Button = inflatedView.findViewById(R.id.get_id_token)
        getIdToken.setOnClickListener{
            getIdToken(it)
        }
        val generateIdTokenButton: Button = inflatedView.findViewById(R.id.generate_id_token)
        generateIdTokenButton.setOnClickListener {
            generateIdToken(it)
        }
        val getMetadataButton : Button = inflatedView.findViewById(R.id.get_metadata)
        getMetadataButton.setOnClickListener {
            getMetadata(it)
        }
        val updateEmailButton : Button = inflatedView.findViewById(R.id.update_email)
        updateEmailButton.setOnClickListener {
            updateEmail(it)
        }
        val updateSmsButton : Button = inflatedView.findViewById(R.id.update_sms)
        updateSmsButton.setOnClickListener {
            updateSMS(it)
        }
        val isLoggedInButton : Button = inflatedView.findViewById(R.id.is_logged_in)
        isLoggedInButton.setOnClickListener {
            isLoggedIn(it)
        }
        val showMfaButton : Button = inflatedView.findViewById(R.id.show_settings)
        showMfaButton.setOnClickListener {
            showMFA()
        }
        val logoutButton : Button = inflatedView.findViewById(R.id.logout)
        logoutButton.setOnClickListener {
            logout(it)
        }

        return inflatedView
    }

    /**
     * User Module
     */
    fun getIdToken(v: View) {
        val configuration = GetIdTokenConfiguration(lifespan = 900)
        val completable = magic.user.getIdToken(this.requireActivity(), configuration)
        completable.whenComplete { response: GetIdTokenResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("Id Token:" + response.result)
            }
        }
    }

    /**
     * Generates a Decentralized Id Token with optional serialized data.
     */
    fun generateIdToken(v: View) {
        val configuration = GenerateIdTokenConfiguration(lifespan = 3600, attachment = "none")
        val completable = magic.user.generateIdToken(this.requireActivity(), configuration)
        completable.whenComplete { response: GenerateIdTokenResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("Id Token:" + response.result)
            }
        }
    }

    fun getMetadata(v: View) {
        val completable = magic.user.getMetadata(this.requireActivity())
        completable.whenComplete { response: GetMetadataResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("Email: " + response.result.email + "\n" + "issuer: " + response.result.issuer + "\n")
            }
        }
    }

    fun updateEmail(v: View) {
        val configuration = UpdateEmailConfiguration("")
        val completable = magic.user.updateEmail(this.requireContext(), configuration)
        completable.whenComplete { response: UpdateEmailResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                Log.d("result-token", response.result.toString())
            }
        }
    }

    fun updateSMS(v: View) {
        val completable = magic.user.updatePhoneNumber(this.requireActivity())
        completable.whenComplete { updatePhoneNumberResponse: UpdatePhoneNumberResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (updatePhoneNumberResponse != null) {
                Log.d("Update phone number result", updatePhoneNumberResponse.result.toString())
            }
        }
    }

    fun isLoggedIn(v: View) {
        val completable = magic.user.isLoggedIn(this.requireActivity())
        completable.whenComplete { response: IsLoggedInResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null && response.result) {
                tabActivity.toastAsync("You're Logged In")
            }
        }
    }

    /**
     * Show MFA
     */
    private fun showMFA() {
        val result = (magic as Magic).user.showSettings(this.requireContext())
        result.whenComplete { isShowMfaResponse: ShowMfaResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (isShowMfaResponse != null) {
                var resp = isShowMfaResponse.result
                tabActivity.toastAsync("Showing MFA response: $resp ")
            }
        }
    }


    fun logout(v: View) {
        tabActivity.toastAsync("Logging out...")
        val completable = magic.user.logout(this.requireActivity())
        completable.whenComplete { response: LogoutResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null && response.result) {
                tabActivity.toastAsync("You're logged out!")
                tabActivity.startMALoginActivity()
            }
        }
    }
}
