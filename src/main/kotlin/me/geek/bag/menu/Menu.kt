package me.geek.bag.menu

import me.geek.bag.GeekPlayerBag
import me.geek.bag.api.DataManager.getData
import me.geek.bag.menu.action.PlayerBag
import me.geek.bag.menu.sub.Icon
import me.geek.bag.menu.sub.MenuData
import me.geek.bag.menu.sub.MenuGroup
import me.geek.bag.menu.sub.Type
import me.geek.bag.utils.colorify
import me.geek.bag.utils.forFile
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.releaseResourceFile
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration.Companion.getObject
import taboolib.module.configuration.SecuredFile
import taboolib.module.configuration.util.getMap
import taboolib.platform.util.sendLang
import java.io.File
import kotlin.system.measureTimeMillis


/**
 * 作者: 老廖
 * 时间: 2022/12/11
 *
 **/
object Menu {
    private val AIR = ItemStack(Material.AIR)

    // start 已打开的会话界面缓存
    val isOpen: MutableList<Player> = mutableListOf()
    val SessionCache: MutableMap<Player, MenuBase> = mutableMapOf()
    // end


    /*
    /**
     * key = 菜单绑定的命令(or 菜单名字) , value = 实列
     */
    private val MenuCache: MutableMap<String, MenuData> = mutableMapOf()
    fun getMenu(cmd: String): MenuData? {
        return MenuCache[cmd]
    }

     */
    /**
     * 菜单配置缓存
     */
    private val MenuCache: MutableList<MenuData> = mutableListOf()
    fun Player.getMenu(): MenuData {
        MenuCache.forEach {
            if (this.hasPermission(it.group.perm)) {
                return it
            } else if (it.group.default) {
                return it
            }
        }
        error("你的GUI配置错误，未设置默认UI")
    }



    fun closeGui() {
        Bukkit.getOnlinePlayers().forEach { player: Player ->
            if (isOpen.contains(player)) {
                player.closeInventory()
            }
        }
    }
    fun Player.openMenu(menuData: MenuData) {
        if (this.getData().isLook) {
            this.sendLang("player-bag-look")
            return
        }
        playSound(location, Sound.UI_BUTTON_CLICK, 1f, 2f)
        PlayerBag(this, menuData).build()
    }

    fun loadMenu() {
        val list = mutableListOf<File>()
        measureTimeMillis {
            MenuCache.clear()
            list.also {
                it.addAll(forFile(saveDefaultMenu))
            }
            list.forEach { file ->
                val icon = mutableListOf<Icon>()
                val menu: SecuredFile = SecuredFile.loadConfiguration(file)
                val menuTag: String = file.name.substring(0, file.name.indexOf("."))
                val title: String = menu.getString("title")!!.colorify()
                val type: String = menu.getString("type")!!
                val size: Int = menu.getStringList("layout").size * 9
                val layout: MutableList<Char> = mutableListOf<Char>().apply {
                    menu.getStringList("layout").forEach {
                        it.indices.forEach { index -> add(it[index]) }
                    }
                }
                val bindings: String = menu.getString("command") ?: ""
                menu.getMap<String, ConfigurationSection>("icon").forEach { (name, obj) -> icon.add(Icon(name[0], obj)) }

                val items = arrayListOf<ItemStack>()
                val listIcon: MutableMap<Char, Icon> = mutableMapOf<Char, Icon>().apply {
                    layout.forEachIndexed { _, value ->
                        if (value != ' ') {
                            icon.forEach { ic ->
                                if (ic.icon == value) {
                                    items.add(buildItems(ic))
                                    this[value] = ic
                                }
                            }
                        } else items.add(AIR)
                    }
                }
                val group = menu.getObject<MenuGroup>("group",false)
                MenuCache.add(MenuData(menuTag, type, title, bindings, layout, size, listIcon, items.toTypedArray(), group))
              //  MenuCache[menuTag] = MenuData(menuTag, type, title, bindings, layout, size, listIcon, items.toTypedArray(), group)
              //  MenuCache[bindings] = MenuData(menuTag, type, title, bindings, layout, size, listIcon, items.toTypedArray())
            }
        }.also {
            MenuCache.sortByDescending { menuData -> menuData.group.priority  }
            GeekPlayerBag.say("§7菜单界面加载完成... §8(耗时 $it ms)");
        }
    }
    private fun buildItems(icon: Icon): ItemStack {
        return when {
            icon.mats.contains(ia) ->  TODO()

            icon.type == Type.ITEMS -> AIR
            else -> {
                val itemStack = try {
                    ItemStack(Material.valueOf(icon.mats.uppercase()), 1, icon.data.toShort())
                } catch (ing: IllegalArgumentException) {
                    ItemStack(Material.STONE, 1)
                }
                val itemMeta = itemStack.itemMeta
                if (itemMeta != null) {
                    itemMeta.setDisplayName(icon.name.colorify())
                    if (icon.lore.size == 1 && icon.lore[0].isEmpty()) {
                        itemMeta.lore = null
                    } else {
                        itemMeta.lore = icon.lore
                    }
                    itemStack.itemMeta = itemMeta
                }
                itemStack
            }
        }
    }
    private val saveDefaultMenu by lazy {
        val dir = File(GeekPlayerBag.instance.dataFolder, "menu")
        if (!dir.exists()) {
            arrayOf(
                "menu/default.yml",
                "menu/vip.yml",
            ).forEach { releaseResourceFile(it, true) }
        }
        dir
    }

    private val ia = Regex("(IA|ia|ItemsAdder):")


}