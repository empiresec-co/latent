package cp.empiresec.connexio.services

import android.os.Looper
import co.empiresec.connexio.BuildConfig

fun checkNotInMainThread() {
    if (BuildConfig.DEBUG && isMainThread()) {
        throw OnMainThreadException("It should not be in the MainThread")
    }
}

fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()

class OnMainThreadException(
    str: String,
) : RuntimeException(str)
