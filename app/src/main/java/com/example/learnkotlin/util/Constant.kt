package com.example.learnkotlin.util

object EndPoint {
    const val LOGIN = "api/learn-kotlin-login"
    const val REGISTER = "api/learn-kotlin-register"

    const val SET_INPUT_MATERI = "api/learn-kotlin-set-input-materi"
    const val SET_UPDATE_MATERI = "api/learn-kotlin-update-materi"
    const val DELETE_MATERI = "api/learn-kotlin-delete-materi"
    const val GET_DETAIL_INPUT_MATERI = "api/learn-kotlin-get-detail-materi"
    const val GET_ALL_INPUT_MATERI = "api/learn-kotlin-get-all-input-materi"
    const val GET_INPUT_MATERI_BY_ID = "api/learn-kotlin-get-input-materi-by-id"

    const val SET_INPUT_KUIS = "api/learn-kotlin-set-input-kuis"
    const val SET_UPDATE_KUIS = "api/learn-kotlin-update-kuis"
    const val DELETE_KUIS = "api/learn-kotlin-delete-kuis"
    const val GET_DETAIL_INPUT_KUIS = "api/learn-kotlin-get-detail-kuis"
    const val GET_ALL_INPUT_KUIS = "api/learn-kotlin-get-all-input-kuis"
    const val GET_INPUT_KUIS_BY_ID = "api/learn-kotlin-get-input-kuis-by-id"
}

object SESSION {
    const val ID = "ID"
    const val IDFRAGMENT = "IDFRAGMENT"
    const val EDITMATERI = "EDITMATERI"
    const val EDITKUIS = "EDITKUIS"
    const val EMAIL = "EMAIL"
    const val NAME = "NAME"
    const val STATUS = "STATUS"
    const val IMAGE = "IMAGE"
    const val NUMBER_PHONE = "NUMBER_PHONE"
    const val ACCESS_TOKEN = "ACCESS_TOKEN"
}

object AUTH {
    const val AUTH_HEADER = "Authorization"
}

object MESSAGE {
    const val STATUS_SUCCESS = "success"
    const val STATUS_ERROR = "error"
}

object AUTH_STATUS {
    const val USER = "User"
    const val ADMIN = "Admin"
}

object URL {
    const val BASE_URL = "https://45a4-2404-8000-1022-34a4-48d4-5d80-3fd6-b743.ap.ngrok.io"
}