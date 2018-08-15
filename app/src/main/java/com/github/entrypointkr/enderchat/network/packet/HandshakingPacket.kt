package com.github.entrypointkr.enderchat.network.packet

import com.github.entrypointkr.enderchat.network.PacketBuffer
import com.github.entrypointkr.enderchat.network.ProtocolVersion
import com.github.entrypointkr.enderchat.network.Writable

/**
 * Created by JunHyeong on 2018-08-09
 */
class HandshakingPacket(
        private val version: ProtocolVersion,
        private val host: String,
        private val port: Int,
        private val state: Type
) : Writable, Packet {
    override fun write(buffer: PacketBuffer) {
        buffer.putVarInt(version.protocolNumber)
        buffer.putString(host)
        buffer.handle.putShort(port.toShort())
        buffer.putVarInt(state.data)
    }

    enum class Type(val data: Int) {
        STATUS(1),
        LOGIN(2)
    }
}
