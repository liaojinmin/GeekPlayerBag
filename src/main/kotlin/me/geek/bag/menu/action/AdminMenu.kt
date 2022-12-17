package me.geek.bag.menu.action

import me.geek.bag.api.DataManager
import me.geek.bag.menu.Menu
import me.geek.bag.menu.MenuBase
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

/**
 * 作者: 老廖
 * 时间: 2022/12/17
 *
 **/
class AdminMenu(
    override val player: Player,
): MenuBase() {
    private val fi = listOf(' ', '>', '<')
    private val layout: String =
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "<       >"
    override fun build(): MenuBase {
        addItems {
            val data = DataManager.getPlayerDataList()
            if (data.isNotEmpty()) {
                var dataSize = data.size
                var items = this.inventory.contents
                while (dataSize > 0) {
                    layout.forEachIndexed { index, c ->
                        if (!fi.contains(c)) {
                            if (dataSize > 0) {
                                val player = data[data.size - dataSize]
                                items[index] = ItemStack(Material.PLAYER_HEAD).apply {
                                    itemMeta = itemMeta?.also {
                                        it.setDisplayName("§f${player.user} §7的扩展背包")
                                        it.lore = listOf(
                                            "",
                                            "§8| §7UUID: §f${player.uuid}",
                                            "§8| §7物品大小: §a${player.itemsData.size}",
                                            "",
                                            "§8| §7状态: §a在线 §8(暂未支持离线)",
                                            "",
                                            "§8[§a鼠标点击§8] §7- §F查看背包"
                                        )
                                    }
                                }
                                dataSize--
                            }
                        }
                    }
                    this.contents.add(items)
                    items = this.inventory.contents
                }
            }
            this.openMenu()

        }
        return this
    }


    override fun onClick(event: InventoryClickEvent) {
        layout[event.rawSlot].let {
            if (it == ' ') return
            when (it) {
                '<' -> {
                    event.isCancelled = true
                    if (this.page != 0) {
                        // 将当前页面的更改保存
                        this.contents[page] = event.inventory.contents
                        // 上跳页面
                        this.page -= 1
                        this.inventory.contents = this.contents[page]
                        return
                    }
                }
                '>' -> {
                    if (this.contents.size > this.page + 1 ) {
                        // 将当前页面的更改保存
                        this.contents[page] = event.inventory.contents
                        // 下跳页面
                        this.page += 1
                        this.inventory.contents = this.contents[page]
                    }
                    return
                }
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent) {

    }
}