package com.github.entrypointkr.enderchat.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.entrypointkr.enderchat.R
import kotlinx.android.synthetic.main.activity_server_add.*

/**
 * Created by JunHyeong on 2018-08-13
 */
class ServerAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_add)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Server"

        text_name.onFocusChangeListener = EditorHintDisplayer(text_name_layout, "Minecraft Server")
        text_port.onFocusChangeListener = EditorHintDisplayer(text_port_layout, "25565")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.server_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                finish()
            }
            R.id.server_add_button -> {
                tryAddServer()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onAddButton(view: View) {
        tryAddServer()
    }

    fun tryAddServer() {
        if (text_address.text.isBlank()) {
            text_address_layout.error = "Please enter a server address."
        } else {
            text_address_layout.error = null
            val name = if (text_name.text.isBlank()) "Minecraft Server" else text_name.text.toString()
            val addr = text_address.text.toString()
            val port = text_port.text.toString().toIntOrNull() ?: 25565

            val intent = Intent()
            intent.putExtra("name", name)
            intent.putExtra("address", addr)
            intent.putExtra("port", port)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
