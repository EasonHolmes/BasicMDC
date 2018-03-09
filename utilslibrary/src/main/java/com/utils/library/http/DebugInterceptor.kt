package com.utils.library.http

import com.elvishew.xlog.XLog
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter
import com.elvishew.xlog.formatter.message.json.JsonFormatter

import java.nio.charset.Charset

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okio.Buffer

/**
 * Created by cuiyang on 2017/10/19.
 */

class DebugInterceptor : Interceptor {
    private var jsonFormatter: JsonFormatter = DefaultJsonFormatter()

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //request Log start
        val request = chain.request()
        val requestBody = request.body()

        if (requestBody != null) {
            val connection = chain.connection()
            val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1

            val requestBuffer = Buffer()
            var requestCharset: Charset? = UTF8
            requestBody.writeTo(requestBuffer)

            val requestContentType = requestBody.contentType()
            if (requestContentType != null) {
                requestCharset = requestContentType.charset(UTF8)
            }
            XLog.e("请求开始:\n--> " + request.method() //方法

                    + ' ' + request.url() + "\n"//url

                    + ' ' + if (requestCharset != null) requestBuffer.readString(requestCharset) else "" + "\n"//post时后面跟的参数

                    + ' ' + protocol(protocol) + "\n"//http1.0orhttp1.1

                    + " (" + requestBody.contentLength() + "-byte body)")//request内容长度
        } else {
            XLog.e("请求开始:\n--> " + request.method() //方法

                    + ' ' + request.url() + "\n")//url
        }
        //request Log end

        //respose log start
        val response = chain.proceed(request)
        val responseBody = response.body()

        val source = responseBody!!.source()
        // Buffer the entire body.
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()

        var charset: Charset? = UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(UTF8)
        }

        if (responseBody.contentLength() != 0L && charset != null) {
            val json = buffer.clone().readString(charset)
            //如果为{开头就是json   XLog.i(jsonFormatter.format(buffer.clone().readString(charset)))
            if ("{" == json.subSequence(0, 1)) {
                XLog.d(request.url().toString() + "\n")
                XLog.d(jsonFormatter.format(json))
            } else {
                XLog.d(request.url().toString() + "\n" + json)
            }
        } else {
            XLog.e("请求结束:\n" + response.request().url().toString() + "没有返回任何数据")
        }
        //respose log end
        return response
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")

        private fun protocol(protocol: Protocol): String {
            return if (protocol == Protocol.HTTP_1_0) "HTTP/1.0" else "HTTP/1.1"
        }
    }
}
