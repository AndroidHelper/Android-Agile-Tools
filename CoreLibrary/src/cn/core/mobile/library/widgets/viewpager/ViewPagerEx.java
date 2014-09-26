
package cn.core.mobile.library.widgets.viewpager;

import cn.core.library.mobile.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * ViewPager功能扩展类。扩展自动翻页功能, 无限循环功能。
 * <ul>
 * <strong>Basic Setting and Usage for auto scroll</strong>
 * <li>{@link #setAutoStart(boolean autoStart)} start auto scroll.</li>
 * <li>{@link #stopAutoScroll()} stop auto scroll</li>
 * <li>{@link #setInterval(long)} set auto scroll time in milliseconds, default
 * is {@link #DEFAULT_INTERVAL}</li>
 * </ul>
 * <ul>
 *
 * @author Will.Wu </br> Create at 2014年2月27日 下午2:38:55
 * @version 1.0
 */
@SuppressLint("HandlerLeak")
public class ViewPagerEx extends ViewPager implements OnTouchListener {
    private static final int DEFAULT_INTERVAL = 5000;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    /**
     * 自动翻页等待的时间
     */
    private long mInterval = DEFAULT_INTERVAL;
    /**
     * 自动翻页的方向。
     */
    private int mDirection = RIGHT;
    /**
     * 自动翻页到最后一页或者第一页是否循环播放。
     */
    private boolean mFillingLoop = true;
    /**
     * 是否使用自动翻页功能
     */
    private boolean mAutoStart = false;
    /**
     * 自动翻页到最后一页或者第一页是否使用动画进入下个循环
     */
    private boolean mIsBorderAnimation = true;
    private boolean mRunning = false;
    private boolean mStarted = false;
    private boolean mVisible = false;
    private boolean mUserPresent = true;

    private final Handler mHandler = new AutoScrollHandler();
    private final BroadcastReceiver mReceiver = new UserPresentReceiver();
    private DurationScroller mScroller = null;

    private final static int FLIP_MSG = 1;

    private static final boolean DEFAULT_BOUNDARY_CASHING = false;
    OnPageChangeListener mOuterPageChangeListener = null;
    OnPageChangeListener mOnPageChangeListener = new MyOnPageChangeListener();
    private LoopPagerAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;

    public ViewPagerEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerEx);
        mInterval = a.getInt(R.styleable.ViewPagerEx_flipInterval, DEFAULT_INTERVAL);
        mAutoStart = a.getBoolean(R.styleable.ViewPagerEx_autoStart, false);
        mFillingLoop = a.getBoolean(R.styleable.ViewPagerEx_fillingLoop, true);
        mDirection = a.getInt(R.styleable.ViewPagerEx_direction, RIGHT);
        mIsBorderAnimation = a.getBoolean(R.styleable.ViewPagerEx_borderAnimation, true);
        a.recycle();

        init();
    }

    public ViewPagerEx(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        registerReceiver();

        if (mAutoStart) {
            // Automatically start when requested
            startFlipping();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        mVisible = false;

        unregisterReceiver();
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if (mAutoStart) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stopFlipping();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                startFlipping();
            }
        }
        return false;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        // TODO Auto-generated method stub
        mAdapter = new LoopPagerAdapterWrapper(adapter);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    }

    private void init() {
        initScroller();
        setOnTouchListener(this);

        super.setOnPageChangeListener(mOnPageChangeListener);
    }

    private void initScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            mScroller = new DurationScroller(getContext(),
                    (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listen for broadcasts related to user-presence
     *
     * @author Will.Wu
     */
    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        getContext().registerReceiver(mReceiver, filter, null, mHandler);
    }

    /**
     * 注销广播
     *
     * @author Will.Wu
     */
    private void unregisterReceiver() {
        getContext().unregisterReceiver(mReceiver);
    }

    /**
     * How long to wait before flipping to the next view
     *
     * @param milliseconds time in milliseconds
     */
    public void setInterval(long milliseconds) {
        mInterval = milliseconds;
    }

    public long getInterval() {
        return mInterval;
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
    }

    public void setFillingLoop(boolean isLoop) {
        mFillingLoop = isLoop;
    }

    public boolean isFillingLoop() {
        return mFillingLoop;
    }

    /**
     * Set if this view automatically calls {@link #startFlipping()} when it
     * becomes attached to a window.
     */
    public void setAutoStart(boolean autoStart) {
        mAutoStart = autoStart;
    }

    /**
     * No more auto scroll.
     *
     * @author Will.Wu
     */
    public void stopAutoScroll() {
        stopFlipping();
    }

    /**
     * Returns true if this view automatically calls {@link #startFlipping()}
     * when it becomes attached to a window.
     */
    public boolean isAutoStart() {
        return mAutoStart;
    }

    /**
     * whether animating when auto scroll at the last or first item, default is
     * true
     *
     * @return
     */
    public boolean isBorderAnimation() {
        return mIsBorderAnimation;
    }

    /**
     * set whether animating when auto scroll at the last or first item, default
     * is true
     *
     * @param isBorderAnimation
     */
    public void setBorderAnimation(boolean isBorderAnimation) {
        this.mIsBorderAnimation = isBorderAnimation;
    }

    public void scrollOnce() {
        scrollOnce(mDirection);
    }

    /**
     * scroll only once
     */
    public void scrollOnce(int direction) {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int totalCount;
        if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
            return;
        }

        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        if (nextItem < 0) {
            if (mFillingLoop) {
                setCurrentItem(0, mIsBorderAnimation);
                // setCurrentItem(totalCount - 1, mIsBorderAnimation);
            }
        } else if (nextItem == totalCount) {
            if (mFillingLoop) {
                setCurrentItem(nextItem, mIsBorderAnimation);

                // setCurrentItem(0, mIsBorderAnimation);
            }
        } else {
            setCurrentItem(nextItem, true);
        }
    }

    /**
     * Returns true if the child views are flipping.
     */
    public boolean isFlipping() {
        return mStarted;
    }

    /**
     * Start a timer to cycle through child views
     */
    protected void startFlipping() {
        mStarted = true;
        updateRunning();
    }

    /**
     * No more flips
     */
    protected void stopFlipping() {
        mStarted = false;
        //mHandler.removeMessages(FLIP_MSG);
        updateRunning();
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted && mUserPresent;
        if (running != mRunning) {
            if (running) {
                Message msg = mHandler.obtainMessage(FLIP_MSG);
                mHandler.sendMessageDelayed(msg, mInterval);
            } else {
                mHandler.removeMessages(FLIP_MSG);
            }
            mRunning = running;
        }
    }

    /**
     * 设置滚动动画时间毫秒数
     * @param duration Duration of the scroll in milliseconds.
     * @author Will.Wu
     */
    public void setScrollerDuration(int duration) {
        mScroller.setmDuration(duration);
    }

    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *
     * @param position
     * @param count
     * @return (position-1)%count
     */
    public static int toRealPosition(int position, int count) {
        position = position - 1;
        if (position < 0) {
            position += count;
        } else {
            position = position % count;
        }
        return position;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be
     * destroyed This may help to prevent "blinking" of some views
     *
     * @param flag
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

    /**
     * @author Will.Wu </br> Create at 2014年3月4日 上午11:01:00
     * @version 1.0
     */
    class DurationScroller extends Scroller {
        private int mDuration = 800;

        public DurationScroller(Context context) {
            super(context);
        }

        public DurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void setmDuration(int mDuration) {
            this.mDuration = mDuration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, this.mDuration);
        }
    }

    /**
     * 锁屏/解锁Receiver
     *
     * @author Will.Wu </br> Create at 2014年3月4日 上午11:04:57
     * @version 1.0
     */
    class UserPresentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mUserPresent = true;
            }
            updateRunning();
        }
    }

    /**
     * @author Will.Wu </br> Create at 2014年3月4日 上午11:08:05
     * @version 1.0
     */
    class AutoScrollHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLIP_MSG) {
                if (mRunning) {
                    scrollOnce();
                    msg = obtainMessage(FLIP_MSG);
                    sendMessageDelayed(msg, mInterval);
                }
            }
        }
    }

    class MyOnPageChangeListener implements OnPageChangeListener {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
            if (mAdapter != null) {
                int position = ViewPagerEx.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO Auto-generated method stub
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0 && mPreviousOffset == 0
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;
            if (mOuterPageChangeListener != null) {
                if (realPosition != mAdapter.getRealCount() - 1) {
                    mOuterPageChangeListener.onPageScrolled(realPosition, positionOffset,
                            positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(realPosition, 0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }
        }
    }

}
