package me.geek.bag.utils

import com.google.gson.*
import com.google.gson.annotations.Expose
import me.geek.bag.api.PlayerBagData
import me.geek.bag.api.PlayerData
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Type
import java.util.*

/**
 * 作者: 老廖
 * 时间: 2022/12/10
 *
 **/
object ClassSerializable {
    private fun toJson(data: PlayerData): String {
        return GsonBuilder()
            .setExclusionStrategies(Exclude())
            .create().toJson(data)
    }
    fun gsonSerialize(data: PlayerData): ByteArray {
        return toJson(data).toByteArray(charset = Charsets.UTF_8)
    }
    fun gsonUnSerialize(objs: ByteArray): Any {
        val gson = GsonBuilder().setExclusionStrategies(Exclude())
        gson.registerTypeAdapter(PlayerData::class.java, UnSerializePlayerData())
        return gson.create().fromJson(String(objs, charset = Charsets.UTF_8), PlayerData::class.java)
    }
    class Exclude : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes): Boolean {
            return f.getAnnotation(Expose::class.java) != null
        }

        override fun shouldSkipClass(clazz: Class<*>): Boolean {
            return clazz.getAnnotation(Expose::class.java) != null
        }
    }
    class UnSerializePlayerData: JsonDeserializer<PlayerData> {
        override fun deserialize(json: JsonElement, p1: Type, p2: JsonDeserializationContext?): PlayerData {
            val jsonObject = json.asJsonObject
            val uuid = UUID.fromString(jsonObject.get("uuid").asString)
            val player = Bukkit.getPlayer(uuid)
            val item = mutableListOf<ItemStack>().apply {
                val b = jsonObject.get("itemString").asJsonArray
                if (b.size() != 0) {
                    b.forEach {
                        val a = it.asString.deserializeItemStack()
                        if (a != null) add(a)
                    }
                }
            }

            if (player == null) throw NullPlayerException()
            return PlayerBagData(player, item)
        }

    }
    class NullPlayerException() : IllegalArgumentException("未找到该玩家，请检查原因，如果持续发生错误，提供日志联系开发者！")

}