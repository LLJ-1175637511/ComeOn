package com.comeon.android.library.kit

import java.util.Stack

class SafeStack<T> : Stack<T>() {

    override fun peek(): T? {
        if (empty()) return null
        return super.peek()
    }

    fun copy(): SafeStack<T> {
        val newStack = SafeStack<T>()
        newStack.addAll(this)
        return newStack
    }

}