package co.empiresec.possibilities.utils

public fun arrayOfNotNull(vararg elements: String?) = removeTrailingNullsAndEmptyOthers(*elements)

public fun removeTrailingNullsAndEmptyOthers(vararg elements: String?): Array<String> {
    val lastNonNullIndex = elements.indexOfLast { it != null }

    if (lastNonNullIndex < 0) return Array(0) { "" }

    return Array(lastNonNullIndex + 1) { index ->
        elements[index] ?: ""
    }
}

fun Array<String>.startsWith(startsWith: Array<String>): Boolean {
    if (startsWith.size > this.size) return false
    for (tagIdx in startsWith.indices) {
        if (startsWith[tagIdx] != this[tagIdx]) return false
    }
    return true
}

public inline fun <T, R> Array<out T>.lastNotNullOfOrNull(transform: (T) -> R?): R? {
    for (index in this.indices.reversed()) {
        val result = transform(this[index])
        if (result != null) {
            return result
        }
    }
    return null
}
