package dev.dmayr.chatapplication.data.datasource.websocket

import dev.dmayr.chatapplication.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

sealed class WebSocketEvent {
    data class OnMessage(val text: String) : WebSocketEvent()
    object OnConnectionOpened : WebSocketEvent()
    object OnConnectionClosed : WebSocketEvent()
    data class OnConnectionFailed(val throwable: Throwable, val response: Response?) :
        WebSocketEvent()

    object OnConnectionClosing : WebSocketEvent()
}

@Singleton
class WebSocketDataSource @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private var webSocket: WebSocket? = null
    private val _events = Channel<WebSocketEvent>(Channel.BUFFERED)
    val events: Flow<WebSocketEvent> = _events.receiveAsFlow()

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            _events.trySend(WebSocketEvent.OnConnectionOpened)
            println("Conexión WebSocket abierta")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            _events.trySend(WebSocketEvent.OnMessage(text))
            println("Mensaje WebSocket recibido: $text")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            _events.trySend(WebSocketEvent.OnConnectionClosing)
            println("Cerrando conexión WebSocket: $code / $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            _events.trySend(WebSocketEvent.OnConnectionClosed)
            println("Conexión WebSocket cerrada: $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            _events.trySend(WebSocketEvent.OnConnectionFailed(t, response))
            println("Conexión WebSocket falló: ${t.localizedMessage}")
        }
    }

    fun connect() {
        val websocketURL =
            "${BuildConfig.WEBSOCKET_BASE_URL}?api_key=${BuildConfig.WEBSOCKET_API_KEY}&notify_self=1"
        val request = Request.Builder()
            .url(websocketURL)
            .build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener) // [55, 57, 58]
    }

    fun sendMessage(message: String) {
        webSocket?.send(message) // [58, 59]
    }

    fun disconnect() {
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Goodbye!") // [58, 59]
        webSocket = null
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
