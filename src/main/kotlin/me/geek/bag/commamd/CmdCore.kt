package me.geek.bag.commamd



import me.geek.bag.GeekPlayerBag
import me.geek.bag.SetTings
import me.geek.bag.api.DataManager
import me.geek.bag.api.DataManager.getData
import me.geek.bag.menu.Menu
import me.geek.bag.menu.Menu.getMenu
import me.geek.bag.menu.Menu.openMenu
import me.geek.bag.menu.action.AdminMenu
import me.geek.bag.menu.action.AdminMenu2
import org.bukkit.Bukkit

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import taboolib.common.platform.command.*
import taboolib.common.platform.function.adaptCommandSender


import taboolib.module.chat.TellrawJson
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang


@CommandHeader(name = "GeekPlayerBag", aliases = ["gekBag", "bag"], permissionDefault = PermissionDefault.TRUE )
object CmdCore {





    @CommandBody
    val main = mainCommand {
        execute { sender, _, _ ->
            createHelp(sender)
        }
    }

    @CommandBody
    val open = subCommand {
        execute<Player> { sender, _, _ ->
            sender.openMenu(sender.getMenu())
        }
    }

    @CommandBody(permission = "bag.command.admin")
    val adminOpen = subCommand {
        dynamic("目标玩家") {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            execute<Player> { sender, text, _ ->
                val args = text.args()
                if (args.size <= 1) AdminMenu(sender).build()
                else {
                    Bukkit.getPlayer(args[1])?.let {
                        AdminMenu2(sender, it.getData()).build()
                    } ?: sender.sendLang("cmd-adminOpen-deny", args[1])
                }
            }
        }
    }
    @CommandBody(permission = "bag.command.admin")
    val adminClear = subCommand {
        dynamic("目标玩家") {
            suggestion<CommandSender>(uncheck = false) { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            execute<Player> { sender, text, _ ->
                val args = text.args()
                Bukkit.getPlayer(args[1])?.getData()?.itemsData?.clear() ?: sender.sendLang("cmd-adminOpen-deny", args[1])
            }
        }
    }


    @CommandBody(permission = "bag.command.admin")
    val reload = subCommand {
        execute<CommandSender> { c, _, _ ->
            SetTings.onReload()
            DataManager.saveGlobalData()
            Menu.closeGui()
            Menu.loadMenu()
            if (c is Player) c.sendMessage("§8[§6GeekPlayerBag§8] §a配置已重载...")
            else GeekPlayerBag.say("§a配置已重载...")
        }
    }






    private fun createHelp(sender: CommandSender) {
        val s = adaptCommandSender(sender)
        s.sendMessage("")
        TellrawJson()
            .append("  ").append("§f§lGeekPlayerBag")
            .hoverText("§7轻量级扩展背包系统 By 老廖")
            .append(" ").append("§f${GeekPlayerBag.VERSION}")
            .hoverText("""
                §7插件版本: §f${GeekPlayerBag.VERSION}
            """.trimIndent()).sendTo(s)
        s.sendMessage("")
        s.sendMessage("  §7指令: §f/gekBag §8[...]")
        if (sender.hasPermission("bag.command.admin")) {
            s.sendLang("CMD-HELP-ADMIN")
        }
        s.sendLang("CMD-HELP-PLAYER")
    }
}