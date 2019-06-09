package com.github.mkdika.trafimonserver.helper

class HttpRequestHelper {

    companion object {

        fun encodeString(inputStr: String) : String {
            val encodedStr = inputStr
                .replace(" ","%20")
                .replace("\"","%22")
                .replace(",","%2C")
                .replace("<","%3C")
                .replace(">","%3E")
                .replace("#","%23")
                .replace("%","%25")
                .replace("|","%7C")
            return encodedStr
        }
    }
}