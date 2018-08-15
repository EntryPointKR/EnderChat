package com.github.entrypointkr.enderchat.network

/**
 * Created by JunHyeong on 2018-08-09
 */
interface Writable {
    fun write(buffer: PacketBuffer)
}
