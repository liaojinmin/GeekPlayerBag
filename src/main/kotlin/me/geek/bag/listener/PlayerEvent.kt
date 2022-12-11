package me.geek.bag.listener

import me.geek.bag.api.DataManager
import me.geek.bag.api.DataManager.save
import me.geek.bag.api.DataManager.select
import me.geek.bag.api.event.PlayerBagLoadEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * 作者: 老廖
 * 时间: 2022/12/11
 *
 **/
object PlayerEvent {

    @SubscribeEvent(priority = EventPriority.LOW, ignoreCancelled = true )
    fun onJoin(e: PlayerJoinEvent) {
        e.player.select(true)
    }
    @SubscribeEvent(priority = EventPriority.LOW, ignoreCancelled = true )
    fun onQuit(e: PlayerQuitEvent) {
        e.player.save(true)
    }

}