package com.comeon.android.app.home.video.ui.activity

import androidx.fragment.app.Fragment
import com.comeon.android.app.home.video.ui.fragment.PlayerFragment
import com.comeon.android.library.container.activity.HostActivity

class PlayerActivity : HostActivity() {

    companion object {
        const val TAG = "PlayerActivity"
    }

    override fun buildHostFragment(): Fragment {
        return PlayerFragment()
    }

}