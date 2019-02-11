package com.hjq.basemvp.di.component;

import android.app.Activity;
import android.content.Context;


import com.hjq.basemvp.di.module.FragmentModule;
import com.hjq.basemvp.di.scope.ContextLife;
import com.hjq.basemvp.di.scope.PerFragment;

import dagger.Component;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/10/20 0020 下午 2:46
 * mail : 2726449200@qq.com
 * describe ：
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getAcitivtyContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getAcitivty();

    // void inject(ZhihuFragment zhihuFragment);

}
