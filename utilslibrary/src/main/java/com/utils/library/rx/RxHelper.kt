package com.utils.library.rx

import com.google.gson.JsonObject
import com.utils.library.utils.isEmptyOrNull
import io.reactivex.functions.Function

/**
 * Created by cuiyang on 2017/1/17.
 */

object RxHelper {

    /**
     * 做一个中间层当code不为0时data节点直接remove避免有retroift的gson类型转换错误
     */
    val RxMapJsonobjectData: Function<JsonObject, String> = Function { jsonObject ->
        val code = jsonObject.get("code").asInt
        if (code != 0 || jsonObject.get("data").toString().isEmptyOrNull() || jsonObject.get("data").toString() == "\"\"") {
            jsonObject.remove("data")
        }
        jsonObject.toString()
    }
}
