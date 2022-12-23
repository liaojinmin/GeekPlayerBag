package me.geek.bag.scheduler.task

import me.geek.bag.GeekPlayerBag
import me.geek.bag.SetTings
import me.geek.bag.api.DataManager
import me.geek.bag.scheduler.sql.actions
import me.geek.bag.scheduler.sql.use
import taboolib.common.platform.function.submitAsync
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2022/12/18
 *
 **/
class AutoSave {
    init {
        submitAsync(delay = SetTings.dataTask.delay.toLong() * 20, period = 300) {
            var res: IntArray = intArrayOf()
            if (DataManager.getPlayerDataList().isNotEmpty()) {
                measureTimeMillis {
                    DataManager.getConnection().use {
                        this.prepareStatement("UPDATE `player_data` SET `user`=?,`data`=?,`time`=? WHERE `uuid`=?;"
                        ).actions { p ->
                            val data = DataManager.getPlayerDataList().iterator()
                            while (data.hasNext()) {
                                val a = data.next()
                                p.setString(1, a.user)
                                p.setBytes(2, a.toByteArray())
                                p.setString(3, System.currentTimeMillis().toString())
                                p.setString(4, a.uuid.toString())
                                p.addBatch()
                            }
                            res = p.executeBatch()
                        }
                    }
                }.also {
                    var data = 0
                    res.forEach { e ->
                        if (e >= 0) {
                            data+=1
                        }
                    }
                    GeekPlayerBag.debug("&7数据维护事务 &8| &f成功保存 $data 条玩家背包数据... §8(耗时 $it Ms)")
                }
            }
        }
    }
}