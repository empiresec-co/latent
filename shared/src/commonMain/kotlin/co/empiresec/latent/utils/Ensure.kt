package co.empiresec.latent.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun ensure(
    condition: Boolean,
    exit: () -> Nothing,
) {
    contract {
        returns() implies condition
    }
    if (!condition) exit()
}
