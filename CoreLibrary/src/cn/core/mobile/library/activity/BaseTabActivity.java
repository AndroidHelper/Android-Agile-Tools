/**
 * 项目名称: CoreLibrary
 * 文件名称: BaseTabActivityV2.java
 * 创建时间: 2014年4月14日
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved. 
 */

package cn.core.mobile.library.activity;

import cn.core.library.mobile.R;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import java.util.ArrayList;

/**
 * @author Will.Wu </br> Create at 2014年4月14日 上午9:57:42
 * @version 1.0
 */
public abstract class BaseTabActivity extends FragmentActivity implements OnTabChangeListener {
    protected TabHost mTabHost;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private FragmentManager mFragmentManager;
    private TabInfo mLastTab;
    private boolean mAttached;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        init(arg0);
        mFragmentManager = getSupportFragmentManager();
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);
        fillView();
        setListener();
        initTab();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.base_push_right_in, R.anim.base_push_left_out);
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        overridePendingTransition(R.anim.base_push_left_in, R.anim.base_push_right_out);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String currentTab = mTabHost.getCurrentTabTag();
        // Go through all tabs and make sure their fragments match
        // the correct state.
        FragmentTransaction ft = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.fragment != null && !tab.fragment.isDetached()) {
                if (tab.tag.equals(currentTab)) {
                    // The fragment for this tab is already there and
                    // active, and it is what we really want to have
                    // as the current tab.  Nothing to do.
                    mLastTab = tab;
                } else {
                    // This fragment was restored in the active state,
                    // but is not the current tab.  Deactivate it.
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.detach(tab.fragment);
                }
            }
        }
        // We are now ready to go.  Make sure we are switched to the
        // correct tab.
        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commit();
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    /**
     * your <code>setContentView(layoutResID)</code> in here.
     */
    protected abstract void init(Bundle savedInstanceState);

    protected void setListener() {
    }

    protected void loadData() {
    }

    protected void fillView() {
    }

    protected abstract void initTab();

    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        if (mAttached) {
            FragmentTransaction ft = doTabChanged(tabId, null);
            if (ft != null) {
                ft.commit();
            }
        }
    }

    /**
     * @param tag           The tag of {@link TabHost.TabSpec}
     * @param indicator     Specify a view as the tab indicator.
     * @param fragmentCalzz Specify the Fragment that should be used as the
     *                      content of the tab.
     * @param bundle
     * @author Will.Wu
     */
    public void addTab(String tag, View indicator, Class<?> fragmentCalzz, Bundle bundle) {
        if (mTabHost != null) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
            tabSpec.setContent(new DummyTabFactory(this));
            tabSpec.setIndicator(indicator);

            TabInfo info = new TabInfo(tag, fragmentCalzz, bundle);
            if (mAttached) {
                // If we are already attached to the window, then check to make
                // sure this tab's fragment is inactive if it exists.  This shouldn't
                // normally happen.
                info.fragment = mFragmentManager.findFragmentByTag(tag);
                if (info.fragment != null && !info.fragment.isDetached()) {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.detach(info.fragment);
                    ft.commit();
                }
            }
            mTabHost.addTab(tabSpec);
            mTabs.add(info);
        }
    }

    public void switchTab(int index) {
        if (mTabHost != null) {
            mTabHost.setCurrentTab(index);
        }
    }

    public void switchTab(String tag) {
        if (mTabHost != null) {
            mTabHost.setCurrentTabByTag(tag);
        }
    }

    public TabHost getTabHost() {
        return mTabHost;
    }

    /**
     * 检查网络连接
     *
     * @return
     */
    public boolean checkInternetConnection() {
        NetworkInfo info = null;
        if (info == null) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            info = manager.getActiveNetworkInfo();
        }
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(this,
                            newTab.clss.getName(), newTab.args);
                    ft.add(R.id.base_real_tab_content, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }

            mLastTab = newTab;
        }
        return ft;
    }

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

}
