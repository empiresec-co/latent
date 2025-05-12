package co.empiresec.possibilities.nip94FileMetadata.tags

import co.empiresec.possibilities.nip01Core.HexKey
import co.empiresec.possibilities.nip01Core.has
import co.empiresec.latent.utils.arrayOfNotNull
import co.empiresec.latent.utils.ensure


class ImageTag(
    val imageUrl: String,
    val hash: HexKey?,
) {
    companion object {
        const val TAG_NAME = "image"

        @JvmStatic
        fun parse(tag: Array<String>): ImageTag? {
            ensure(tag.has(1)) { return null }
            ensure(tag[0] == TAG_NAME) { return null }
            ensure(tag[1].isNotEmpty()) { return null }
            return ImageTag(tag[1], tag.getOrNull(2))
        }

        @JvmStatic
        fun assemble(
            imageUrl: String,
            hash: String? = null,
        ) = arrayOfNotNull(TAG_NAME, imageUrl, hash)
    }
}
