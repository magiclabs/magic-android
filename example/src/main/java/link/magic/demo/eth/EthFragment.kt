package link.magic.demo.eth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import link.magic.android.Magic
import link.magic.android.modules.web3j.contract.MagicTxnManager
import link.magic.android.modules.web3j.signTypedData.request.EIP712TypedDataLegacyFields
import link.magic.android.modules.web3j.signTypedData.response.SignTypedData
import link.magic.demo.R
import link.magic.demo.eth.contract.simpleStorage.SimpleStorage
import link.magic.demo.tabs.MainTabActivity
import org.web3j.protocol.Web3j
import org.web3j.protocol.admin.methods.response.PersonalSign
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction
import org.web3j.protocol.core.methods.response.*
import org.web3j.protocol.geth.Geth
import org.web3j.tx.gas.StaticGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigInteger


class EthFragment: Fragment() {

    private lateinit var mainTabActivity: MainTabActivity
    private lateinit var web3j: Web3j
    private lateinit var gethWeb3j: Geth
    private lateinit var magic: Magic

    private lateinit var inflatedView: View

    private var account: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainTabActivity = requireActivity() as MainTabActivity
        magic = mainTabActivity.magic

        if (magic is Magic) {
            val provider = (magic as Magic).rpcProvider
            provider.context = this.requireActivity()
            web3j = Web3j.build(provider)
            gethWeb3j = Geth.build(provider)
        }
        if (magic is Magic) {
            val provider = (magic as Magic).rpcProvider
            provider.context = this.requireActivity()
            web3j = Web3j.build(provider)
            gethWeb3j = Geth.build(provider)
        }

        inflatedView =  inflater.inflate(R.layout.tab_eth, container, false)

        // Web3 Button binding
        inflatedView.findViewById<Button>(R.id.get_address).setOnClickListener {
            getAddress(it)
        }
         inflatedView.findViewById<Button>(R.id.get_balance).setOnClickListener {
            getBalance(it)
        }
        inflatedView.findViewById<Button>(R.id.get_network).setOnClickListener {
            getChainId(it)
        }
         inflatedView.findViewById<Button>(R.id.send_transaction).setOnClickListener {
            sendTransaction(it)
        }
         inflatedView.findViewById<Button>(R.id.get_coinbase).setOnClickListener {
            getCoinbase(it)
        }
         inflatedView.findViewById<Button>(R.id.person_sign).setOnClickListener {
            personSign(it)
        }
         inflatedView.findViewById<Button>(R.id.sign_typed_data_legacy).setOnClickListener {
            signTypedDataLegacy(it)
        }
         inflatedView.findViewById<Button>(R.id.sign_typed_data_legacy_json).setOnClickListener {
            signTypedDataLegacyJson(it)
        }
         inflatedView.findViewById<Button>(R.id.sign_typed_data).setOnClickListener {
            signTypedData(it)
        }
         inflatedView.findViewById<Button>(R.id.sign_typed_data_v4).setOnClickListener {
            signTypedDataV4(it)
        }

        // Contract
        inflatedView.findViewById<Button>(R.id.deploy_contract).setOnClickListener {
            deployContract(it)
        }
        inflatedView.findViewById<Button>(R.id.contract_read).setOnClickListener {
            contractRead(it)
        }
        inflatedView.findViewById<Button>(R.id.contract_write).setOnClickListener {
            contractWrite(it)
        }

