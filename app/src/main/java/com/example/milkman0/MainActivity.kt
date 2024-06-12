package com.example.milkman0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.milkman0.ui.theme.MilkMan0Theme
import android.content.Intent

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

class MainActivity : AppCompatActivity() {
    private val SMS_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }

        val buttonSend: Button = findViewById(R.id.button_send)
        val editTextMobile: EditText = findViewById(R.id.edit_mobile_no)
        val editTextMessage: EditText = findViewById(R.id.edit_message)

        buttonSend.setOnClickListener {
            // Get the values from the EditText fields
            val mobileNumbers = editTextMobile.text.toString().split(",").map { it.trim() }
            val message = editTextMessage.text.toString()
            sendSmsMessages(mobileNumbers, message)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == SMS_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Method to check if an app is installed
        private fun appInstalledOrNot(packageName: String): Boolean {
            val packageManager = packageManager
            return try {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                Log.d("MainActivity", "Package $packageName is installed.")
                true
            } catch (e: PackageManager.NameNotFoundException) {
                Log.d("MainActivity", "Package $packageName is NOT installed.")
                false
            }
        }
        // Method to send WhatsApp messages
        private fun sendWhatsAppMessages(mobileNumbers: List<String>, message: String) {
            for (mobileNumber in mobileNumbers) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+91$mobileNumber&text=$message")
                startActivity(intent)
            }
        }
        // Method to show option to send SMS
        private fun showSmsOption(mobileNumbers: List<String>, message: String) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("WhatsApp not installed")
            builder.setMessage("Would you like to send the message via SMS instead?")
            builder.setPositiveButton("Yes") { _, _ ->
                sendSmsMessages(mobileNumbers, message)
            }
            builder.setNegativeButton("No", null)
            builder.show()
        }
        // Method to send SMS messages
        private fun sendSmsMessages(mobileNumbers: List<String>, message: String) {
            val smsManager = SmsManager.getDefault()
            for (mobileNumber in mobileNumbers) {
                smsManager.sendTextMessage(mobileNumber, null, message, null, null)
            }
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show()
        }
}

