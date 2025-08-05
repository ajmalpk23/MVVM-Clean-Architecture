package com.example.test.data.network

enum class HttpStatusCode(val value: Int) {
    SUCCESS_ONE(200),
    SUCCESS_TWO(201),
    SUCCESS_THREE(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    UNPROCESSABLE_ENTITY(422),
    INTERNAL_SERVER_ERROR(500),
    TIMEOUT(504);

    companion object {

        fun fromCode(value: Int): HttpStatusCode? {
            return HttpStatusCode.entries.find { it.value == value }
        }
    }
}