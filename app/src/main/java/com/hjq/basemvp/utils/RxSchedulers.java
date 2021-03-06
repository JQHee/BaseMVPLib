package com.hjq.basemvp.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/10/20 0020 上午 10:34
 * mail : 2726449200@qq.com
 * describe ：通用的Rx线程转换类
 * 参考:http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0819/3327.html
 */
public class RxSchedulers {
    static final ObservableTransformer schedulersTransformer= upstream -> (upstream).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return  (ObservableTransformer<T, T>)schedulersTransformer;
    }
}
