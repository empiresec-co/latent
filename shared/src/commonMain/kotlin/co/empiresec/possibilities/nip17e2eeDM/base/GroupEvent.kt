package co.empiresec.possibilities.nip17e2eeDm.base

import androidx.compose.runtime.Immutable
import co.empiresec.possibilities.nip01Core.HexKey
import co.empiresec.possibilities.nip01Core.hints.PubKeyHintProvider
import co.empiresec.possibilities.nip01Core.tags.people.PTag
import co.empiresec.possibilities.nip59Giftwrap.WrappedEvent
import kotlinx.collections.immutable.toImmutableSet

@Immutable
open class BaseDMGroupEvent(
    id: HexKey,
    pubKey: HexKey,
    createdAt: Long,
    kind: Int,
    tags: Array<Array<String>>,
    content: String,
    sig: HexKey,
) : WrappedEvent(id, pubKey, createdAt, kind, tags, content, sig),
    ChatroomKeyable,
    NIP17Group,
    PubKeyHintProvider {
    override fun pubKeyHints() = tags.mapNotNull(PTag::parseAsHint)

    /** Recipients intended to receive this conversation */
    fun recipients() = tags.mapNotNull(PTag::parse)

    /** Recipients intended to receive this conversation */
    fun recipientsPubKey() = tags.mapNotNull(PTag::parseKey)

    fun talkingWith(oneSideHex: String): Set<HexKey> {
        val listedPubKeys = recipientsPubKey()

        val result =
            if (pubKey == oneSideHex) {
                listedPubKeys.toSet().minus(oneSideHex)
            } else {
                listedPubKeys.plus(pubKey).toSet().minus(oneSideHex)
            }

        if (result.isEmpty()) {
            // talking to myself
            return setOf(pubKey)
        }

        return result
    }

    override fun groupMembers() = recipientsPubKey().plus(pubKey).toSet()

    override fun chatroomKey(toRemove: String): ChatroomKey = ChatroomKey(talkingWith(toRemove).toImmutableSet())
}
