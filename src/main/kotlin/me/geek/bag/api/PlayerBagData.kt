package me.geek.bag.api

import com.google.gson.annotations.Expose
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
data class PlayerBagData(
    @Expose
    override val player: Player,

    @Expose
    override val itemsData: MutableList<ItemStack> = mutableListOf(),
) : PlayerDataBase() {

    override val user: String = player.displayName

    override val uuid: UUID = player.uniqueId

}
