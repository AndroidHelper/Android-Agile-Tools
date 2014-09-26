/**
 * @Title: CoreFragment.java
 * @Package cn.betatown.mobile.comm.fragment
 * @Description:
 * @author Will.Wu
 * @date 2013年12月19日 下午3:45:15 
 * @version V1.0
 */

package cn.core.mobile.library.fragment;

import cn.core.library.mobile.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * @author Will.Wu
 * @ClassName: CoreFragment
 * @Description: TODO
 * @date 2013年12月19日 下午3:45:15
 */
public abstract class BaseFragment extends Fragment {
    private View mProgressContainer;
    private View mContentContainer;
    private View mContentView;
    private View mInternalContentView;
    private View mEmptyView;
    private View mRootView;
    private TextView mProgressMsg;
    private boolean mContentShown;
    private boolean mIsContentEmpty;

    public BaseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        init();
        return inflater.inflate(R.layout.base_fragment, container, false);

    }

    /**
     * Attach to view once the view hierarchy has been created.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureContent();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setInternalContentView(mContentView);
        fillView();
        setListener();
        loadData();
    }

    /**
     * Detach from view.
     */
    @Override
    public void onDestroyView() {
        mContentShown = false;
        mIsContentEmpty = false;
        mProgressContainer = mContentContainer = mContentView = mInternalContentView = mEmptyView = null;
        super.onDestroyView();
    }

    public abstract void init();

    protected void fillView() {
    }

    protected void setListener() {
    }

    protected void loadData() {
    }

    /**
     * Return content view or null if the content view has not been initialized.
     *
     * @return content view or null
     * @see #setContentView(android.view.View)
     * @see #setContentView(int)
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * Set the content content from a layout resource.
     *
     * @param layoutResId Resource ID to be inflated.
     * @see #setContentView(android.view.View)
     * @see #getContentView()
     */
    public void setContentView(int layoutResId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(layoutResId, null);
        setContentView(view);
    }

    /**
     * Set the content view to an explicit view. If the content view was
     * installed earlier, the content will be replaced with a new view.
     *
     * @param view The desired content to display. Value can't be null.
     * @see #setContentView(int)
     * @see #getContentView()
     */
    public void setContentView(View view) {
        mContentView = view;
    }

    private void setInternalContentView(View view) {
        ensureContent();
        if (view == null) {
            throw new IllegalArgumentException("Content view can't be null");
        }
        if (mContentContainer instanceof ViewGroup) {
            ViewGroup contentContainer = (ViewGroup) mContentContainer;
            if (mInternalContentView == null) {
                contentContainer.addView(view);
            } else {
                int index = contentContainer.indexOfChild(mInternalContentView);
                // replace content view
                contentContainer.removeView(mInternalContentView);
                contentContainer.addView(view, index);
            }
            mInternalContentView = view;
        } else {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
    }

    /**
     * The default content for a ProgressFragment has a TextView that can be
     * shown when the content is empty {@link #setContentEmpty(boolean)}. If you
     * would like to have it shown, call this method to supply the text it
     * should use.
     *
     * @param resId Identification of string from a resources
     * @see #setEmptyText(CharSequence)
     */
    public void setEmptyText(int resId) {
        setEmptyText(getString(resId));
    }

    /**
     * The default content for a ProgressFragment has a TextView that can be
     * shown when the content is empty {@link #setContentEmpty(boolean)}. If you
     * would like to have it shown, call this method to supply the text it
     * should use.
     *
     * @param text Text for empty view
     * @see #setEmptyText(int)
     */
    public void setEmptyText(CharSequence text) {
        ensureContent();
        if (mEmptyView != null && mEmptyView instanceof TextView) {
            ((TextView) mEmptyView).setText(text);
        } else {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
    }

    /**
     * Control whether the content is being displayed. You can make it not
     * displayed if you are waiting for the initial data to show in it. During
     * this time an indeterminant progress indicator will be shown instead.
     *
     * @param shown If true, the content view is shown; if false, the progress
     *              indicator. The initial value is true.
     * @see #setContentShownNoAnimation(boolean)
     */
    public void setContentShown(boolean shown) {
        setContentShown(shown, true);
    }

    /**
     * Like {@link #setContentShown(boolean)}, but no animation is used when
     * transitioning from the previous state.
     *
     * @param shown If true, the content view is shown; if false, the progress
     *              indicator. The initial value is true.
     * @see #setContentShown(boolean)
     */
    public void setContentShownNoAnimation(boolean shown) {
        setContentShown(shown, false);
    }

    /**
     * Control whether the content is being displayed. You can make it not
     * displayed if you are waiting for the initial data to show in it. During
     * this time an indeterminant progress indicator will be shown instead.
     *
     * @param shown   If true, the content view is shown; if false, the progress
     *                indicator. The initial value is true.
     * @param animate If true, an animation will be used to transition to the
     *                new state.
     */
    private void setContentShown(boolean shown, boolean animate) {
        ensureContent();
        if (mContentShown == shown) {
            return;
        }
        mContentShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_out));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mContentContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mContentContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Returns true if content is empty. The default content is not empty.
     *
     * @return true if content is null or empty
     * @see #setContentEmpty(boolean)
     */
    public boolean isContentEmpty() {
        return mIsContentEmpty;
    }

    /**
     * If the content is empty, then set true otherwise false. The default
     * content is not empty. You can't call this method if the content view has
     * not been initialized before {@link #setContentView(android.view.View)}
     * and content view not null.
     *
     * @param isEmpty true if content is empty else false
     * @see #isContentEmpty()
     */
    public void setContentEmpty(boolean isEmpty) {
        ensureContent();
        if (mContentView == null) {
            throw new IllegalStateException("Content view must be initialized before");
        }
        if (isEmpty) {
            mEmptyView.setVisibility(View.VISIBLE);
            mContentView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mContentView.setVisibility(View.VISIBLE);
        }
        mIsContentEmpty = isEmpty;
    }

    /**
     * Initialization views.
     */
    private void ensureContent() {
        if (mContentContainer != null && mProgressContainer != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }
        mProgressContainer = root.findViewById(R.id.base_fragment_progress_container);
        if (mProgressContainer == null) {
            throw new RuntimeException(
                    "Your content must have a ViewGroup whose id attribute is 'R.id.base_fragment_progress_container'");
        }
        mProgressMsg = (TextView) root.findViewById(R.id.base_progress_message);

        mContentContainer = root.findViewById(R.id.base_fragment_content_container);
        if (mContentContainer == null) {
            throw new RuntimeException(
                    "Your content must have a ViewGroup whose id attribute is 'R.id.base_fragment_content_container'");
        }
        mEmptyView = root.findViewById(android.R.id.empty);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        mContentShown = true;
        // We are starting without a content, so assume we won't
        // have our data right away and start with the progress indicator.
        if (mContentView == null) {
            setContentShown(false, false);
        }
    }

    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(cancelable, "");
    }

    public void showProgressDialog(boolean cancelable, String msg) {
        if (isAdded()) {
            if (!TextUtils.isEmpty(msg) && mProgressMsg != null) {
                mProgressMsg.setText(msg);
            }
            setContentShown(false, false);
        }
    }

    public void stopProgressDialog() {
        if (isAdded()) {
            setContentShown(true, false);
        }
    }

}
