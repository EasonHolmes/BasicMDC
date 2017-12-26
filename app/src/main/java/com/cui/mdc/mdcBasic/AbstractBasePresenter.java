package com.cui.mdc.mdcBasic;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author cuiyang
 * @date 16/6/6
 * <p>
 * getCompositeDisposable().add(ApiClient.getApiService()
 * .getBidTypeList()
 * .map(RxHelper.RxMapJsonobjectData)
 * .map(str -> GsonSingle.getInstance().fromJson(str, BidTypeListEntity.class))
 * .compose(RxSchedulersHelper.io_main())
 * .compose(mView.bindToLifecycle())
 * .subscribe(entity -> mView.getBidTypeListUpdateRv(entity),
 * throwable -> mView.refreshError(throwable.getMessage())));
 * <p>
 * <p>
 * 转换jsonarray
 * //        val type = object : TypeToken<ArrayList<T>>() {}.type
 * //        var s  = GsonSingle.getInstance().fromJson<List<T>>("",type);
 */
public class AbstractBasePresenter<V> implements LifecycleObserver, BaseContract.BasePresenter {
    /**
     * 这个基类的Presenter 主要的作用就是将当前Presenter持有的view 在合适的时候给清除掉
     * 如果有多个P2使用同一个P1的情况在P1不要确定泛型在P2中的构造方法中强转
     */
    protected V mView;
    private CompositeDisposable disposables;

    public AbstractBasePresenter(V mView) {
        this.mView = mView;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void dettach() {
        if (disposables != null) {
            disposables.clear();
            disposables = null;
        }
        mView = null;
    }

    protected CompositeDisposable getCompositeDisposable() {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        return disposables;
    }


}
