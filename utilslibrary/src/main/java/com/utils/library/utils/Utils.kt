package com.utils.library.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cuiyang on 16/6/3.
 */
object Utils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * Convert Dp to Pixel
     */
    fun dpToPx(dp: Float, resources: Resources): Int {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        return px.toInt()
    }

    fun daysBetween(smdate: String, bdate: String, sdf: SimpleDateFormat): Int {
        val cal = Calendar.getInstance()
        cal.time = sdf.parse(smdate)
        val time1 = cal.timeInMillis
        cal.time = sdf.parse(bdate)
        val time2 = cal.timeInMillis
        val betweenDays = (time2 - time1) / (1000 * 3600 * 24)
        return Integer.parseInt(betweenDays.toString())
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    fun getDistanceTime(str1: String, str2: String, df: SimpleDateFormat): String {
        val one: Date
        val two: Date
        val day: Long = 0
        var hour: Long = 0
        var min: Long = 0
        val sec: Long = 0
        try {
            one = df.parse(str1)
            two = df.parse(str2)
            val time1 = one.time
            val time2 = two.time
            val diff: Long
            if (time1 < time2) {
                diff = time2 - time1
            } else {
                diff = time1 - time2
            }
            //            day = diff / (24 * 60 * 60 * 1000);
            hour = diff / (60 * 60 * 1000) - day * 24
            min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
            //            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        //        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
        return if (hour > 0) hour.toString() + "小时" else if (min > 0) min.toString() + "分钟" else "1分钟"
    }

    /**
     * 当我们判断出当前设备打开允许模拟位置时，在判断一下手机系统的版本，若为Android M以及以上，就屏蔽不管。
     * 可能部分同学会问那么android M上的选择模拟位置信息应用有影响么？答案是否定的，由于我们的App没有添加允许模拟位置的权限，所以其根本不会出现在选择模拟位置应用列表，进而不会执行模拟位置的操作。
     * 所以最终的解决方案就是，检测设备是否开启了模拟位置选项，若开启了，则判断当前设备是否为Android M即以上，若是，则屏蔽不管，否则阻塞用户操作，引导用户关闭模拟位置选项。
     */
    fun isAllowMockLocation(mContext: Context): Boolean {
        var isOpen = Settings.Secure.getInt(mContext.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0

        /**
         * 该判断API是androidM以下的API,由于Android M中已经没有了关闭允许模拟位置的入口,所以这里一旦检测到开启了模拟位置,并且是android M以上,则
         * 默认设置为未有开启模拟位置
         */
        if (isOpen && Build.VERSION.SDK_INT > 22) {
            isOpen = false
        }
        return isOpen
    }
}