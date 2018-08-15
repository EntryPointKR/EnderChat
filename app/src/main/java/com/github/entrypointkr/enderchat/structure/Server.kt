package com.github.entrypointkr.enderchat.structure

/**
 * Created by JunHyeong on 2018-08-14
 */
val SERVERS = ArrayList<Server>()

class Server(
        private val name: String,
        private val addr: String,
        private val port: Int
) {

}
