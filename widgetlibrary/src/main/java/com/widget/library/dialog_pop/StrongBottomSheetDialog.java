package com.widget.library.dialog_pop;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

/**
 * <p>更加强壮的 BottomSheetDialog </p>
 * <li>增加了设置显示高度跟最大高度的方法</li>
 * <li>修复了通过手势关闭后无法再显示的问题</li>
 * </ul>
 */

public class StrongBottomSheetDialog extends BottomSheetDialog {

    private int mPeekHeight;
    private int mMaxHeight;
    private boolean mCreated;
    private Window mWindow;
    private BottomSheetBehavior mBottomSheetBehavior;

    public StrongBottomSheetDialog(@NonNull Context context) {
        super(context);
        mWindow = getWindow();
    }

    // 便捷的构造器
    public StrongBottomSheetDialog(@NonNull Context context, int peekHeight, int maxHeight) {
        this(context);
        mPeekHeight = peekHeight;
        mMaxHeight = maxHeight;
    }

    public StrongBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        mWindow = getWindow();
    }

    public StrongBottomSheetDialog(@NonNull Context context, boolean cancelable,
                                   OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCreated = true;

        setPeekHeight();
        setMaxHeight();
        setBottomSheetCallback();
    }

    public void setPeekHeight(int peekHeight) {
        mPeekHeight = peekHeight;
        if (mCreated) {
            setPeekHeight();
        }
    }

    public void setMaxHeight(int height) {
        mMaxHeight = height;
        if (mCreated) {
            setMaxHeight();
        }
    }

    private void setPeekHeight() {
        if (mPeekHeight <= 0) {
            return;
        }
        if (getBottomSheetBehavior() != null) {
            mBottomSheetBehavior.setPeekHeight(mPeekHeight);
        }
    }

    private void setMaxHeight() {
        if (mMaxHeight <= 0) {
            return;
        }
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mMaxHeight);
        mWindow.setGravity(Gravity.BOTTOM);
    }

    private BottomSheetBehavior getBottomSheetBehavior() {
        if (mBottomSheetBehavior != null) {
            return mBottomSheetBehavior;
        }
        View view = mWindow.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        // setContentView() 没有调用
        if (view == null) {
            return null;
        }
        mBottomSheetBehavior = BottomSheetBehavior.from(view);
        return mBottomSheetBehavior;
    }

    private void setBottomSheetCallback() {
        if (getBottomSheetBehavior() != null) {
            mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
        }
    }

    //这个方法是用来修复手动划出屏幕后不再显示的bug，这个方法必须在setContentView后面
    //系统的BottomSheetDialog是基于BottomSheetBehavior的这个我我们知道，这里判断了当我们滑动隐藏了BottomSheetBehavior中的View后，它替我们关闭了Dialog，所以我们再次调用dialog.show()的时候Dialog没法再此打开。
    //所以我们得自己来设置，并且在监听到用户滑动关闭BottomSheetDialog后，我们把BottomSheetBehavior的状态设置为BottomSheetBehavior.STATE_COLLAPSED，也就是半个打开状态（BottomSheetBehavior.STATE_EXPANDED为全打开），根据源码我把设置的方法提供下：
    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                StrongBottomSheetDialog.this.dismiss();
                mBottomSheetBehavior.setState(
                        BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
}