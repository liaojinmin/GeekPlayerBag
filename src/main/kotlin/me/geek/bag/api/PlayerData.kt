package me.geek.bag.api

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
interface PlayerData {
    /**
     * 玩家对象
     */
    val player: Player
    /**
     * 玩家名称
     */
    val user: String

    /**
     * 玩家UID
     */
    val uuid: UUID

    /**
     * 扩展背包物品列表
     */
    val itemsData: MutableList<ItemStack>

    /**
     * 背包物品序列化字符
     */
    var itemString: String

    /**
     * 获取玩家背包页面大小
     */
    fun getBagPageSize(): Int

    /**
     * 数据序列化
     */
    fun toByteArray(): ByteArray
}