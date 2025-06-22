package com.comeon.android.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.comeon.android.foundation.spi.InstanceProvider
import com.comeon.android.app.databinding.ActivityMainBinding
import com.lh.comeon.demo.DemoActivity
import com.lh.comeon.demo.spi.IFood

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }

        InstanceProvider.register(IFood::class.java, object : IFood {
            override fun getName(): String {
                return "Food3"
            }
        })
        binding.root.setOnClickListener {
            startActivity(Intent(this, DemoActivity::class.java))
        }
    }

}