package link.magic.demo.magic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import link.magic.android.Magic
import link.magic.android.MagicEvent
import link.magic.android.core.relayer.MagicEventListener
import link.magic.android.modules.wallet.requestConfiguration.RequestUserInfoWithUIConfiguration
import link.magic.android.modules.wallet.requestConfiguration.WalletUserInfoEmailOptions
import link.magic.android.modules.wallet.requestConfiguration.WalletUserInfoScope
import link.magic.android.modules.wallet.response.DisconnectResponse
import link.magic.android.modules.wallet.response.RequestUserInfoWithUIResponse
import link.magic.android.modules.wallet.response.ShowWalletResponse
import link.magic.android.modules.wallet.response.WalletInfoResponse
import link.magic.demo.R
import link.magic.demo.login.MALoginActivity
import link.magic.demo.tabs.MainTabActivity


class MCFragment: Fragment(), MagicEventListener {

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
        magic.rpcProvider.setMagicEventListener(this)

        inflatedView =  inflater.inflate(R.layout.tab_mc, container, false)

        val getWalletInfo : Button = inflatedView.findViewById(R.id.get_wallet_info)
        getWalletInfo.setOnClickListener {
            getInfo(it)
        }
        val showWallet: Button = inflatedView.findViewById(R.id.show_wallet)
        showWallet.setOnClickListener{
            showUI(it)
        }
        val requestUserInfo: Button = inflatedView.findViewById(R.id.request_user_info)
        requestUserInfo.setOnClickListener {
            requestUserInfoWithUI(it)
        }
        val disconnect : Button = inflatedView.findViewById(R.id.disconnect)
        disconnect.setOnClickListener {
            disconnect(it)
        }

        return inflatedView
    }

    /**
     * Wallet Module
     */
    fun getInfo(v: View) {
        val completable = magic.wallet.getInfo(this.requireContext())
        completable.whenComplete { response: WalletInfoResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("Wallet Type:" + response.result.walletType)
            }
        }
    }
    fun showUI(v: View) {
        val completable = magic.wallet.showUI(this.requireContext())
        completable.whenComplete { response: ShowWalletResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("show Wallet:" + response.result)
            }
        }
    }

    fun requestUserInfoWithUI(v: View) {
        val config = RequestUserInfoWithUIConfiguration(
            scope = WalletUserInfoScope(email = WalletUserInfoEmailOptions.required)
        )
        val completable = magic.wallet.requestUserInfoWithUI(this.requireContext(), config)
        completable.whenComplete { response: RequestUserInfoWithUIResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("Request User Info" + response.result.email)
            }
        }
    }

    fun disconnect(v: View) {
        val completable = magic.wallet.disconnect(this.requireContext())
        completable.whenComplete { response: DisconnectResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {

                tabActivity.toastAsync("Disconnect -" + response.result)
                val intent = Intent(activity, MALoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onMagicEvent(eventType: MagicEvent, data: String) {
        if (eventType == MagicEvent.CLOSED_BY_USER) {
            magic.events.emit(MagicEvent.CLOSE_MAGIC_WINDOW, this.requireContext())
        }
    }
}
