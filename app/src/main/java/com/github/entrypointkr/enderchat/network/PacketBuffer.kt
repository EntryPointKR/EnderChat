package com.github.entrypointkr.enderchat.network

import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.charset.Charset
import kotlin.experimental.and

/**
 * Created by JunHyeong on 2018-08-09
 */
class PacketBuffer(val handle: ByteBuffer) {
    fun getString(): String {
        val length = getVarInt()
        val bytes = ByteArray(length)
        handle.get(bytes)
        return String(bytes, Charset.forName("UTF-8"))
    }

    fun putString(str: String) {
        putVarInt(str.length)
        handle.put(str.toByteArray(Charset.forName("UTF-8")))
    }

    fun getVarInt(): Int {
        var i = 0
        var j = 0
        while (true) {
            val b0 = handle.get()
            i = i or ((b0 and 127).toInt() shl (j++ * 7))
            if (j > 5) {
                throw RuntimeException("VarInt too big")
            }
            if (b0 and 128.toByte() != 128.toByte()) {
                break
            }
        }
        return i
    }

    fun putVarInt(num: Int) {
        var ret = num
        while (ret and -128 != 0) {
            handle.put((ret and 127 or 128).toByte())
            ret = ret ushr 7
        }
        handle.put(ret.toByte())
    }

    fun put(buffer: PacketBuffer) {
        val size = buffer.handle.position()
        val array = buffer.handle.array()
        handle.put(array, 0, size)
    }

    fun resetPosition() {
        handle.clear()
    }

    fun position() : Int {
        return handle.position()
    }

    fun setPosition(pos: Int) : Buffer {
        return handle.position(pos)
    }
}
