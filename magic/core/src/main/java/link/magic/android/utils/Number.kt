package link.magic.android.utils

internal class Number {
    fun generateRandomId(): Long {
        val rightLimit = 10000L
        val leftLimit = 1L
        return leftLimit + (Math.random() * (rightLimit - leftLimit)).toLong()
    }
}
