package me.geek.bag.api

import me.geek.bag.SetTings
import me.geek.bag.api.DataManager.save
import me.geek.bag.api.event.PlayerBagLoadEvent
import me.geek.bag.scheduler.SQLImpl
import me.geek.bag.scheduler.sql.Mysql
import me.geek.bag.scheduler.sql.Sqlite
import me.geek.bag.scheduler.sql.action
import me.geek.bag.scheduler.sql.use
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync
import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
object DataManager {

    /**
     * 玩家在线缓存
     */
    private val PlayerCache: MutableMap<UUID, PlayerData> = ConcurrentHashMap()

    fun getPlayerDataList() = PlayerCache.map { it.value }

    private val SqlImpl: SQLImpl = SQLImpl()

    fun saveGlobalData() = SqlImpl.updateGlobal(getPlayerDataList())


    /**
     * 获取玩家数据
     */
    fun Player.getData(): PlayerData {
       return PlayerCache[this.uniqueId] ?: SqlImpl.select(this, true)
    }

    /**
     * 查询玩家数据
     */
    fun Player.select(isAsync: Boolean = false) {
        if (isAsync)
            submitAsync {
                SqlImpl.select(this@select, true).also {
                    val eve = PlayerBagLoadEvent(it)
                    eve.call()
                    if (!eve.isCancelled) {
                        PlayerCache[this@select.uniqueId] = it
                    }
                }
            }
        else SqlImpl.select(this, true).also {
            val eve = PlayerBagLoadEvent(it)
            eve.call()
            if (!eve.isCancelled) {
                PlayerCache[this@select.uniqueId] = it
            }
        }
    }

    /**
     * 保存玩家数据
     */
    fun Player.save(isAsync: Boolean = false) {
        if (isAsync) submitAsync {
            PlayerCache[this@save.uniqueId]?.let { SqlImpl.update(it) }
            PlayerCache.remove(this@save.uniqueId)
        }
        else {
            PlayerCache[this.uniqueId]?.let { SqlImpl.update(it) }
            PlayerCache.remove(this.uniqueId)
        }
    }



        private val dataSub by lazy {
            if (SetTings.sqlConfig.use_type.equals("mysql", ignoreCase = true)){
                return@lazy Mysql(SetTings.sqlConfig)
            } else return@lazy Sqlite(SetTings.sqlConfig)
        }


        fun isActive(): Boolean = dataSub.isActive

        fun getConnection(): Connection {
            return dataSub.getConnection()
        }
        fun closeData() {
            dataSub.onClose()
        }
        fun start() {
            dataSub.onStart()
            if (dataSub.isActive) {
                dataSub.createTab {
                    getConnection().use {
                        createStatement().action { statement ->
                            if (dataSub is Mysql) {
                                statement.addBatch(SqlTab.MYSQL_1.tab)
                            } else {
                                statement.addBatch("PRAGMA foreign_keys = ON;")
                                statement.addBatch("PRAGMA encoding = 'UTF-8';")
                                statement.addBatch(SqlTab.SQLITE_1.tab)
                            }
                            statement.executeBatch()
                        }
                    }
                }
            }
        }

        enum class SqlTab(val tab: String) {
            MYSQL_1("CREATE TABLE IF NOT EXISTS `player_data` (" +
                    " `uuid` CHAR(36) NOT NULL UNIQUE, " +
                    " `user` varchar(16) NOT NULL UNIQUE," +
                    " `data` longblob NOT NULL," +
                    " `time` BIGINT(20) NOT NULL," +
                    "PRIMARY KEY (`uuid`, `user`)" +
                    ");"),
            SQLITE_1("CREATE TABLE IF NOT EXISTS `player_data` (" +
                    " `uuid` CHAR(36) NOT NULL UNIQUE PRIMARY KEY, " +
                    " `user` varchar(16) NOT NULL UNIQUE," +
                    " `data` longblob NOT NULL," +
                    " `time` BIGINT(20) NOT NULL" +
                    ");"),
        }
}