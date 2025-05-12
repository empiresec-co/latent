package co.empiresec.possibilities.nip01Core

typealias Tag = Array<String>

/**
 * Returns if the Tag has at least $index elements.
 */
fun Tag.has(index: Int) = size > index

fun Tag.name() = this[0]

fun Tag.value() = this[1]

fun Tag.hasValue() = if (has(1)) this[1].isNotEmpty() else false

fun Tag.nameOrNull() = if (has(0)) this[0] else null

fun Tag.valueOrNull() = if (has(1)) this[1] else null
