package me.geek.bag.scheduler

import me.geek.bag.SetTings
import me.geek.bag.api.PlayerData
import me.geek.bag.utils.ClassSerializable
import me.geek.bag.utils.serializeItemStacks
import org.bukkit.inventory.ItemStack


/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
abstract class PlayerDataBase : PlayerData {

    @Synchronized
    override fun upBagItems(newItems: MutableList<ItemStack>) {
        this.itemsData.clear()
        this.itemsData.addAll(newItems)
    }

    override fun getBagPageSize(): Int {
        SetTings.bagPageData.permGroup.forEach {
            if (player.hasPermission(it.perm)) {
                return it.size
            }
        }
        return SetTings.bagPageData.defaultSize
    }

    override fun toByteArray(): ByteArray {
        this.itemString = this.itemsData.serializeItemStacks()
        return ClassSerializable.gsonSerialize(this)
    }
}