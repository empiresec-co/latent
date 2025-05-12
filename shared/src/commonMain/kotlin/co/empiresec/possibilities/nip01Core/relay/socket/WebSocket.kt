package co.empiresec.possibilities.nip01Core.relay.sockets

interface WebSocket {
    fun connect()

    fun cancel()

    fun send(msg: String): Boolean
}
