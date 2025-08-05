package dev.dmayr.chatapplication.data.datasource.websocket

import com.google.gson.Gson
import dev.dmayr.chatapplication.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor(
    private val gson: Gson
) {
    private var webSocket: WebSocketClient? = null
    private val messageChannel = Channel<String>(Channel.UNLIMITED)
    private val _connectionStatus = MutableStateFlow(false)

    val messages: Flow<String> = messageChannel.receiveAsFlow()
    val connectionStatus = _connectionStatus.asStateFlow()

    fun connect() {
        try {
            val uri = URI(BuildConfig.WEBSOCKET_URL)
            webSocket = object : WebSocketClient(uri) {
                override fun onOpen(handshake: ServerHandshake?) {
                    _connectionStatus.value = true
                }

                override fun onMessage(message: String?) {
                    message?.let {
                        // echo.websocket.org echoes back what we send
                        // Parse the message and broadcast to other connected clients
                        messageChannel.trySend(it)
                    }
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    _connectionStatus.value = false
                }

                override fun onError(ex: Exception?) {
                    _connectionStatus.value = false
                }
            }
            webSocket?.connect()
        } catch (e: Exception) {
            _connectionStatus.value = false
        }
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close()
        _connectionStatus.value = false
    }
}
