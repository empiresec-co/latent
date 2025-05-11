package co.empiresec.latent.jackson

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import co.empiresec.possiblities.nip01Core.HexKey

class EventManualSerializer {
    companion object {
        private fun assemble(
            id: HexKey,
            pubKey: HexKey,
            createdAt: Long,
            kind: Int,
            tags: Array<Array<String>>,
            content: String,
            sig: String,
        ): ObjectNode {
            val factory = JsonNodeFactory.instance

            return factory.objectNode().apply {
                put("id", id)
                put("pubkey", pubKey)
                put("created_at", createdAt)
                put("kind", kind)
                replace(
                    "tags",
                    factory.arrayNode(tags.size).apply {
                        tags.forEach { tag ->
                            add(
                                factory.arrayNode(tag.size).apply { tag.forEach { add(it) } },
                            )
                        }
                    },
                )
                put("content", content)
                put("sig", sig)
            }
        }

        fun toJson(
            id: HexKey,
            pubKey: HexKey,
            createdAt: Long,
            kind: Int,
            tags: Array<Array<String>>,
            content: String,
            sig: String,
        ): String {
            val obj = assemble(id, pubKey, createdAt, kind, tags, content, sig)
            return EventMapper.mapper.writeValueAsString(obj)
        }

        /**
         * For debug purposes only
         */
        fun toPrettyJson(
            id: HexKey,
            pubKey: HexKey,
            createdAt: Long,
            kind: Int,
            tags: Array<Array<String>>,
            content: String,
            sig: String,
        ): String {
            val obj = assemble(id, pubKey, createdAt, kind, tags, content, sig)
            return EventMapper.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
        }
    }
}
