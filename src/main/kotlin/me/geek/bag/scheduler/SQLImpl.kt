package me.geek.bag.scheduler

import me.geek.bag.api.DataManager
import me.geek.bag.api.PlayerBagData
import me.geek.bag.api.PlayerData
import me.geek.bag.scheduler.sql.actions
import me.geek.bag.scheduler.sql.use
import me.geek.bag.utils.ClassSerializable
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
class SQLImpl: SQL() {
    private val manager by lazy { DataManager }

    override fun insert(data: PlayerData) {
        if (manager.isActive()) {
            submitAsync {
                manager.getConnection().use {
                    this.prepareStatement(
                        "INSERT INTO player_data(`uuid`,`user`,`data`,`time`) VALUES(?,?,?,?);"
                    ).actions { p ->
                        p.setString(1, data.uuid.toString())
                        p.setString(2, data.user)
                        p.setBytes(3, data.toByteArray())
                        p.setString(4, System.currentTimeMillis().toString())
                        p.execute()
                    }
                }
            }
        }
    }

    override fun select(player: Player, upEmpty: Boolean): PlayerData {
        var data: PlayerData? = null
        if (manager.isActive()) {
            manager.getConnection().use {
                this.prepareStatement(
                    "SELECT `data` FROM `player_data` WHERE uuid=?;"
                ).actions { p ->
                    p.setString(1, player.uniqueId.toString())
                    val res = p.executeQuery()
                    if (res.next()) {
                        val var10 = ClassSerializable.gsonUnSerialize(res.getBytes("data"))
                        if (var10 is PlayerData) data = var10
                    } else { data = getDefaultData(player).also { if (upEmpty) insert(it) } }
                }
            }
            return data!!
        } else throw SqlNotActiveException()
    }

    override fun update(data: PlayerData) {
        if (manager.isActive()) {
            manager.getConnection().use {
                this.prepareStatement(
                    "UPDATE `player_data` SET `user`=?,`data`=?,`time`=? WHERE `uuid`=?;"
                ).actions { p ->
                    p.setString(1, data.user)
                    p.setBytes(2, data.toByteArray())
                    p.setString(3, System.currentTimeMillis().toString())
                    p.setString(4, data.uuid.toString())
                    p.executeUpdate()
                }
            }
        }
    }

    override fun updateGlobal(data: List<PlayerData>) {
        if (manager.isActive()) {
            manager.getConnection().use {
                this.prepareStatement(
                    "UPDATE `player_data` SET `user`=?,`data`=?,`time`=? WHERE `uuid`=?;"
                ).actions { p ->
                    data.forEach {
                        p.setString(1, it.user)
                        p.setBytes(2, it.toByteArray())
                        p.setString(3, System.currentTimeMillis().toString())
                        p.setString(4, it.uuid.toString())
                        p.addBatch()
                    }
                    p.executeBatch()
                }
            }
        }
    }

    override fun getDefaultData(player: Player): PlayerData {
        return PlayerBagData(player)
    }

    class SqlNotActiveException : IllegalArgumentException("数据库连接错误，请检查数据库服务器以及配置...")

}