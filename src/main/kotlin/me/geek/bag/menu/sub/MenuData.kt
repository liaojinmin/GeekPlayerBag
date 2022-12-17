package me.geek.bag.menu.sub

import org.bukkit.inventory.ItemStack

/**
 * 作者: 老廖
 * 时间: 2022/12/12
 *
 **/
data class MenuData(
    /**
     * 菜单文件名称
     */
    val fileName: String,
    /**
     * 菜单种类
     */
    val menuType: String,
    /**
     * 菜单标题
     */
    val title: String,
    /**
     * 菜单绑定的指令
     */
    val command: String,
    /**
     * 菜单字符布局
     */
    val layout: MutableList<Char>,
    /**
     * 菜单大小
     */
    val size: Int,
    /**
     * 菜单图标配置
     */
    val icon: MutableMap<Char, Icon>,
    /**
     * 菜单预构建物品
     */
    val items: Array<ItemStack>,

    /**
     * 菜单权限组
     */
    val group: MenuGroup
) {




    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MenuData

        if (menuType != other.menuType) return false
        if (fileName != other.fileName) return false
        if (title != other.title) return false
        if (layout != other.layout) return false
        if (size != other.size) return false
        if (icon != other.icon) return false
        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = menuType.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + layout.hashCode()
        result = 31 * result + size
        result = 31 * result + icon.hashCode()
        result = 31 * result + items.contentHashCode()
        return result
    }
}
