package com.github.entrypointkr.enderchat.network

import com.github.entrypointkr.enderchat.network.packet.*
import org.json.JSONObject
import java.nio.ByteBuffer
import kotlin.reflect.KClass

/**
 * Created by JunHyeong on 2018-08-09
 */
object PacketCodec {
    private val writableRegistry: MutableMap<KClass<out Writable>, Int> = hashMapOf(
            HandshakingPacket::class to 0,
            StartPacket::class to 0
    )
    private val readableRegistry: MutableMap<Int, (PacketBuffer) -> Packet> = hashMapOf(
            0 to { buffer ->
                val str = buffer.getString()
                ServerInfoPacket(JSONObject(str))
            }
    )
    private val contextBuffer = ByteBuffer.allocateDirect(8192)

    fun registerWritable(type: KClass<out Writable>, index: Int) {
        writableRegistry[type] = index
    }

    fun registerReadable(index: Int, factory: (PacketBuffer) -> Packet) {
        readableRegistry[index] = factory
    }

    fun getVarIntSize(number: Int): Int {
        for (i in 1..4) {
            if (number and (-1 shl i * 7) == 0) {
                return i
            }
        }
        return 5
    }

    fun encode(writable: Writable): PacketBuffer {
        contextBuffer.clear()
        val buffer = PacketBuffer(contextBuffer)
        val id = writableRegistry[writable::class]!!
        buffer.putVarInt(id)
        writable.write(buffer)

        val size = buffer.handle.position()
        val tagSize = getVarIntSize(size)
        val encoded = PacketBuffer(ByteBuffer.allocate(size + tagSize))
        encoded.putVarInt(size)
        encoded.put(buffer)
        return encoded
    }

    fun decode(buffer: PacketBuffer): Packet? {
        val position = buffer.position()
        buffer.resetPosition()
        val size = buffer.getVarInt()
        val bufferSize = position - buffer.position()
        return if (bufferSize >= size) {
            val id = buffer.getVarInt()
            val factory = readableRegistry[id]
            factory?.invoke(buffer) ?: UnknownPacket
        } else {
            buffer.setPosition(position)
            null
        }
    }
}
