package me.geek.bag.menu.action

import me.geek.bag.api.PlayerData
import me.geek.bag.menu.MenuBase
import me.geek.bag.menu.sub.Type
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * 作者: 老廖
 * 时间: 2022/12/18
 *
 **/
class AdminMenu2(
    override val player: Player,
    private val target: PlayerData
): MenuBase() {
    private val layout: String =
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "mmmmmmmmm" +
        "<       >"

    override val inventory: Inventory = Bukkit.createInventory(this.player, 54, "§6G §0- §f${target.user} §7的扩展背包")
    override fun build(): MenuBase {
        // 锁定玩家
        target.isLook = true
        target.player.closeInventory()

        addItems {
            if (target.itemsData.isNotEmpty()) {
                var dataSize = target.itemsData.size
                var items = this.inventory.contents
                while (dataSize > 0) {
                    layout.forEachIndexed { index, c ->
                        if (c != ' ') {
                            when (c) {
                                '>' -> items[index] = ItemStack(Material.IRON_DOOR).apply {
                                    itemMeta = itemMeta?.also {
                                        it.setDisplayName("§7下一页")
                                    }
                                }
                                '<' -> items[index] = ItemStack(Material.IRON_DOOR).apply {
                                    itemMeta = itemMeta?.also {
                                        it.setDisplayName("§7上一页")
                                    }
                                }
                                else -> {
                                    if (dataSize > 0) {
                                        val itemStack = target.itemsData[target.itemsData.size - dataSize]
                                        items[index] = itemStack
                                        dataSize--
                                    }
                                }
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
            when (it) {
                '<' -> {
                    event.isCancelled = true
                    if (this.page != 0) {
                        // 将当前页面的更改保存
                        this.contents[page] = event.inventory.contents
                        // 上跳页面
                        this.page -= 1
                        this.inventory.contents = this.contents[page]
                    }
                    return
                }
                '>' -> {
                    event.isCancelled = true
                    if (this.contents.size > this.page + 1 ) {
                        // 将当前页面的更改保存
                        this.contents[page] = event.inventory.contents
                        // 下跳页面
                        this.page += 1
                        this.inventory.contents = this.contents[page]
                    }
                    return
                }
                else -> {}
            }
        }

    }
    private fun PlayerData.filter(items: Array<ItemStack>, newItems: MutableList<ItemStack>) {
        items.forEachIndexed { index, itemStack ->
            if (itemStack != null) {
                // 通过索引寻找图标字符
                layout[index].let {
                    if (!listOf(' ', '>', '<').contains(it)) {
                        if (itemStack.type != Material.AIR) {
                            newItems.add(itemStack)
                        }
                    }
                }
            }
        }
    }
    override fun onClose(event: InventoryCloseEvent) {
        val newItems = mutableListOf<ItemStack>()

        if (contents.size == 0) target.filter(event.inventory.contents, newItems)
        // 遍历分页
        contents.forEachIndexed { page, value ->
            if (page != this.page) {
                target.filter(value, newItems)
            } else target.filter(event.inventory.contents, newItems)
        }
        target.upBagItems(newItems) // 同步更新数据
        // 解锁玩家
        target.isLook = false
    }

}