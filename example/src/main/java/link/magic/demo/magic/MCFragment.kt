package link.magic.demo.magic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import link.magic.android.MagicConnect
import link.magic.android.modules.connect.response.DisconnectResponse
import link.magic.android.modules.connect.response.UserInfoResponse
import link.magic.android.modules.connect.response.ShowWalletResponse
import link.magic.android.modules.connect.response.WalletInfoResponse
import link.magic.demo.tabs.MainTabActivity
import link.magic.demo.R

class MCFragment: Fragment() {

    private lateinit var tabActivity: MainTabActivity
    private lateinit var magic: MagicConnect

    private lateinit var inflatedView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        tabActivity = requireActivity() as MainTabActivity
        magic = tabActivity.magic as MagicConnect

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
        val completable = magic.connect.getWalletInfo()
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
        val completable = magic.connect.showWallet()
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
        val completable = magic.connect.requestUserInfo()
        completable.whenComplete { response: UserInfoResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null) {
                tabActivity.toastAsync("Request User Info" + response.result.email)
            }
        }
    }

    fun disconnect(v: View) {
        val completable = magic.connect.disconnect()
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
