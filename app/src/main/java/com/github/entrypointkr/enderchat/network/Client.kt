package com.github.entrypointkr.enderchat.network

import com.github.entrypointkr.enderchat.network.packet.Packet
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SelectionKey.*
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.*

/**
 * Created by JunHyeong on 2018-08-06
 */
class Client(
        private val selector: Selector = Selector.open(),
        private val channel: SocketChannel = SocketChannel.open(),
        var listener: (Packet) -> Unit = {}
) {
    private val readBuffer: ByteBuffer = ByteBuffer.allocateDirect(8192)
    private val writableQueue: ArrayDeque<Writable> = ArrayDeque()

    fun connect(host: String, port: Int): Client {
        if (channel.connect(InetSocketAddress(host, port))) {
            channel.configureBlocking(false)
                    .register(selector, channel.validOps())
        }
        return this
    }

    fun write(writable: Writable): Client {
        writableQueue += writable
        channel.keyFor(selector)?.interestOps(OP_WRITE)
        return this
    }

    fun runMainLoop() {
        while (channel.isOpen) {
            selector.select()
            processSelector()
        }
    }

    private fun processSelector() {
        val iterator = selector.selectedKeys().iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            processKey(key)
            iterator.remove()
        }
    }

    private fun processKey(key: SelectionKey) {
        when (key.readyOps()) {
            OP_READ -> {
                val count = channel.read(readBuffer)
                if (count != -1) {
                    processRead()
                } else {
                    channel.close()
                }
            }
            OP_WRITE -> {
                processWritableQueue()
                key.interestOps(OP_READ)
            }
            OP_CONNECT -> {
                // Empty
            }
            OP_ACCEPT -> {
                // Empty
            }
        }
    }

    private fun processRead() {
        val readable = PacketCodec.decode(PacketBuffer(readBuffer))
        readable?.let {
            readBuffer.clear()
            listener.invoke(readable)
        }
    }

    private fun processWritableQueue() {
        while (!writableQueue.isEmpty()) {
            val writable = writableQueue.poll()
            val buffer = PacketCodec.encode(writable)
            buffer.resetPosition()
            channel.write(buffer.handle)
            println(writable::class.qualifiedName + " writed")
        }
    }
}
