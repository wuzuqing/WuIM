package com.wuzuqing.component_base.rxbus;

import android.util.Log;

import com.wuzuqing.component_base.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static RxBus instance;
    public static boolean DEBUG = BuildConfig.DEBUG;
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    private RxBus() {
    }


    public static RxBus get() {
        if (null == instance) {
            synchronized (RxBus.class) {
                if (null == instance) {

                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    /**
     * rxBus的注册方法
     *
     * @param tag
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag) {
        //可以为同一个tag 注册多个
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T> subject;
        //使用replaysubject 可以先发送事件,在订阅
        subjectList.add(subject = ReplaySubject.create());
        if (DEBUG) Log.d(TAG, "[register]" + tag + "\t subjectMapper: " + subjectMapper.size());
        return subject;
    }

    /**
     * rxBus的取消注册
     *
     * @param tag
     * @param observable
     */
    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject) observable);
            if (isEmpty(subjects)) {
                subjectMapper.remove(tag);
            }
        }

        if (DEBUG) Log.d(TAG, "[unregister] tag:" + tag + " \tsubjectMapper: " + subjectMapper.size());
    }

    /**
     * 发布事件
     *
     * @param tag
     * @param content
     */
    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);

        if (!isEmpty(subjectList)) {
            //同一个tag 都发布事件
            for (Subject subject : subjectList) {
                subject.onNext(content);

            }
        }
        if (DEBUG) Log.d(TAG, "[send]subjectMapper: " + subjectMapper.size());
    }


    public boolean containsKey(Object tag){
        return subjectMapper.containsKey(tag);
    }


    public boolean isEmpty(List list) {

        if (list != null) {
            if (list.size() == 0) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}