package com.wms.superadmin.network.resources

sealed class Response<out T : Any>

class SuccessResponse<out T : Any>(val data: T) : Response<T>() {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = data
}

class ErrorResponse(
    val message: String = "Something Went Wrong",
    val data: Exception? = null
) :
    Response<Nothing>() {


    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getExceptionIfNotHandled(): Exception? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }


}

class UnAuthResponse() : Response<Nothing>()