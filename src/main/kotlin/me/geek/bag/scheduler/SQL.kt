package me.geek.bag.scheduler

import me.geek.bag.api.PlayerData
import me.geek.bag.api.PlayerDataBase
import org.bukkit.entity.Player
import taboolib.module.nms.nmsProxy

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
abstract class SQL {

    abstract fun insert(data: PlayerData)
    abstract fun select(player: Player, upEmpty: Boolean): PlayerData
    abstract fun update(data: PlayerData)
    abstract fun updateGlobal(data: List<PlayerData>)
    abstract fun getDefaultData(player: Player): PlayerData




    companion object {

        val INSTANCE by lazy {
            nmsProxy<SQL>()
        }
    }
}