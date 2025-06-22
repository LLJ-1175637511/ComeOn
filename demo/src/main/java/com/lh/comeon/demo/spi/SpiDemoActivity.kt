package com.lh.comeon.demo.spi

import androidx.appcompat.app.AppCompatActivity
import com.comeon.android.foundation.spi.InstanceProvider
import com.comeon.android.demo.databinding.ActivityDemoTestSpiBinding
import com.comeon.android.library.ext.showToast

class SpiDemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoTestSpiBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDemoTestSpiBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }
        binding.btSpiAllTest.setOnClickListener {
            InstanceProvider.getAll(IFood::class.java).forEach {
                showToast(it.getName())
            }
        }
        binding.btSpiSingleTest.setOnClickListener {
            InstanceProvider.get(IFood::class.java)?.let {
                showToast(it.getName())
            }
        }
    }

}