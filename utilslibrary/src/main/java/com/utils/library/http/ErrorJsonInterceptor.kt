package com.utils.library.http

import com.elvishew.xlog.XLog
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter
import com.elvishew.xlog.formatter.message.json.JsonFormatter
import com.utils.library.utils.isEmptyOrNull

import java.nio.charset.Charset

import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

/**
 * Created by cuiyang on 2017/4/6.
 * debug请求日志
 */

class ErrorJsonInterceptor : Interceptor {
    private var jsonFormatter: JsonFormatter = DefaultJsonFormatter()
    private val charset1 = Charset.forName("UTF-8")

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val response = chain.proceed(request)
        val responseBody = response.body() ?: return response

        val source = responseBody.source()
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer()

        var charset: Charset? = charset1
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset1)
        }

        if (responseBody.contentLength() != 0L && charset != null) {
            val json = buffer.clone().readString(charset)
            //开头为"{"即是json
            if ("{" == json.subSequence(0, 1)) {
                val jsonObject = jsonConvert(json)
                //先清除原先内容
                buffer.clear()
                //将处理过的json放入
                buffer.writeString(jsonObject.toString(), charset1)
//                XLog.d(jsonFormatter.format(jsonObject.toString()))
            }
        }
        return response
    }

    /**
     * 对错误json进行处理
     */
    private fun jsonConvert(json: String): JSONObject {
        val jsonObject = JSONObject(json)
        //对错误数据进行处理 data直接remove
        val code = jsonObject.getInt("code")
        if (code != 0) {
            jsonObject.remove("data")
        } else if ((jsonObject.get("data").toString() == "null")) {
            jsonObject.remove("data")
            jsonObject.remove("code")
            //自定义一个code去通知
            jsonObject.put("code", -999)
        }
        return jsonObject
    }

}
