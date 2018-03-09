package com.utils.library.rx;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by cuiyang on 16/6/29.
 * http://www.jianshu.com/p/f3f0eccbcd6f
 */
public class RxSchedulersHelper {

    //    http://www.jianshu.com/p/e9e03194199e  rx1
    //https://juejin.im/post/58ce6cb2b123db3f6b3fcd2d rx2
    private static final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(io.reactivex.Observable upstream) {
            return upstream.subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private static final FlowableTransformer Flowable_schedulersTransformer = new FlowableTransformer() {
        @Override
        public Publisher apply(Flowable upstream) {
            return upstream.subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * observable 统一的io转主线程的线程调度
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }

    /**
     * flowable  统一的io转主线程的线程调度
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> Flowable_io_main() {
        return (FlowableTransformer<T, T>) Flowable_schedulersTransformer;
    }

}
