package com.github.entrypointkr.enderchat.network.packet

import org.json.JSONObject

/**
 * Created by JunHyeong on 2018-08-09
 */
class ServerInfoPacket(val json: JSONObject) : Packet {
    init {
        println(json.toString())
    }
}
