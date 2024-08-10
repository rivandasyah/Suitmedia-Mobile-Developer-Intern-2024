package com.rivaphy.suitmediatestapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rivaphy.suitmediatestapp.R
import com.rivaphy.suitmediatestapp.databinding.ActivitySecondScreenBinding

class SecondScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondScreenBinding
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = savedInstanceState?.getString(FirstScreenActivity.TAG) ?: intent.getStringExtra(FirstScreenActivity.TAG)
        binding.tvSecondScreenName.text = name

        val selectedUserName = intent.getStringExtra(ThirdScreenActivity.USER_TAG)

        if (selectedUserName != null) {
            binding.tvSecondScreenSelectedUserName.text = selectedUserName
        } else {
            binding.tvSecondScreenSelectedUserName.text = getString(R.string.selected_user_name)
        }

        binding.ivSecondScreenBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSecondScreenChooseAUser.setOnClickListener {
            val intent = Intent(this, ThirdScreenActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val selectedUserName = data?.getStringExtra(ThirdScreenActivity.USER_TAG)
            binding.tvSecondScreenSelectedUserName.text = selectedUserName
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FirstScreenActivity.TAG, name)
    }
}