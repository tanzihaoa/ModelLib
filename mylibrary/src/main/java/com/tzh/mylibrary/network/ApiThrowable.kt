package com.tzh.mylibrary.network

class ApiThrowable(val status: Int, message: String? = null, case: Throwable? = null) : Throwable(message, case) {
    companion object {
        /**
         * 默认错误
         */
        private const val HTTP_ERROR_DEFAULT = -1

        @JvmStatic
        fun newThrowable(content: String): ApiThrowable {
            return ApiThrowable(HTTP_ERROR_DEFAULT, content)
        }
    }
}