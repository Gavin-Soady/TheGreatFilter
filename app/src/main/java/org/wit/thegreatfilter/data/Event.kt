package org.wit.thegreatfilter.data

open class Event<out T>(private val content:T) {
    var hasBennHandled = false
        private set
    fun getContentOrNull(): T? {

        return if (hasBennHandled)
            null
        else{
                hasBennHandled = true
                content
            }
    }
}