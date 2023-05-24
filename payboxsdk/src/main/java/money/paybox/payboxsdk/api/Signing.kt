package money.paybox.payboxsdk.api

import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

abstract class Signing {

    abstract var secretKey: String
    fun HashMap<String, String>.signedParams(url: String): SortedMap<String, String> {
        val paths = url.split("/")
        var sig = paths[paths.size - 1]
        this[Params.SALT] =
            java.lang.Long.toHexString(java.lang.Double.doubleToLongBits(Math.random()))
        val sorted = this.toSortedMap(Comparator { o1, o2 ->
            o1.compareTo(o2)
        })
        for (entry in sorted) {
            sig += ";${entry.value}"
        }
        sig += ";${secretKey}"
        sorted[Params.SIG] = sig.md5()
        return sorted
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}