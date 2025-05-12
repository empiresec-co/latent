package co.empiresec.possibilities.nip01Core.relay

enum class RelayState {
    // Websocket connected
    CONNECTED,

    // Websocket disconnecting
    DISCONNECTING,

    // Websocket disconnected
    DISCONNECTED,
}
