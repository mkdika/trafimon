package com.github.mkdika.trafimonserver.helper

sealed class TrafiException(message: String?) : Exception(message) {

    class RecordNotFoundException(message: String = "Trafi record not found error.") : TrafiException(message)
}