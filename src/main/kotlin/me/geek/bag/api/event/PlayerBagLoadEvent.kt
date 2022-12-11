package me.geek.bag.api.event

import me.geek.bag.api.PlayerData
import taboolib.platform.type.BukkitProxyEvent

/**
 * 作者: 老廖
 * 时间: 2022/12/11
 *
 **/
class PlayerBagLoadEvent(val data: PlayerData) : BukkitProxyEvent()