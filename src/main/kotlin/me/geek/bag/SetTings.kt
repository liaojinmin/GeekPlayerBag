package me.geek.bag

import me.geek.bag.scheduler.sql.SqlConfig
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration.Companion.getObject

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
@PlatformSide([Platform.BUKKIT])
object SetTings {
    @Config(value = "settings.yml", autoReload = true)
    lateinit var config: ConfigFile
        private set

    @Awake(LifeCycle.ENABLE)
    fun init() {
        config.onReload { onLoadSetTings() }
    }


    var debug: Boolean = false
        private set
    private var ConfigVersion: Double = 1.0

    lateinit var sqlConfig: SqlConfig
    private set

    lateinit var bagPageData: BagPageData
    private set


    fun onReload() {
        debug = config.getBoolean("debug", false)
        bagPageData = config.getObject<BagPageData>("bagpagesize", false).also {
            it.permGroup.sortByDescending { bag -> bag.priority }
        }
    }
    fun onLoadSetTings() {
        debug = config.getBoolean("debug", false)
        ConfigVersion = config.getDouble("ConfigVersion")
        sqlConfig = config.getObject<SqlConfig>("data_storage", false).apply {
            sqlite = GeekPlayerBag.instance.dataFolder
        }
        bagPageData = config.getObject<BagPageData>("bagpagesize", false).also {
            it.permGroup.sortByDescending { bag -> bag.priority }
        }
    }

    data class BagPageData(
        val defaultSize: Int = 1,
        val permGroup: MutableList<PageData> = mutableListOf()
    )
    data class PageData(
        val priority: Int = 0,
        val perm: String = "",
        val size: Int = 1,
    )
}