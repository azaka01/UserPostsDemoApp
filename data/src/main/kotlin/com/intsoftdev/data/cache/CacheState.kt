package com.intsoftdev.data.cache

sealed class CacheState {
    object Empty : CacheState() // nothing in the cache
    object Stale : CacheState() // the cache time expiry period has ended
    object Usable : CacheState() // neither of the above states
}