package com.comeon.android.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.comeon.android.foundation.spi.InstanceProvider
import com.comeon.android.app.databinding.ActivityMainBinding
import com.comeon.android.component.home.HomeActivity
import com.comeon.android.library.kit.MultipleClickActionHelper
import com.lh.comeon.demo.DemoActivity
import com.lh.comeon.demo.spi.IFood

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private val clickActionHelper = MultipleClickActionHelper(6) {
        startActivity(Intent(this, HomeActivity::class.java))
    }

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
        binding.btTest.setOnClickListener {
            startActivity(Intent(this, DemoActivity::class.java))
        }
        binding.root.setOnClickListener {
            clickActionHelper.registerClick()
        }
        startActivity(Intent(this, DemoActivity::class.java))
    }

}