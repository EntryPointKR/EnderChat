package com.github.entrypointkr.enderchat.network.packet

import com.github.entrypointkr.enderchat.network.PacketBuffer
import com.github.entrypointkr.enderchat.network.Writable

/**
 * Created by JunHyeong on 2018-08-09
 */
class StartPacket : Writable, Packet {
    override fun write(buffer: PacketBuffer) {
        // Nothing
    }
}
