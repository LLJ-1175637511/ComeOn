package com.lh.comeon.demo.view.fresco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.comeon.android.demo.databinding.ActivityTestFrescoBinding
import com.comeon.android.library.ui.fresco.ext.setDynamicImageUrl

class TestFrescoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestFrescoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestFrescoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dynamicImage.setDynamicImageUrl("https://pic1.zhimg.com/v2-ced66979f2d5ca4efa1aadb92b3c3b50.webp")
//        binding.dynamicImage.setDynamicImageUrl("https://picx.zhimg.com/v2-b979d884e1aef69f3abf47ac1fc74092.png")

    }
}