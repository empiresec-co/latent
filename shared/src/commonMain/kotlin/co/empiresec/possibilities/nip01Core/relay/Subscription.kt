package co.empiresec.possibilities.nip01Core.relay

import co.empiresec.possibilities.nip01Core.relay.filters.Filter
import java.util.UUID

class Subscription(
    val id: String = UUID.randomUUID().toString().substring(0, 4),
    val filters: List<Filter>,
)
