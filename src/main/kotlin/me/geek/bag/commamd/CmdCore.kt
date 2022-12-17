package me.geek.bag.commamd



import me.geek.bag.GeekPlayerBag
import me.geek.bag.SetTings
import me.geek.bag.api.DataManager
import me.geek.bag.api.DataManager.getData
import me.geek.bag.menu.Menu
import me.geek.bag.menu.Menu.getMenu
import me.geek.bag.menu.Menu.openMenu
import me.geek.bag.menu.action.AdminMenu
import org.bukkit.Bukkit

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import taboolib.common.platform.command.*
import taboolib.common.platform.function.adaptCommandSender


import taboolib.module.chat.TellrawJson
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
        execute<Player> { sender, _, _ ->
            AdminMenu(sender).build()
        }
    }


    @CommandBody(permission = "bag.command.admin")
    val reload = subCommand {
        execute<CommandSender> { _, _, _ ->
            SetTings.onReload()
            DataManager.saveGlobalData()
            Menu.closeGui()
            Menu.loadMenu()
        }
    }






    private fun createHelp(sender: CommandSender) {
        val s = adaptCommandSender(sender)
        s.sendMessage("")
        TellrawJson()
            .append("  ").append("§f§lGeekPlayerBag")
            .hoverText("§7现代化高级扩展背包系统 By 老廖")
            .append(" ").append("§f${GeekPlayerBag.VERSION}")
            .hoverText("""
                §7插件版本: §f${GeekPlayerBag.VERSION}
            """.trimIndent()).sendTo(s)
        s.sendMessage("")
        s.sendMessage("  §7指令: §f/gekBag §8[...]")
        if (sender.hasPermission("Bag.command.admin")) {
         //   s.sendLang("CMD-HELP-ADMIN")
        }
      //  s.sendLang("CMD-HELP-PLAYER")
    }
}