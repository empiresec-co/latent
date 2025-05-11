package co.empiresec.latent.jackson

import com.fasterxml.jackson.databind.JsonNode

inline fun <reified R> JsonNode.toTypedArray(transform: (JsonNode) -> R): Array<R> = Array(size()) { transform(get(it)) }
