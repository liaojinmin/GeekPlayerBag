package me.geek.bag.menu

import me.geek.bag.api.PlayerDataBase
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


/**
 * 作者: 老廖
 * 时间: 2022/12/11
 *
 **/
object Menu {
    private val isOpen: MutableList<Player> = ArrayList()

    fun closeGui() {
        Bukkit.getOnlinePlayers().forEach { player: Player ->
            if (isOpen.contains(player)) {
                player.closeInventory()
            }
        }
    }
    fun Player.openBag(dataBase: PlayerDataBase) {
        isOpen.add(this)
        playSound(location, Sound.UI_BUTTON_CLICK, 1f, 2f)

    }


}