package com.android.zaojiu.helper

import android.content.ContentValues
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import java.lang.reflect.Modifier

/**
 * 对象可将属性信息存储于本地的对象类
 */
abstract class LocalStoreObject {

    protected fun toContentValues(): ContentValues {
        val values = ContentValues()
        try {
            for (field in javaClass.fields) {
                val modifier = field.modifiers
                if (Modifier.isFinal(modifier) || Modifier.isStatic(modifier)) {
                    // nothing..
                } else {
                    if (Modifier.isPublic(modifier)) {
                        val type = field.type
                        val fileds = field.get(this)
                        if (type == String::class.java) {
                            values.put(field.name, fileds?.toString() ?: "")

                        } else if (type == Integer.TYPE || type == Int::class.java) {
                            values.put(field.name, fileds?.toString() ?: "")

                        } else if (type == java.lang.Float.TYPE || type == Float::class.java) {
                            values.put(field.name, fileds?.toString() ?: "")

                        } else if (type == java.lang.Long.TYPE || type == Long::class.java) {
                            values.put(field.name, fileds?.toString() ?: "")

                        } else if (type == java.lang.Double.TYPE || type == Double::class.java) {
                            values.put(field.name, fileds?.toString() ?: "")

                        } else if (type == java.lang.Short.TYPE || type == Short::class.java) {
                            values.put(field.name, fileds?.toString() ?: "")

                        } else if (type == byteArrayOf().javaClass) {
                            values.put(field.name, fileds?.toString() ?: "")

                        } else if (type == java.lang.Boolean.TYPE || type == Boolean::class.java) {
                            values.put(field.name, fileds?.toString() ?: "")
                        } else {
                            // undo..
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
        return values
    }


    /**
     * 从应用程序 SharedPreferences 读取属性到指定的对象属性

     * @param prefs
     * *
     * @param obj
     */
    protected fun readLocalPropertiesFromSharedPreferences(prefs: SharedPreferences?, obj: Any?): Any? {
        if (prefs == null || obj == null) return null

        for (field in obj::class.java.declaredFields) {
            field.isAccessible = true
            val modifier = field.modifiers
            if (Modifier.isFinal(modifier) || Modifier.isStatic(modifier)) {
                continue
            }
//
//            if (!Modifier.isPublic(modifier)) {
//                continue
//            }

            try {
                val type = field.type
                if (type == String::class.java) {
                    field.set(obj, prefs.getString(field.name, ""))

                } else if (type == Integer.TYPE || type == Int::class.java) {
                    field.set(obj, prefs.getInt(field.name, -1))

                } else if (type == java.lang.Float.TYPE || type == Float::class.java) {
                    field.set(obj, prefs.getFloat(field.name, -1f))

                } else if (type == java.lang.Long.TYPE || type == Long::class.java) {
                    field.set(obj, prefs.getLong(field.name, -1))

                } else if (type == java.lang.Boolean.TYPE || type == Boolean::class.java) {
                    field.set(obj, prefs.getBoolean(field.name, false))

                }
            } catch (e: Exception) {
            }
        }
        return obj
    }

    /**
     * 存储指定对象的属性到应用程序 SharedPreferences

     * @param editor
     * *
     * @param obj
     */
    protected fun saveInstanceToSharedPreferences(editor: Editor?, obj: Any?) {
        if (editor == null || obj == null) return

        try {
            for (field in obj::class.java.declaredFields) {
                field.isAccessible = true
                val modifier = field.modifiers
                if (Modifier.isFinal(modifier) || Modifier.isStatic(modifier)) {
                    continue
                }

//                if (!Modifier.isPublic(modifier)) {
//                    continue
//                }

                val type = field.type
                if (type == String::class.java) {
                    var value: String? = field.get(obj).toString()
                    if ("null" == value || value == null) {
                        value = ""
                    }
                    editor.putString(field.name, value)

                } else if (type == Integer.TYPE) {
                    editor.putInt(field.name, Integer.valueOf(field.get(obj).toString())!!)

                } else if (type == java.lang.Float.TYPE) {
                    editor.putFloat(field.name, java.lang.Float.valueOf(field.get(obj).toString())!!)

                } else if (type == java.lang.Long.TYPE) {
                    editor.putLong(field.name, java.lang.Long.valueOf(field.get(obj).toString())!!)

                } else if (type == java.lang.Boolean.TYPE) {
                    editor.putBoolean(field.name, java.lang.Boolean.valueOf(field.get(obj).toString())!!)
                } else {
                    // NOTHING TO HERE. 仅仅保存，Sttring, int, float, long, boolean 类型的数据
                }
            }
        } catch (e: Exception) {
        }

        editor.commit()
    }
}