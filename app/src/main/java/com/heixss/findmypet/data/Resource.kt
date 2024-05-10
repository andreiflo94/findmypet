package com.heixss.findmypet.data

class Resource<T> private constructor(val status: Status, val data: T?, val throwable: Throwable?) {

    enum class Status {
        SUCCESS, ERROR
    }

    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data, null)

        fun <T> error(error: Throwable? = null): Resource<T> = Resource(Status.ERROR, null, error)
        fun <T> error(): Resource<T> = Resource(Status.ERROR, null, null)
    }
}
