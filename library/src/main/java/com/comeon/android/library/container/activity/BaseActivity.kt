package com.comeon.android.library.container.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.comeon.android.library.kit.SafeStack

abstract class BaseActivity : AppCompatActivity() {

    companion object {

        private val stack = SafeStack<Activity>()

        @JvmStatic
        internal fun addActivity(activity: Activity) {
            stack.push(activity)
        }

        @JvmStatic
        internal fun removeActivity(activity: Activity) {
            stack.remove(activity)
        }

        @JvmStatic
        fun getTopActivity(): Activity {
            return stack.peek()!!
        }

        @JvmStatic
        fun getActivityCount(): Int {
            return stack.size
        }

        @JvmStatic
        fun getActivityStack(): SafeStack<Activity> {
            //返回一个副本，避免外部修改原始栈
            return stack.copy()
        }
    }

}