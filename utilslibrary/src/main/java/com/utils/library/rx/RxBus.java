//package com.utils.library.rx;
//
//import java.util.HashMap;
//
//import io.reactivex.Flowable;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.processors.FlowableProcessor;
//import io.reactivex.processors.PublishProcessor;
//
//public class RxBus {
//    private static volatile RxBus mInstance;
//    private FlowableProcessor<Object> mSubject;
//    private HashMap<String, CompositeDisposable> disposableHashMap;
//
//    private RxBus() {
//        //调用toSerialized()方法，保证线程安全
//        mSubject = PublishProcessor.create().toSerialized();
//    }
//
//    public static RxBus getInstance() {
//        if (mInstance == null) {
//            synchronized (RxBus.class) {
//                if (mInstance == null) {
//                    mInstance = new RxBus();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    /**
//     * 发送事件
//     *
//     * @param o
//     */
//    public void post(Object o) {
//        mSubject.onNext(o);
//    }
//
//    /**
//     * 返回指定类型的Observable实例
//     *
//     * @param type
//     * @param <T>
//     * @return
//     */
//    public <T> Flowable<T> tObservable(final Class<T> type) {
////       return mSubject.filter(new Func1<Object, Boolean>() {
////            @Override
////            public Boolean call(Object t) {
////                return type.isInstance(t);
////            }
////        }).cast(type);
//
//        return mSubject.ofType(type);
//    }
//
//    /**
//     * 是否已有观察者订阅
//     *
//     * @return
//     */
//    public boolean hasObservers() {
//        return mSubject.hasSubscribers();
//    }
//
//    /**
//     * 保存订阅后的subscription
//     *
//     * @param o
//     * @param subscription
//     */
//    public void addSubscription(Object o, Disposable subscription) {
//        if (disposableHashMap == null) {
//            disposableHashMap = new HashMap<>();
//        }
//        String key = o.getClass().getName();
//        if (disposableHashMap.get(key) != null) {
//            disposableHashMap.get(key).add(subscription);
//        } else {
//            CompositeDisposable compositeSubscription = new CompositeDisposable();
//            compositeSubscription.add(subscription);
//            disposableHashMap.put(key, compositeSubscription);
//        }
//    }
//
//    /**
//     * 取消订阅
//     *
//     * @param o
//     */
//    public void unSubscribe(Object o) {
//        if (disposableHashMap == null) {
//            return;
//        }
//
//        String key = o.getClass().getName();
//        if (!disposableHashMap.containsKey(key)) {
//            return;
//        }
//        if (disposableHashMap.get(key) != null) {
//            disposableHashMap.get(key).dispose();
//        }
//
//        disposableHashMap.remove(key);
//    }
//
//    public void removeAll() {
//        if (disposableHashMap != null && disposableHashMap.size() > 0) {
//            for (String key : disposableHashMap.keySet()) {
//                disposableHashMap.get(key).dispose();
//            }
//            disposableHashMap.clear();
//        }
//    }
//}
