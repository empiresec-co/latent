package co.empiresec.possibilities.nip01Core.relay.filters

import co.empiresec.possibilities.nip01Core.core.Event

class Filter(
    val ids: List<String>? = null,
    val authors: List<String>? = null,
    val kinds: List<Int>? = null,
    val tags: Map<String, List<String>>? = null,
    val since: Long? = null,
    val until: Long? = null,
    val limit: Int? = null,
    val search: String? = null,
) {
    fun toJson() = FilterSerializer.toJson(ids, authors, kinds, tags, since, until, limit, search)

    fun match(event: Event) = FilterMatcher.match(event, ids, authors, kinds, tags, since, until)

    fun copy(
        ids: List<String>? = this.ids,
        authors: List<String>? = this.authors,
        kinds: List<Int>? = this.kinds,
        tags: Map<String, List<String>>? = this.tags,
        since: Long? = this.since,
        until: Long? = this.until,
        limit: Int? = this.limit,
        search: String? = this.search,
    ) = Filter(ids, authors, kinds, tags, since, until, limit, search)
}
