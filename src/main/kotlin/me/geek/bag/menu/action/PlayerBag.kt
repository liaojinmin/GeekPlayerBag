package me.geek.bag.menu.action

import me.geek.bag.GeekPlayerBag
import me.geek.bag.api.DataManager.getData
import me.geek.bag.api.PlayerData
import me.geek.bag.menu.MenuBase
import me.geek.bag.menu.sub.MenuData
import me.geek.bag.menu.sub.Type.*
import org.bukkit.Material

import org.bukkit.entity.Player

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack


/**
 * 作者: 老廖
 * 时间: 2022/12/12
 *
 **/
class PlayerBag(
    override val player: Player,
    override val menuData: MenuData,
): MenuBase() {


    override fun onClick(event: InventoryClickEvent) {
        menuData.layout[event.rawSlot].let {
            if (it == ' ') event.isCancelled = true
            menuData.icon[it]?.let { icon ->
                when (icon.type) {
                    NORMAL -> {
                        event.isCancelled = true
                     //   player.sendMessage("NORMAL")
                        return
                    }
                    ITEMS -> {
                    //    player.sendMessage("ITEMS")
                        return
                    }
                    LAST_PAGE -> {
                        event.isCancelled = true
                        player.sound(icon.sound)
                        if (this.page != 0) {
                            // 将当前页面的更改保存
                            this.contents[page] = event.inventory.contents
                            // 上跳页面
                            this.page -= 1
                            this.inventory.contents = this.contents[page]
                        }
                        return
                    }
                    NEXT_PAGE -> {
                        event.isCancelled = true
                        player.sound(icon.sound)
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

    }

    override fun onClose(event: InventoryCloseEvent) {
        GeekPlayerBag.debug("onClose")
        val data = player.getData()

        data.itemsData.clear()
        if (contents.size == 0) itemFilter(event.inventory.contents, data)
        // 遍历分页
        contents.forEachIndexed { page, value ->
            if (page != this.page) {
                itemFilter(value, data)
            } else itemFilter(event.inventory.contents, data)
        }
    }
    private fun itemFilter(items: Array<ItemStack>, data: PlayerData) {
        items.forEachIndexed { index, itemStack ->
          //  if (itemStack != null) {
               // 通过索引寻找图标字符
                menuData.layout[index].let {
                   // 找到字符 判断图标种类
                    menuData.icon[it]?.let { ic ->
                        if (ic.type == ITEMS) {
                            data.itemsData.add(itemStack)
                        }
                    }
                }
          //  }
        }
    }


    override fun build(): MenuBase {
        addItems {
            val data = player.getData()
            if (data.itemsData.isNotEmpty()) {
                var itemSize = data.itemsData.size
                var item = this.inventory.contents
                while (itemSize > 0) {
                    menuData.layout.forEachIndexed { index, value ->
                        if (value != ' ') {
                            menuData.icon[value]?.let { icon ->
                                if (icon.type == ITEMS) {
                                    if (itemSize > 0) {
                                        val i = data.itemsData[data.itemsData.size - itemSize]
                                        item[index] = i
                                        itemSize--
                                    }
                                }
                            }
                        }
                    }
                    this.contents.add(item)
                    item = this.inventory.contents
                }
            }
            // 1 .. 3 = 3
            (contents.size+1..data.getBagPageSize()).forEach { _ ->
                contents.add(this.inventory.contents)
            }
            this.openMenu()
        }
        return this
    }


}