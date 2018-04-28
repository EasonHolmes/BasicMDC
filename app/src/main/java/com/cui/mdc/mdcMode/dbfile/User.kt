package com.android.zaojiu.model.entity.dbfile


import android.content.Context
import com.android.zaojiu.helper.LocalStoreObject
import com.utils.library.utils.isNotEmptyStr
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * 当前登录用户

 * @since 2015-3
 */
class User(private val mContext: Context) : LocalStoreObject() {
    //uid
    var userId: Long = 0
    var avatar: String = ""
    var email: String = ""
    var _TOKEN: String = ""
    var nick: String = ""
    var mobile: String = ""
    var isSubscribed: Boolean = false
    var permissions: Boolean = false

    val isLogin: Boolean
        get() = userId > 0

    /**
     * 属性值本地数据持久化
     */
    fun saveInstance() {
//        Schedulers.io().createWorker().schedule {
        val editor = mContext.getSharedPreferences("u_user", Context.MODE_PRIVATE).edit()
        saveInstanceToSharedPreferences(editor, this)
//        }.dispose()
    }

    /**
     * 从应用程序配置中读取属性
     */
    fun readLocalProperties(): User {
//        Schedulers.io().createWorker().schedule {
        val prefs = mContext.getSharedPreferences("u_user", Context.MODE_PRIVATE)
        return readLocalPropertiesFromSharedPreferences(prefs, this) as User
//        }.dispose()
    }

    /**
     * 重置属性
     */
    fun reset() {
        userId = 0
        avatar = ""
        _TOKEN = ""
        email = ""
        nick = ""
        mobile = ""
        isSubscribed = false
        permissions = false
        saveInstance()
    }

    override fun toString(): String {
        return "User(mContext=$mContext, userId=$userId, avatar='$avatar', email='$email', _TOKEN='$_TOKEN', nick='$nick', mobile='$mobile', isSubscribed=$isSubscribed, permissions=$permissions)"
    }

}
