package me.geek.bag

import me.geek.bag.api.DataManager
import me.geek.bag.menu.Menu
import me.geek.bag.utils.colorify
import org.bukkit.Bukkit
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.platform.BukkitPlugin

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
@RuntimeDependencies(
    RuntimeDependency(
        value = "!com.zaxxer:HikariCP:4.0.3",
        relocate = ["!com.zaxxer.hikari", "!com.zaxxer.hikari_4_0_3_bag"]),
)
@PlatformSide([Platform.BUKKIT])
object GeekPlayerBag: Plugin() {
    val instance by lazy { BukkitPlugin.getInstance() }
    const val VERSION = 1.0
    val BukkitVersion by lazy { Bukkit.getVersion().substringAfter("MC:").filter { it.isDigit() }.toInt() }


    override fun onLoad() {
        console().sendMessage("")
        console().sendMessage("正在加载 §3§lGeekPlayerBag §f...  §8" + Bukkit.getVersion())
        console().sendMessage("")
    }

    override fun onEnable() {
        runLogo()
        SetTings.onLoadSetTings()
        Menu.loadMenu()
        DataManager.start()
    }

    override fun onActive() {

    }

    override fun onDisable() {
        Menu.closeGui()
        DataManager.saveGlobalData() // 保存数据
        DataManager.closeData() // 关闭数据库

    }




    @JvmStatic
    fun say(msg: String) {
        if (BukkitVersion >= 1160)
            console().sendMessage("&8[<g#2:#FFB5C5:#EE0000>GeekPlayerBag&8] &7$msg".colorify())
        else
            console().sendMessage("§8[§6GeekPlayerBag§8] ${msg.replace("&", "§")}")
    }
    @JvmStatic
    fun debug(msg: String) {
        if(SetTings.debug) {
            if (BukkitVersion >= 1160)
                console().sendMessage("&8[<g#2:#FFB5C5:#EE0000>GeekPlayerBag&8] &cDeBug &8| &7$msg".colorify())
            else
                console().sendMessage("§8[§6GeekPlayerBag§8] ${msg.replace("&", "§")}")
        }
    }
    private fun runLogo() {
        console().sendMessage("")
        console().sendMessage("       §aGeekPlayerBag§8-§6Plus  §bv$VERSION §7by §awww.geekcraft.ink")
        console().sendMessage("       §8适用于Bukkit: §71.12.2-1.19.2 §8当前: §7 ${Bukkit.getServer().version}")
        console().sendMessage("")
    }
}