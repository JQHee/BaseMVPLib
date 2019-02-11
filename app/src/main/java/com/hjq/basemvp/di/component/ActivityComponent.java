package com.hjq.basemvp.di.component;

import android.app.Activity;
import android.content.Context;


import com.hjq.basemvp.di.module.ActivityModule;
import com.hjq.basemvp.di.scope.ContextLife;
import com.hjq.basemvp.di.scope.PerActivity;

import dagger.Component;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/10/19 0019 下午 7:39
 * mail : 2726449200@qq.com
 * describe ：
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    // void inject(ZhihuWebActivity zhihuWebActivity);

}
