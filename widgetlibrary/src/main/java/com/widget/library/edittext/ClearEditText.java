package com.widget.library.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.widget.library.R;


/**
 * 在焦点变化时和输入内容发生变化时均要判断是否显示右边clean图标
 */
@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText {
	private class FocusChangeListenerImpl implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			isHasFocus = hasFocus;
			if (isHasFocus) {
				boolean isVisible = getText().toString().length() >= 1;
				setClearDrawableVisible(isVisible);
			} else {
				setClearDrawableVisible(false);
			}
		}

	}

	// 当输入结束后判断是否显示右边clean的图标
	private class TextWatcherImpl implements TextWatcher {
		/**
		 * 上次文本的长度,用于判断是删除还是添加
		 */
		int oldLength = 0;

		@Override
		public void afterTextChanged(Editable s) {
			printLog("afterTextChanged:" + s.toString());
			int length = s.toString().length();
			boolean isVisible = length >= 1;
			setClearDrawableVisible(isVisible);
//			boolean checkFormat = checkFormat(length);
//			printLog("checkFormat:" + checkFormat);
//			boolean del = s.length() > oldLength ? false : true;
//			if (mFormatEditText && checkFormat && !del) {
//				s.insert(s.length() - formatText.length(), formatText);
//			}
			oldLength = s.length();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	}

	private boolean isHasFocus;

	/**
	 * 是否格式化显示内容 四个字符中间加个空格
	 */
//	private boolean mFormatEditText = true;
//	private String formatText = "-";
	private Drawable mRightDrawable;

	public ClearEditText(Context context) {
		super(context);
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

//	public void setmFormatEditText(boolean mFormatEditText) {
//		this.mFormatEditText = mFormatEditText;
//	}

//	public void setFormatText(String formatText) {
//		this.formatText = formatText;
//	}

	/**
	 * 根据当前文本的内容判断是否需要加空格
	 * 
//	 * @param length
	 * @return
	 */
//	private boolean checkFormat(int length) {
//		int formatCellLength = 4;
//		if (length != 0
//				&& (length % (formatCellLength + formatText.length()) == 0)) {
//			return true;
//		}
//		return false;
//	}

	private void init() {
		// getCompoundDrawables:
		// Returns drawables for the left, top, right, and bottom borders.
		Drawable[] drawables = this.getCompoundDrawables();

		// 取得right位置的Drawable
		// 即我们在布局文件中设置的android:drawableRight
		mRightDrawable = drawables[2];

		// 设置焦点变化的监听
		this.setOnFocusChangeListener(new FocusChangeListenerImpl());
		// 设置EditText文字变化的监听
		this.addTextChangedListener(new TextWatcherImpl());
		// 初始化时让右边clean图标不可见
		setClearDrawableVisible(false);
//		setHintTextColor(getResources().getColor(R.color.ddcar_text_color));
	}

	/**
	 * 当手指抬起的位置在clean的图标的区域 我们将此视为进行清除操作 getWidth():得到控件的宽度
	 * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
	 * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
	 * getPaddingRight():clean的图标右边缘至控件右边缘的距离 于是: getWidth() -
	 * getTotalPaddingRight()表示: 控件左边到clean的图标左边缘的区域 getWidth() -
	 * getPaddingRight()表示: 控件左边到clean的图标右边缘的区域 所以这两者之间的区域刚好是clean的图标的区域
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:

			boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
					&& (event.getX() < (getWidth() - getPaddingRight()));
			if (isClean) {
				setText("");
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	private void printLog(String string) {
		Log.d("CleanableEditText", string);
	}

	// 隐藏或者显示右边clean的图标
	protected void setClearDrawableVisible(boolean isVisible) {
		Drawable rightDrawable;
		if (isVisible) {
			rightDrawable = mRightDrawable;
		} else {
			rightDrawable = null;
		}
		// 使用代码设置该控件left, top, right, and bottom处的图标
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], rightDrawable,
				getCompoundDrawables()[3]);
	}

	// 显示一个动画,以提示用户输入
	public void startShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	// CycleTimes动画重复的次数
	public Animation shakeAnimation(int CycleTimes) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
		translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	@Override
	public Editable getText() {
		return super.getText();
	}

//	public String getFormatText() {
//		String str = getText().toString();
//		if (mFormatEditText) {

//			str.replaceAll(formatText, "");

//		}
//		return str;
//	}
}
