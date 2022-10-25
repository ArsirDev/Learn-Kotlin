package com.example.learnkotlin.util

import android.content.Context
import android.content.SharedPreferences
import com.example.learnkotlin.util.SESSION.ACCESS_TOKEN
import javax.inject.Inject
import com.example.learnkotlin.util.SESSION.EMAIL
import com.example.learnkotlin.util.SESSION.IMAGE
import com.example.learnkotlin.util.SESSION.NAME
import com.example.learnkotlin.util.SESSION.NUMBER_PHONE
import com.example.learnkotlin.util.SESSION.STATUS

class SessionManager @Inject constructor(
    context: Context
) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    private var PRIVATE_MODE = 0

    fun createAuthSession(
        nama: String?,
        email: String?,
        status: String?,
        image: String?,
        number_phone: String?,
        accessToken: String?
    ) {
        editor.putString(EMAIL, email)
        editor.putString(NAME, nama)
        editor.putString(STATUS, status)
        editor.putString(IMAGE, image)
        editor.putString(NUMBER_PHONE, number_phone)
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.commit()
    }

    fun logout() {
        editor.remove(NAME)
        editor.remove(EMAIL)
        editor.remove(STATUS)
        editor.remove(IMAGE)
        editor.remove(NUMBER_PHONE)
        editor.remove(ACCESS_TOKEN)
        editor.commit()
    }

    val token: String?
        get() = sharedPreferences.getString(
            ACCESS_TOKEN,
            ""
        )

    val Email: String?
        get() = sharedPreferences.getString(
            EMAIL,
            ""
        )

    val Name: String?
        get() = sharedPreferences.getString(
            NAME,
            ""
        )

    val status: String?
        get() = sharedPreferences.getString(
            STATUS,
            ""
        )

    val image: String?
        get() = sharedPreferences.getString(
            IMAGE,
            ""
        )

    val phone: String?
        get() = sharedPreferences.getString(
            NUMBER_PHONE,
            ""
        )


    init {
        sharedPreferences = context.getSharedPreferences(
            PREF_NAME,
            PRIVATE_MODE
        )
        editor = sharedPreferences.edit()
    }

    companion object {
        private const val PREF_NAME = "AUTH"
    }
}