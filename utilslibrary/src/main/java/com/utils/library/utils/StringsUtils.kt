package com.utils.library.utils

/**
 * Created by cuiyang on 2017/10/19.
 */
fun String?.isEquals(s2: String?): Boolean {
    if (this == null) {
        return if (s2 == null) true else s2 == this
    } else {
        return this == s2
    }
}

fun String?.isEmptyOrNull(): Boolean {
    return this == null || this.trim { it <= ' ' }.isEmpty() || "null".equals(this.trim { it <= ' ' }, ignoreCase = true)
}

fun String?.isNotEmptyStr(): Boolean {
    return !(this.isEmptyOrNull())
}

fun String?.getString(defaultValue: String): String {
    return if (this.isEmptyOrNull()) defaultValue else this.toString()
}
