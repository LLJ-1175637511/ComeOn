package com.comeon.android.library.container.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.comeon.android.library.databinding.ActivityHostContainerBinding

open class HostActivity : CoActivity() {

    protected lateinit var binding: ActivityHostContainerBinding

    private val hostFragment: Fragment by lazy {
        buildHostFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.hostFragmentContainer.id, hostFragment, getFragmentTag())
            .commitAllowingStateLoss()
    }

    protected open fun buildHostFragment(): Fragment {
        return Fragment()
    }

    protected open fun getFragmentTag() = "HostFragment"

}