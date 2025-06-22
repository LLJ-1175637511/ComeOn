package com.lh.comeon.demo.spi

import com.google.auto.service.AutoService

@AutoService(IFood::class)
class Food2: IFood {
    override fun getName(): String {
        return "Food2"
    }
}