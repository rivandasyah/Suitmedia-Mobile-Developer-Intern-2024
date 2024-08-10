package com.rivaphy.suitmediatestapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rivaphy.suitmediatestapp.databinding.ActivityFirstScreenBinding

class FirstScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnFirstScreenNext.setOnClickListener {
            val name = binding.edtFirstScreenName.editText?.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SecondScreenActivity::class.java)
                intent.putExtra(TAG, name)
                startActivity(intent)
            }
        }

        binding.btnFirstScreenCheck.setOnClickListener {
            val sentence = binding.edtFirstScreenPalindrome.editText?.text.toString()

            if (sentence.isEmpty()) {
                Toast.makeText(this, "Palindrome is empty", Toast.LENGTH_SHORT).show()
            } else {
                if (isPalindrome(sentence)) {
                    showDialog("isPalindrome")
                } else {
                    showDialog("not palindrome")
                }
            }
        }

    }

    private fun isPalindrome(sentence: String): Boolean {
        val checkPalindrome = sentence.replace("\\s".toRegex(), "").lowercase()
        return checkPalindrome == checkPalindrome.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        const val TAG = "USER_NAME"
    }
}