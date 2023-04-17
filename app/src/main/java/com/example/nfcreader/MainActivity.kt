package com.example.nfcreader

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nfcreader.databinding.ActivityMainBinding
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("NFCData", "onNewIntent")

        val tagFromIntent: Tag = intent!!.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!

//        val ndef = Ndef.get(tagFromIntent)
//        val cardId = bytesToHexString(tagFromIntent.id)
//        var chipId: String? = null
////        if (ndef != null) {
//            ndef.connect()
//            val message = ndef.ndefMessage
//            if (message != null && message.records.isNotEmpty()) {
//                val record = message.records[0]
//                val payload = record.payload
//                chipId = bytesToHexString(payload.copyOfRange(3, payload.size))
//            }
//            ndef.close()
////        }
//        Log.d("NDEF", "Card ID: $cardId")
//        Log.d("NDEF", "Chip ID: $chipId")

        val n = NfcA.get(tagFromIntent)
        val nfc = NfcA.get(tagFromIntent)
        var test=bytesToHexString(nfc.atqa)
        var test2=nfc.sak.toString()
        var id2= n.tag.id
        Log.e("NFCData", n.toString())
        Log.e("NFCData", id2.toString())
        Log.e("NFCData", test)
        Log.e("NFCData", test2)




        val uid = bytesToHexString(nfc.tag.id)

        val nfcA = NfcA.get(tagFromIntent)
        nfcA.connect()
        val data = nfcA.transceive(byteArrayOf(0x30.toByte(), 0x00.toByte()))
        nfcA.close()
        Log.e("NFCResult", data.toString())

        binding.idTextView.text = uid.toString()
        binding.idTextView.setText(uid.toString())
        Toast.makeText(applicationContext, uid, Toast.LENGTH_LONG).show()

        val atqa: ByteArray = n.getAtqa()
        val sak: Short = nfc.getSak()
        Log.e("NFCData", "id : " + uid.toString())
        Log.e("NFCData", sak.toString())

        nfc.connect()
        val isConnected = nfc.isConnected()
        Log.e("NFCData", "Connection" + isConnected.toString())

    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexArray[v ushr 4]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

}