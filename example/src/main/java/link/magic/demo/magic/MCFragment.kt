package link.magic.demo.magic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import link.magic.android.Magic
import link.magic.android.modules.wallet.requestConfiguration.RequestUserInfoWithUIConfiguration
import link.magic.android.modules.wallet.requestConfiguration.WalletUserInfoEmailOptions
import link.magic.android.modules.wallet.requestConfiguration.WalletUserInfoScope
import link.magic.android.modules.wallet.response.DisconnectResponse
import link.magic.android.modules.wallet.response.RequestUserInfoWithUIResponse
import link.magic.android.modules.wallet.response.ShowWalletResponse
import link.magic.android.modules.wallet.response.WalletInfoResponse
import link.magic.demo.tabs.MainTabActivity
import link.magic.demo.R

class MCFragment: Fragment() {

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

        inflatedView =  inflater.inflate(R.layout.tab_mc, container, false)

        val getWalletInfo : Button = inflatedView.findViewById(R.id.get_wallet_info)
        getWalletInfo.setOnClickListener {
            getWalletInfo(it)
        }
        val showWallet: Button = inflatedView.findViewById(R.id.show_wallet)
        showWallet.setOnClickListener{
            showWallet(it)
        }
        val requestUserInfo: Button = inflatedView.findViewById(R.id.request_user_info)
        requestUserInfo.setOnClickListener {
            requestUserInfo(it)
        }
        val disconnect : Button = inflatedView.findViewById(R.id.disconnect)
        disconnect.setOnClickListener {
            disconnect(it)
        }

        return inflatedView
    }

    /**
     * Connect Module
     */
    fun getWalletInfo(v: View) {
        val completable = magic.wallet.getInfo()
        completable.whenComplete { response: WalletInfoResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("show Wallet:" + response.result.walletType)
            }
        }
    }
    fun showWallet(v: View) {
        val completable = magic.wallet.showUI()
        completable.whenComplete { response: ShowWalletResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("show Wallet:" + response.result)
            }
        }
    }

    fun requestUserInfo(v: View) {
        val config = RequestUserInfoWithUIConfiguration(
            scope = WalletUserInfoScope(email = WalletUserInfoEmailOptions.required)
        )
        val completable = magic.wallet.requestUserInfoWithUI(config)
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
        val completable = magic.wallet.disconnect()
        completable.whenComplete { response: DisconnectResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("Disconnect" + response.result)
            }
        }
    }
}
