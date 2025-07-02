package com.comeon.android.app.home.video.ui.activity

import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.comeon.android.app.home.video.ui.fragment.PlayerFragment
import com.comeon.android.library.container.activity.HostActivity

class PlayerActivity : HostActivity() {

    companion object {
        const val TAG = "PlayerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    override fun buildHostFragment(): Fragment {
        return PlayerFragment()
    }

}