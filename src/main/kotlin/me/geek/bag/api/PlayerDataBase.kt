package me.geek.bag.api

import me.geek.bag.utils.ClassSerializable
import me.geek.bag.utils.serializeItemStacks

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
abstract class PlayerDataBase : PlayerData{

    override fun toByteArray(): ByteArray {
        return ClassSerializable.gsonSerialize(this)
    }

    override val itemString by lazy {
        this.itemsData.serializeItemStacks()
    }
}