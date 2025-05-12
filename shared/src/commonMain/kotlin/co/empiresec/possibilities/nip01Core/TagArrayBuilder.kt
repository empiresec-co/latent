package co.empiresec.possibilities.nip01Core

class TagArrayBuilder<T : IEvent> {
    /**
     * keeps a tag list by tag names to treat tags that must be unique
     */
    private val tagList = mutableMapOf<String, MutableList<Tag>>()

    fun remove(tagName: String): TagArrayBuilder<T> {
        tagList.remove(tagName)
        return this
    }

    fun remove(
        tagName: String,
        tagValue: String,
    ): TagArrayBuilder<T> {
        tagList[tagName]?.removeIf { it.valueOrNull() == tagValue }
        if (tagList[tagName]?.isEmpty() == true) {
            tagList.remove(tagName)
        }
        return this
    }

    fun removeIf(
        predicate: (Tag, Tag) -> Boolean,
        toCompare: Tag,
    ): TagArrayBuilder<T> {
        val tagName = toCompare.nameOrNull() ?: return this
        tagList[tagName]?.removeIf { predicate(it, toCompare) }
        if (tagList[tagName]?.isEmpty() == true) {
            tagList.remove(tagName)
        }
        return this
    }

    fun add(tag: Array<String>): TagArrayBuilder<T> {
        if (tag.isEmpty() || tag[0].isEmpty()) return this
        tagList.getOrPut(tag[0], ::mutableListOf).add(tag)
        return this
    }

    fun addUnique(tag: Array<String>): TagArrayBuilder<T> {
        if (tag.isEmpty() || tag[0].isEmpty()) return this
        tagList[tag[0]] = mutableListOf(tag)
        return this
    }

    fun addAll(tag: List<Array<String>>): TagArrayBuilder<T> {
        tag.forEach(::add)
        return this
    }

    fun addAll(tag: Array<Array<String>>): TagArrayBuilder<T> {
        tag.forEach(::add)
        return this
    }

    fun toTypedArray() = tagList.flatMap { it.value }.toTypedArray()

    fun build() = toTypedArray()
}

inline fun <T : Event> tagArray(initializer: TagArrayBuilder<T>.() -> Unit = {}): TagArray = TagArrayBuilder<T>().apply(initializer).build()
