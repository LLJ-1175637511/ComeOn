package com.comeon.android.foundation.spi

import java.util.Collections
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap

object InstanceProvider {

    private val autoRegisterInstanceMap = ConcurrentHashMap<Class<*>, Any>()
    private val customInstanceMap = ConcurrentHashMap<Class<*>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T) {
        customInstanceMap[clazz] = instance
    }

    fun <T : Any> get(clazz: Class<T>, isUseCache: Boolean = true): T? {
        //自定义实例 直接使用自定义缓存
        customInstanceMap[clazz]?.let {
            return it as T
        }
        if (!isUseCache) {
            //不使用缓存 直接查找实例返回
            getFirstInstance(clazz)?.let {
                return it
            }
        }
        //查缓存
        autoRegisterInstanceMap[clazz]?.let {
            return it as T
        }
        //查找实例并缓存
        getFirstInstance(clazz)?.let {
            autoRegisterInstanceMap[clazz] = it
        }
        return null
    }

    fun <T> getAll(clazz: Class<T>): List<T> {
        val list = mutableListOf<T>()
        list.addAll(loadServicesInternal(clazz))
        customInstanceMap[clazz]?.let {
            list.add(it as T)
        }
        return Collections.unmodifiableList(list)
    }

    private fun <T> getFirstInstance(clazz: Class<T>): T? {
        val services = loadServicesInternal(clazz).toList()
        return if (services.isNotEmpty()) {
            services[0]
        } else {
            null
        }
    }

    private fun <T> loadServicesInternal(clazz: Class<T>): Iterable<T> {
        return try {
            ServiceLoader.load(clazz)
        } catch (e: Exception) {
            emptyList()
        }
    }

}