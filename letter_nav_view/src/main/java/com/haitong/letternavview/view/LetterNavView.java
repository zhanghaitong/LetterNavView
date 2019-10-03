package com.haitong.letternavview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.haitong.letternavview.R;
import com.haitong.letternavview.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhhaitong on 2019/9/29.
 */
public class LetterNavView extends View {

    private static final String TAG = "LetterNavView";

    private int navTvTextSize;
    private float navTvTextSelectedSize;
    private int navTvTextColor;
    private int navTvTextSelectedColor;
    private int navBgColor;
    private int navBgActiveColor;
    private float navPaddingLeft;
    private float navPaddingRight;
    private float navPaddingTop;
    private float navPaddingBottom;
    private float overlyTvWidth;
    private float overlyTvHeight;
    private int overlyTextSize;
    private int overlyTextColor;
    private int overlyBgColor;

    private Context mContext;

    private LetterChangeListener onTouchingLetterChangedListener;
    private List<String> textList = new ArrayList<>();
    private List<String> topList = new ArrayList<>();
    private List<String> bottomList = new ArrayList<>();

    private static final List<String> letterList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    int activedIndex = -1;
    private Paint mPaint = new Paint();
    private boolean mNavActive = false;
    private TextView tvLetterOverlay;

    public LetterNavView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public LetterNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();

        Log.d(TAG, "LetterNavView() called with: context = [" + context + "], attrs = [" + attrs + "]");

        //加载自定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterNavView);
        navTvTextSize = typedArray.getDimensionPixelSize(R.styleable.LetterNavView_navTvTextSize, DensityUtil.sp2px(context, 10));
        navTvTextSelectedSize = typedArray.getDimension(R.styleable.LetterNavView_navTvTextSelectedSize, DensityUtil.sp2px(context, 10));
        navTvTextColor = typedArray.getColor(R.styleable.LetterNavView_navTvTextColor, Color.parseColor("#8c8c8c"));
        navTvTextSelectedColor = typedArray.getColor(R.styleable.LetterNavView_navTvTextSelectedColor, Color.parseColor("#40c60a"));
        navBgColor = typedArray.getColor(R.styleable.LetterNavView_navBgColor, Color.TRANSPARENT);
        navBgActiveColor = typedArray.getColor(R.styleable.LetterNavView_navBgActiveColor, Color.parseColor("#40000000"));
        navPaddingLeft = typedArray.getDimension(R.styleable.LetterNavView_navPaddingLeft, 0);
        navPaddingRight = typedArray.getDimension(R.styleable.LetterNavView_navPaddingRight, 0);
        navPaddingTop = typedArray.getDimension(R.styleable.LetterNavView_navPaddingTop, DensityUtil.dp2px(context, 10));
        navPaddingBottom = typedArray.getDimension(R.styleable.LetterNavView_navPaddingBottom, DensityUtil.dp2px(context, 10));
        overlyTvWidth = typedArray.getDimension(R.styleable.LetterNavView_overlyTvWidth, DensityUtil.dp2px(context, 65));
        overlyTvHeight = typedArray.getDimension(R.styleable.LetterNavView_overlyTvHeight, DensityUtil.dp2px(context, 65));
        overlyTextSize = typedArray.getDimensionPixelSize(R.styleable.LetterNavView_overlyTextSize, 12);
        overlyTextColor = typedArray.getColor(R.styleable.LetterNavView_overlyTextColor, Color.WHITE);
        overlyBgColor = typedArray.getColor(R.styleable.LetterNavView_overlyBgColor, Color.parseColor("#83000000"));

        LayoutInflater inflater = LayoutInflater.from(context);
        tvLetterOverlay = (TextView) inflater.inflate(R.layout.v_letter_overlay, null);
        tvLetterOverlay.setVisibility(View.INVISIBLE);

        tvLetterOverlay.setTextSize(overlyTextSize);
        tvLetterOverlay.setTextColor(overlyTextColor);
        tvLetterOverlay.setBackgroundColor(overlyBgColor);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                (int) overlyTvWidth,
                (int) overlyTvHeight,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(tvLetterOverlay, layoutParams);

        setPadding((int) navPaddingLeft, (int) navPaddingTop, (int) navPaddingRight, (int) navPaddingBottom);

        typedArray.recycle();
    }

    public LetterNavView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {

        textList.clear();

        textList.addAll(topList);
        textList.addAll(letterList);
        textList.addAll(bottomList);
    }

    public void setNavTopTextList(List<String> navTopTextArray) {
        this.topList.clear();
        this.topList.addAll(navTopTextArray);
        init();
        invalidate();
    }

    public void setNavBottomTextList(List<String> navBottomTextArray) {
        this.bottomList.clear();
        this.bottomList.addAll(navBottomTextArray);
        init();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(navBgColor);

        if (mNavActive) {
            canvas.drawColor(navBgActiveColor);
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingBottom - paddingTop;

        int singleHeight = height / textList.size();

        mPaint.setColor(navTvTextColor);
        mPaint.setTextSize(navTvTextSize);
        float tmpHeight = mPaint.measureText("K") / 2;
        for (int i = 0; i < textList.size(); i++) {
            if (i == activedIndex) {
                mPaint.setColor(navTvTextSelectedColor);
                mPaint.setTextSize(navTvTextSelectedSize);
            } else {
                mPaint.setColor(navTvTextColor);
                mPaint.setTextSize(navTvTextSize);
            }

            float x = (width >> 1) - mPaint.measureText(textList.get(i)) / 2;
            float y = paddingTop + singleHeight * i + singleHeight - tmpHeight;

            canvas.drawText(textList.get(i), x, y, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChooseIndex = activedIndex;
        int choosedIndex = (int) ((y - getPaddingTop()) / (getHeight() - getPaddingTop() - getPaddingBottom()) * textList.size());
        choosedIndex = choosedIndex < 0 ? 0 : choosedIndex;
        choosedIndex = choosedIndex >= textList.size() ? textList.size() - 1 : choosedIndex;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mNavActive = true;
                tvLetterOverlay.setVisibility(VISIBLE);

                if (oldChooseIndex != choosedIndex && onTouchingLetterChangedListener != null) {
                    if (choosedIndex >= 0 && choosedIndex < textList.size()) {
                        activedIndex = choosedIndex;
                        refreshTv();
                        invalidate();
                        onTouchingLetterChangedListener.onTouchingLetterChanged(choosedIndex, textList.get(choosedIndex));
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChooseIndex != choosedIndex && onTouchingLetterChangedListener != null) {
                    if (choosedIndex >= 0 && choosedIndex < textList.size()) {
                        activedIndex = choosedIndex;
                        refreshTv();
                        invalidate();
                        onTouchingLetterChangedListener.onTouchingLetterChanged(choosedIndex, textList.get(choosedIndex));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mNavActive = false;
                tvLetterOverlay.setVisibility(GONE);
                activedIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    private void refreshTv() {
        tvLetterOverlay.setText(textList.get(activedIndex));
        tvLetterOverlay.setTextSize(overlyTextSize / textList.get(activedIndex).length());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setLetterChangeListener(LetterChangeListener listener) {
        this.onTouchingLetterChangedListener = listener;
    }

    public interface LetterChangeListener {
        void onTouchingLetterChanged(int index, String text);
    }
}