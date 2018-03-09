package com.utils.library.rx

import java.util.HashMap

import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

//接收来自New_Update_ShopActivity
// RxBus.getInstance().addSubscription(this, RxBus.getInstance().tObservable(BaseEntity.class)
//        .observeOn(AndroidSchedulers.mainThread())
//.compose(this.bindToLifecycle())
//.subscribe(entity -> {
//    if (binding.include.swipeToLoadLayout != null)
//        binding.include.swipeToLoadLayout.setRefreshing(true);
//}
//, throwable -> refreshError(throwable.toString())
//));

//RxBus.getInstance().post(new BaseEntity());//发送到shoplistFragment更新列表

class RxBus {

    private val mSubject: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()//调用toSerialized()方法，保证线程安全


    private var disposableHashMap: HashMap<String, CompositeDisposable>? = null

    /**
     * 发送事件

     * @param o
     */
    fun post(o: Any) {
        mSubject.onNext(o)
    }

    /**
     * 返回指定类型的Observable实例

     * @param type
     * *
     * @param <T>
     * *
     * @return
    </T> */
    fun <T> tObservable(type: Class<T>): Flowable<T> {
        //       return mSubject.filter(new Func1<Object, Boolean>() {
        //            @Override
        //            public Boolean call(Object t) {
        //                return type.isInstance(t);
        //            }
        //        }).cast(type);

        return mSubject.ofType(type)
    }

    /**
     * 是否已有观察者订阅

     * @return
     */
    fun hasObservers(): Boolean {
        return mSubject.hasSubscribers()
    }

    /**
     * 保存订阅后的subscription

     * @param o
     * *
     * @param subscription
     */
    fun addSubscription(o: Any, subscription: Disposable) {
        if (disposableHashMap == null) {
            disposableHashMap = HashMap<String, CompositeDisposable>()
        }
        val key = o::class.simpleName!!
        if (disposableHashMap!![key] != null) {
            disposableHashMap!![key]?.add(subscription)
        } else {
            val compositeSubscription = CompositeDisposable()
            compositeSubscription.add(subscription)
            disposableHashMap!!.put(key, compositeSubscription)
        }
    }

    /**
     * 取消订阅

     * @param o
     */
    fun unSubscribe(o: Any) {
        if (disposableHashMap == null) {
            return
        }
        val key = o.javaClass.name
        if (!disposableHashMap!!.containsKey(key)) {
            return
        }
        if (disposableHashMap!![key] != null) {
            disposableHashMap!![key]?.dispose()
        }

        disposableHashMap!!.remove(key)
    }

    fun removeAll() {
        if (disposableHashMap != null && disposableHashMap!!.size > 0) {
            for (key in disposableHashMap!!.keys) {
                disposableHashMap!![key]?.dispose()
            }
            disposableHashMap!!.clear()
        }
    }

    companion object {
        @Volatile private var mInstance: RxBus? = null

        fun getInstance(): RxBus {
            if (mInstance == null) {
                synchronized(RxBus::class.java) {
                    if (mInstance == null) {
                        mInstance = RxBus()
                    }
                }
            }
            return mInstance!!
        }
    }
}
