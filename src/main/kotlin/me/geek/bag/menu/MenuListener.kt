package me.geek.bag.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * 作者: 老廖
 * 时间: 2022/12/11
 * 此 MENU 事件处理，借鉴 TabooLib
 **/
object MenuListener {
    @SubscribeEvent
    fun onOpen(e: InventoryOpenEvent) {

    }

    @SubscribeEvent
    fun onClick(e: InventoryClickEvent) {

        // 锁定主手
        if (e.rawSlot - e.inventory.size - 27 == e.whoClicked.inventory.heldItemSlot || e.click == org.bukkit.event.inventory.ClickType.NUMBER_KEY && e.hotbarButton == e.whoClicked.inventory.heldItemSlot) {
            e.isCancelled = true
        }

    }
    @SubscribeEvent
    fun onDrag(e: InventoryDragEvent) {

    }

    @SubscribeEvent
    fun onClose(e: InventoryCloseEvent) {

    }

}