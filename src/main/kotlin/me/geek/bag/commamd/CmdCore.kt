package me.geek.bag.commamd



import me.geek.bag.GeekPlayerBag

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import taboolib.common.platform.command.*
import taboolib.common.platform.function.adaptCommandSender


import taboolib.module.chat.TellrawJson


@CommandHeader(name = "GeekPlayerBag", aliases = ["gekBag", "bag"], permissionDefault = PermissionDefault.TRUE )
object CmdCore {





    @CommandBody
    val main = mainCommand {
        execute { sender, _, _ ->
            createHelp(sender)
        }
    }
    @CommandBody(permission = "Bag.command.admin")
    val reload = subCommand {
        execute<CommandSender> { _, _, _ ->

        }
    }

    @CommandBody(permission = "Bag.command.admin")
    val test = subCommand {
        execute<Player> { sender, _, _ ->

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