        return inflatedView
    }

    /**
     * Web3 functions
     */

    private fun getAddress(v: View){
        try {
            val accounts = web3j.ethAccounts().sendAsync()
            accounts.whenComplete { accRepsonse: EthAccounts?, error: Throwable? ->
                if (error != null) {
                    Log.d("MagicError", error.localizedMessage)
                }
                if (accRepsonse != null && !accRepsonse.hasError()) {
                    account = accRepsonse.accounts[0]
                    Log.d("d", "Your address is" + accRepsonse.accounts[0])
                    mainTabActivity.toastAsync("Your address is" + accRepsonse.accounts[0])
                }
            }

        } catch (e: Exception) {
            mainTabActivity.toastAsync(e.message)
        }
    }

    fun sendTransaction(v: View) {
        try {
            val value: BigInteger =  Convert.toWei("0.5", Convert.Unit.ETHER).toBigInteger()
            val transaction = createEtherTransaction(account, BigInteger("1"), BigInteger("21000"), BigInteger("21000"), account, value)
            val receipt = web3j.ethSendTransaction(transaction).sendAsync()
            receipt.whenComplete  { r: EthSendTransaction?, error: Throwable? ->
                Log.d("version", r.toString())
                if (error != null) {
                    Log.d("error", error.localizedMessage)
                }
                if (r != null && !r.hasError()) {
                    Log.d("Transaction complete: ", r.transactionHash)
                    mainTabActivity.toastAsync("Transaction complete: " + r.transactionHash)
                } else {
                    Log.d("login", "Transaction Incomplete")
                }
            }

        } catch (e: Exception) {
            Log.e("Error", e.localizedMessage)
        }
    }

    fun getBalance(v: View) {
        try {
            val ethGetBalance = web3j
                    .ethGetBalance(account, DefaultBlockParameterName.LATEST)
                    ?.send()
            mainTabActivity.toastAsync("Balance: " + ethGetBalance?.balance.toString())
        } catch (e: Exception) {
            Log.e("E", "error", e)
        }
    }

    fun getChainId(view: View) {
        val network = web3j.ethChainId().sendAsync()
        network.whenComplete { chainId: EthChainId?, error: Throwable? ->
            Log.d("version", chainId.toString())
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (chainId != null && !chainId.hasError()) {
                Log.d("login", chainId.result)
                mainTabActivity.toastAsync(chainId.id.toString())
            } else {
                Log.d("login", "Unable to login")
            }
        }
    }

    fun getCoinbase(view: View) {
        val ethCoinbase = web3j
                .ethCoinbase().send()
        mainTabActivity.toastAsync(ethCoinbase.address)
    }

    fun personSign(view: View) {
        val message = "Magic!!!"
        val personalSign = gethWeb3j.personalSign(
                message, account, "123")
                .sendAsync()
        personalSign.whenComplete { ps: PersonalSign, error: Throwable? ->
            Log.d("version", ps.signedMessage)
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (!ps.hasError()) {
                Log.d("Personal Sign", ps.signedMessage)
                mainTabActivity.toastAsync("Signed Message: " + ps.signedMessage)

                val recovered = gethWeb3j.personalEcRecover(message, ps.signedMessage).send()
                mainTabActivity.toastAsync("Recovered Address: " + recovered.recoverAccountId)
            } else {
                Log.d("login", "Unable to login")
            }
        }
    }

    fun ethSign(view: View) {
        val signedMessage = web3j.ethSign(account, "Hello world").sendAsync()
        signedMessage.whenComplete  { sig: EthSign?, error: Throwable? ->
            if (error != null) {
                mainTabActivity.toastAsync("Error: $error")
            }
            if (sig != null && !sig.hasError()) {
                mainTabActivity.toastAsync("Signature: " + sig.signature)
            } else {
                Log.d("Error", "Something went wrong")
            }
        }

    }

    fun signTypedDataLegacy(v: View) {
        val list = listOf(
                EIP712TypedDataLegacyFields("string", "Hello from Magic", "This message will be signed by you"),
                EIP712TypedDataLegacyFields("uint32", "Here is a number", "90210")
        )

        val signature = (magic as Magic).web3jSigExt.signTypedDataLegacy(this.requireActivity(), account, list).sendAsync()
        signature.whenComplete { sig: SignTypedData?, error: Throwable? ->
            if (error != null) {
                mainTabActivity.toastAsync("Error: $error")
            }
            if (sig != null && !sig.hasError()) {
                mainTabActivity.toastAsync("Signature: " + sig.result)
            } else {
                Log.d("Error", "Something went wrong")
            }
        }
    }

    fun signTypedDataLegacyJson(v: View) {
        val jsonString = "[{\"type\":\"string\",\"name\":\"Hello from Magic\",\"value\":\"This message will be signed by you\"},{\"type\":\"uint32\",\"name\":\"Here is a number\",\"value\":\"90210\"}]"
        val signature = (magic as Magic).web3jSigExt.signTypedDataLegacy(this.requireActivity(), account, jsonString).sendAsync()
        signature.whenComplete { sig: SignTypedData?, error: Throwable? ->
            if (error != null) {
                mainTabActivity.toastAsync("Error: $error")
            }
            if (sig != null && !sig.hasError()) {
                mainTabActivity.toastAsync("Signature: " + sig.result)
            } else {
                Log.d("Error", "Something went wrong")
            }
        }
    }

    fun signTypedData(v: View) {
        val jsonString = "{\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"version\",\"type\":\"string\"},{\"name\":\"verifyingContract\",\"type\":\"address\"}],\"Order\":[{\"name\":\"makerAddress\",\"type\":\"address\"},{\"name\":\"takerAddress\",\"type\":\"address\"},{\"name\":\"feeRecipientAddress\",\"type\":\"address\"},{\"name\":\"senderAddress\",\"type\":\"address\"},{\"name\":\"makerAssetAmount\",\"type\":\"uint256\"},{\"name\":\"takerAssetAmount\",\"type\":\"uint256\"},{\"name\":\"makerFee\",\"type\":\"uint256\"},{\"name\":\"takerFee\",\"type\":\"uint256\"},{\"name\":\"expirationTimeSeconds\",\"type\":\"uint256\"},{\"name\":\"salt\",\"type\":\"uint256\"},{\"name\":\"makerAssetData\",\"type\":\"bytes\"},{\"name\":\"takerAssetData\",\"type\":\"bytes\"}]},\"domain\":{\"name\":\"0x Protocol\",\"version\":\"2\",\"verifyingContract\":\"0x35dd2932454449b14cee11a94d3674a936d5d7b2\"},\"message\":{\"exchangeAddress\":\"0x35dd2932454449b14cee11a94d3674a936d5d7b2\",\"senderAddress\":\"0x0000000000000000000000000000000000000000\",\"makerAddress\":\"0x338be8514c1397e8f3806054e088b2daf1071fcd\",\"takerAddress\":\"0x0000000000000000000000000000000000000000\",\"makerFee\":\"0\",\"takerFee\":\"0\",\"makerAssetAmount\":\"97500000000000\",\"takerAssetAmount\":\"15000000000000000\",\"makerAssetData\":\"0xf47261b0000000000000000000000000d0a1e359811322d97991e03f863a0c30c2cf029c\",\"takerAssetData\":\"0xf47261b00000000000000000000000006ff6c0ff1d68b964901f986d4c9fa3ac68346570\",\"salt\":\"1553722433685\",\"feeRecipientAddress\":\"0xa258b39954cef5cb142fd567a46cddb31a670124\",\"expirationTimeSeconds\":\"1553808833\"},\"primaryType\":\"Order\"}"
        val signature = (magic as Magic).web3jSigExt.signTypedData(this.requireActivity(), account, jsonString).sendAsync()
        signature.whenComplete { sig: SignTypedData?, error: Throwable? ->
            if (error != null) {
                mainTabActivity.toastAsync("Error: $error")
            }
            if (sig != null && !sig.hasError()) {
                mainTabActivity.toastAsync("Signature: " + sig.result)
            } else {
                Log.d("Error", "Something went wrong")
            }
        }
    }

    fun signTypedDataV4(v: View) {
        val jsonString = "{\"domain\":{\"chainId\":1,\"name\":\"Ether Mail\",\"verifyingContract\":\"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC\",\"version\":\"1\"},\"message\":{\"contents\":\"Hello, Bob!\",\"from\":{\"name\":\"Cow\",\"wallets\":[\"0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826\",\"0xDeaDbeefdEAdbeefdEadbEEFdeadbeEFdEaDbeeF\"]},\"to\":[{\"name\":\"Bob\",\"wallets\":[\"0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB\",\"0xB0BdaBea57B0BDABeA57b0bdABEA57b0BDabEa57\",\"0xB0B0b0b0b0b0B000000000000000000000000000\"]}]},\"primaryType\":\"Mail\",\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"version\",\"type\":\"string\"},{\"name\":\"chainId\",\"type\":\"uint256\"},{\"name\":\"verifyingContract\",\"type\":\"address\"}],\"Group\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"members\",\"type\":\"Person[]\"}],\"Mail\":[{\"name\":\"from\",\"type\":\"Person\"},{\"name\":\"to\",\"type\":\"Person[]\"},{\"name\":\"contents\",\"type\":\"string\"}],\"Person\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"wallets\",\"type\":\"address[]\"}]}}"
        val signature = (magic as Magic).web3jSigExt.signTypedDataV4(this.requireActivity(), account, jsonString).sendAsync()
        signature.whenComplete { sig: SignTypedData?, error: Throwable? ->
            if (error != null) {
                mainTabActivity.toastAsync("Error: $error")
            }
            if (sig != null && !sig.hasError()) {
                mainTabActivity.toastAsync("Signature: " + sig.result)
            } else {
                Log.d("Error", "Something went wrong")
            }
        }
    }

    /**
     * deploy Contract
     */
    fun deployContract(view: View) {
        try {
            val price = BigInteger.valueOf(22000000000L)
            val limit = BigInteger.valueOf(4300000)
            val gasProvider = StaticGasProvider(price, limit)
            val contract = SimpleStorage.deploy(
                    web3j,
                    account?.let { MagicTxnManager(web3j, it) },
                    gasProvider
            ).sendAsync()
            contract.whenComplete { storage: SimpleStorage?, error: Throwable? ->
                if (error != null) {
                    mainTabActivity.toastAsync("Error: $error")
                }
                if (storage != null) {
                    mainTabActivity.toastAsync("Deploy to: " + storage.contractAddress)
                } else {
                    Log.d("Error", "Something went wrong")
                }
            }
        } catch (e: Exception) {
            Log.e("E", "error", e)
        }
    }

    /**
     * Contract Read
     */
    fun contractRead(view: View) {
        try {
            val transactionManager = account?.let { MagicTxnManager(web3j, it) }
            val price = BigInteger.valueOf(22000000000L)
            val limit = BigInteger.valueOf(4300000)
            val gasProvider = StaticGasProvider(price, limit)
            val contract = SimpleStorage.load("0x6a2d321a3679b1b3c8a19b84e41abd11763a8ab5", web3j, account?.let { MagicTxnManager(web3j, it) }, gasProvider)
            Log.d("Magic", contract.contractAddress)
            val ethGetCode: EthGetCode? =
                transactionManager?.getCode("0x6a2d321a3679b1b3c8a19b84e41abd11763a8ab5", DefaultBlockParameterName.LATEST)
            if (ethGetCode != null) {
                Log.d("EthGetCode", ethGetCode.code)
                if (ethGetCode.error != null) {
                    Log.d("EthGetCode", ethGetCode.error.toString())
                }
            }
            var code = Numeric.cleanHexPrefix(ethGetCode!!.code)
            val metadataIndex = code.indexOf("a165627a7a72305820")
            if (metadataIndex != -1) {
                code = code.substring(0, metadataIndex)
            }
            Log.d("MagicUnity", (code.isNotEmpty()).toString());
            if (contract.isValid) {
                val ethCall = contract.num().sendAsync()
                mainTabActivity.toastAsync(ethCall.toString())
            } else {
                throw Error("contract not valid")
            }
        } catch (e: Exception) {
            Log.e("E", "error", e)
        }
    }

    /**
     * Contract Write
     */
    fun contractWrite(view: View) {
        try {
            val price = BigInteger.valueOf(22000000000L)
            val limit = BigInteger.valueOf(4300000)
            val gasProvider = StaticGasProvider(price, limit)
            val contract = SimpleStorage.load("0x6a2d321a3679b1b3c8a19b84e41abd11763a8ab5", web3j, account?.let { MagicTxnManager(web3j, it) }, gasProvider)
            mainTabActivity.toastAsync(contract.isValid.toString())
            if (contract.isValid) {
                val ethCall = contract.set(BigInteger("100")).sendAsync()
                mainTabActivity.toastAsync(ethCall.toString())
            } else {
                throw Error("contract not valid")
            }
        } catch (e: Exception) {
            Log.e("E", "error", e)
        }
    }
}