package com.example.learnkotlin.util

import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException
import javax.inject.Inject

class ResponseHandler @Inject constructor(){
    inline fun<reified T> handleResponse(call: () -> Response<T>): Result<T> {
        try {
            val response = call.invoke()
            try {
                return when {
                    response.isSuccessful -> {
                        Result.Success(response.body()!!)
                    }
                    !response.isSuccessful -> {
                        response.errorBody()?.let {
                            val errorResponse = Gson().fromJson(it.charStream(), T::class.java)
                            return Result.Error(errorResponse, "${response.code()} ${response.message()}")
                        } ?: return Result.Error(
                            null,
                            message = let {
                                if (response.code() == 401) {
                                    "Terjadi kesalahan, mohon login ulang."
                                } else {
                                    "${response.code()} ${response.message()}"
                                }
                            }
                        )
                    }
                    else -> {
                        Result.Loading()
                    }
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                return Result.Error(null, "HttpException: ${e.message()}")
            } catch (e: IOException) {
                e.printStackTrace()
                return Result.Error(null, "IOException: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                return Result.Error(null, "Something went wrong: ${e.message}")
            }
        } catch (t: Throwable) {
            return when (t) {
                is SocketTimeoutException -> {
                    t.printStackTrace()
                    Result.Error(null, "Timeout: Mohon periksa internet anda")
                }
                is CancellationException -> {
                    t.printStackTrace()
                    Result.Error(null, "Canceled")
                }
                is Exception -> {
                    t.printStackTrace()
                    Result.Error(null, "Something went wrong: ${t.message}")
                }
                else -> {
                    t.printStackTrace()
                    Result.Error(null, "UnknownException: ${t.message}")
                }
            }
        }
    }
}