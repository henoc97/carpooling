package tg.ulcrsandroid.carpooling.application.utils.lottie

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun loadJsonFromRaw(context: Context, resourceId: Int): String? {
    val inputStream = context.resources.openRawResource(resourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String?
    try {
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        inputStream.close()
        reader.close()
    }
    return stringBuilder.toString()
}