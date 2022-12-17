package me.geek.bag.menu

import me.geek.bag.GeekPlayerBag
import org.bukkit.Bukkit
import org.bukkit.entity.Player

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XSound
import java.util.ArrayList

/**
 * 作者: 老廖
 * 时间: 2022/12/12
 *
 **/
abstract class MenuBase: MenuHeader {

    abstract fun build(): MenuBase

    override var page = 0
    /**
     * 打开菜单
     */
    override fun openMenu() {
        Menu.SessionCache[this.player] = this
        Menu.isOpen.add(this.player)
        if (contents.size != 0) {
            this.inventory.contents = this.contents[0]
        }
        this.player.openInventory(this.inventory)
    }




    fun addItems(func: (MenuBase) -> Unit) {
        func(this)
    }

    val contents: MutableList<Array<ItemStack>> = ArrayList()

    val inventory: Inventory by lazy {

        this.menuData?.let {
            Bukkit.createInventory(this.player, it.size, it.title).apply {
                if (it.items.isNotEmpty()) {
                    this.contents = it.items
                }
            }
        } ?: Bukkit.createInventory(this.player, 54, "§6GeekPlayerBag §0- §7管理员界面")
    }


    fun Player.sound(s: Array<String>) {
        if (s.size < 3) throw Throwable("未知音效: ${s[0]}")

        val sound: XSound = try {
            XSound.valueOf(s[0])
        } catch (e: Throwable) {
            GeekPlayerBag.say("未知音效: ${s[0]}")
            return
        }
        sound.play(this, s[1].toFloat(), s[2].toFloat())
    }



}