package co.empiresec.latent

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform