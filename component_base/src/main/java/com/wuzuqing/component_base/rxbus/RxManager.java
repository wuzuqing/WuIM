package com.wuzuqing.component_base.rxbus;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxManager {

    public RxBus mRxBus = RxBus.get(); //拿到rxBus
    //管理rxbus订阅
    private Map<Object, Observable<?>> mObservables = new HashMap<>();
    /*管理Observables 和 Subscribers订阅*/
    private CompositeDisposable composite = new CompositeDisposable();

    /**
     * RxBus订阅
     *
     * @param eventName
     * @param action1
     */
    public <T> void on(Object eventName, Consumer<T> action1) {
        Observable<T> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);

        /*订阅管理*/
        composite.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        throwable.printStackTrace();
                    }
                }));

    }


    /**
     * 单纯的Observables 和 Subscribers管理
     *
     * @param m
     */
    public void add(Disposable m) {
        /*订阅管理*/
        composite.add(m);
    }

    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    public void clear() {
        composite.clear();// 取消所有订阅
        for (Map.Entry<Object, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除rxbus观察
        }
        mObservables.clear();
        mObservables = null;
        mRxBus = null;
        mObservables = null;
    }

    //发送rxbus
    public void post(Object tag, Object content) {
        if (mRxBus.containsKey(tag)) { //如果有注册事件,则发送.否则不发

        }
        mRxBus.post(tag, content);
    }


}
