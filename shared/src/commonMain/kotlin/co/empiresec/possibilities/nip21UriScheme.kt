package co.empiresec.possibilities

import co.empiresec.possibilities.nip01Core.core.Event
import co.empiresec.possibilities.nip01Core.hints.EventHintBundle
import co.empiresec.possibilities.nip19Bech32.toNIP19

fun Event.toNostrUri(relayHint: String? = null): String = "nostr:${toNIP19(relayHint)}"

fun EventHintBundle<Event>.toNostrUri(): String = "nostr:${toNEvent()}"
