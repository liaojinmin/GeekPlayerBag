package me.geek.bag.menu


import org.bukkit.event.inventory.*

import taboolib.common.platform.event.SubscribeEvent

/**
 * 作者: 老廖
 * 时间: 2022/12/11
 * 此 MENU 事件处理，借鉴 TabooLib
 **/
object MenuListener {

    @SubscribeEvent
    fun onClick(e: InventoryClickEvent) {
        val menu = Menu.SessionCache[e.view.player] ?: return

        if (e.rawSlot < 0 || e.rawSlot >= menu.inventory.size) return

        // 锁定主手
        if (e.rawSlot - e.inventory.size - 27 == e.whoClicked.inventory.heldItemSlot
            || e.click == ClickType.NUMBER_KEY
            && e.hotbarButton == e.whoClicked.inventory.heldItemSlot) {
            e.isCancelled = true
        }
        menu.onClick(e)
    }

    @SubscribeEvent
    fun onClose(e: InventoryCloseEvent) {
        val menu = Menu.SessionCache[e.view.player] ?: return
        menu.onClose(e)
        Menu.SessionCache.remove(e.player)
        Menu.isOpen.remove(e.player)
    }

    /*
    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val message = e.message.removePrefix("/")
        if (message.isNotBlank()) {
            Menu.getMenu(message)?.let {
                e.isCancelled = true
                e.player.openMenu(it)
            }
        }
    }

     */

}