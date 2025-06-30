package com.lh.comeon.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.comeon.android.app.home.video.ui.activity.PlayerActivity
import com.comeon.android.demo.databinding.ActivityTestBinding
import com.lh.comeon.demo.dialog.TestBottomSheetDialog
import com.lh.comeon.demo.spi.SpiDemoActivity
import com.lh.comeon.demo.dialog.TestDialog
import com.lh.comeon.demo.view.animator.AnimatorDemoActivity
import com.lh.comeon.demo.view.fresco.TestFrescoActivity

class DemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityTestBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }
        binding.btSpiTest.setOnClickListener {
            startActivity(Intent(this, SpiDemoActivity::class.java))
        }
        binding.btBottomDialogTest.setOnClickListener {
            TestBottomSheetDialog().show(supportFragmentManager, "TestBottomSheetDialog")
        }
        binding.btDialogTest.setOnClickListener {
            TestDialog().show(supportFragmentManager, "TestDialog")
        }
        binding.btAnimatorTest.setOnClickListener {
            startActivity(Intent(this, AnimatorDemoActivity::class.java))
        }
        binding.btFrescoTest.setOnClickListener {
            startActivity(Intent(this, TestFrescoActivity::class.java))
        }
        binding.btPlayerTest.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
    }
}