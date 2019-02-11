package com.hjq.basemvp.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.basemvp.App;
import com.hjq.basemvp.R;
import com.hjq.basemvp.di.component.ActivityComponent;
import com.hjq.basemvp.di.component.DaggerActivityComponent;
import com.hjq.basemvp.di.module.ActivityModule;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/7 0007 下午 4:50
 * mail : 2726449200@qq.com
 * describe ：
 */
public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends RxAppCompatActivity implements BaseContract.BaseView {

    @Nullable
    @Inject
    protected T mPresenter;
    protected ActivityComponent mActivityComponent;
    @Nullable
    protected Toolbar mToolbar;
    private Unbinder unbinder;
    private SwipeRefreshLayout mRefreshLayout;

    protected abstract Object getLayout();

    protected abstract void initInjector();

    protected abstract void initView();

    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityComponent();;
        if (getLayout() instanceof Integer) {
            setContentView((Integer) getLayout());;
        } else if (getLayout() instanceof View) {
            setContentView((View) getLayout());
        } else {
            throw new ClassCastException("getLayout() type must be int or View");
        }
        initInjector();
        unbinder = ButterKnife.bind(this);
        ActivityCollector.addActivity(this);
        // initToolBar();
        attachView();
        initView();

        if (!NetworkUtils.isConnected()) showNoNet();
    }

    @Override
    public void showLoading() {
        mProgressDialog = new ProgressDialog(this);
        if (mProgressDialog != null) {
            mProgressDialog.setMessage("正在加载数据");
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //设置下拉刷新状态
    public void setRefresh(Boolean refresh) {
        if (mRefreshLayout == null) {
            return;
        }
        if (!refresh) {
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRefreshLayout != null)
                        mRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        } else {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
        detachView();
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onRetry() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public void showSuccess(String successMsg) {
        ToastUtils.showShort(successMsg);
    }

    @Override
    public void showFaild(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void showNoNet() {
        ToastUtils.showShort(R.string.no_network_connection);
    }

    /**
     * 初始化toolbar
     */
    /*
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp());
            //toolbar去掉阴影
            getSupportActionBar().setElevation(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mToolbar.setElevation(0);
            }
        }
        if (isSetRefresh()) {
            setupSwipeRefresh();
        }
    }

    private void setupSwipeRefresh() {
        mRefreshLayout = findViewById(R.id.swipe_refresh);
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(R.color.refresh_color_1,
                    R.color.refresh_color_1, R.color.refresh_color_1);
            mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            mRefreshLayout.setOnRefreshListener(this::requestDataRefresh);
        }
    }
    */

    public void requestDataRefresh() {
    }

    /**
     * 判断子Activity是否需要刷新功能
     *
     * @return false
     */
    public Boolean isSetRefresh() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
        return true;
    }

    /**
     * 是否显示返回键
     *
     * @return
     */
    public boolean showHomeAsUp() {
        return false;
    }


    protected void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }
}
