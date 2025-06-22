package com.lh.comeon.demo.spi

import com.google.auto.service.AutoService

@AutoService(IFood::class)
class Food1: IFood {
    override fun getName(): String {
        return "Food1"
    }
}