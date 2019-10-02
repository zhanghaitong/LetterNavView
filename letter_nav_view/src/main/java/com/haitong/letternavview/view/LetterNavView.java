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


/**
 * Created by next on 2016/3/24.
 */
public class LetterNavView extends View {

    private static final String TAG = "LetterNavView";

    private float navTvTextSize;
    private float navTvTextSelectedSize;
    private int navTvTextColor;
    private int navTvTextSelectedColor;
    private int navBgColor;
    private int navBgActiveColor;
    private float navPaddingLeft;
    private float navPaddingRight;
    private float navPaddingTop;
    private float navPaddingBottom;

    private Context mContext;

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private String[] navTextArray = {"定位", "热门", "最近", "全部", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    int activedIndex = -1;
    private Paint paint = new Paint();
    private boolean navActive = false;
    private TextView tvLetterOverlay;

    public LetterNavView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public LetterNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        Log.d(TAG, "LetterNavView() called with: context = [" + context + "], attrs = [" + attrs + "]");

        //加载自定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LetterNavView);
        navTvTextSize = a.getDimension(R.styleable.LetterNavView_navTvTextSize, DensityUtil.sp2px(context, 10));
        navTvTextSelectedSize = a.getDimension(R.styleable.LetterNavView_navTvTextSelectedSize, DensityUtil.sp2px(context, 10));
        navTvTextColor = a.getColor(R.styleable.LetterNavView_navTvTextColor, Color.parseColor("#8c8c8c"));
        navTvTextSelectedColor = a.getColor(R.styleable.LetterNavView_navTvTextSelectedColor, Color.parseColor("#40c60a"));
        navBgColor = a.getColor(R.styleable.LetterNavView_navBgColor, Color.TRANSPARENT);
        navBgActiveColor = a.getColor(R.styleable.LetterNavView_navBgActiveColor, Color.parseColor("#40000000"));
        navPaddingLeft = a.getDimension(R.styleable.LetterNavView_navPaddingLeft, 0);
        navPaddingRight = a.getDimension(R.styleable.LetterNavView_navPaddingRight, 0);
        navPaddingTop = a.getDimension(R.styleable.LetterNavView_navPaddingTop, 10);
        navPaddingBottom = a.getDimension(R.styleable.LetterNavView_navPaddingBottom, 10);

        LayoutInflater inflater = LayoutInflater.from(context);
        tvLetterOverlay = (TextView) inflater.inflate(R.layout.v_letter_overlay, null);
        tvLetterOverlay.setVisibility(View.INVISIBLE);

        int width = DensityUtil.dp2px(context, 65);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                width, width,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(tvLetterOverlay, layoutParams);

        setPadding((int) navPaddingLeft, (int) navPaddingTop, (int) navPaddingRight, (int) navPaddingBottom);

        //回收资源，这一句必须调用
        a.recycle();
    }

    public LetterNavView(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");
        super.onDraw(canvas);

        canvas.drawColor(navBgColor);

        if (navActive) {
            canvas.drawColor(navBgActiveColor);
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingBottom - paddingTop;
        int singleHeight = (height - paddingTop - paddingBottom) / navTextArray.length;
        for (int i = 0; i < navTextArray.length; i++) {
            paint.setColor(navTvTextColor);
            paint.setTextSize(navTvTextSize);
//            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            if (i == activedIndex) {
                paint.setColor(navTvTextSelectedColor);
                paint.setTextSize(navTvTextSelectedSize);
//                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(navTextArray[i]) / 2;
            float yPos = paddingTop + singleHeight * i + singleHeight;

            canvas.drawText(navTextArray[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = activedIndex;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) ((y - getPaddingTop()) / (getHeight() - getPaddingTop() - getPaddingBottom()) * navTextArray.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                navActive = true;
                tvLetterOverlay.setVisibility(VISIBLE);
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < navTextArray.length) {
                        listener.onTouchingLetterChanged(navTextArray[c]);
                        activedIndex = c;
                        refreshTv();
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < navTextArray.length) {
                        listener.onTouchingLetterChanged(navTextArray[c]);
                        activedIndex = c;

                        refreshTv();

                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                navActive = false;
                tvLetterOverlay.setVisibility(GONE);
                activedIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    private void refreshTv() {
        tvLetterOverlay.setText(navTextArray[activedIndex]);

        if (navTextArray[activedIndex].length() <= 1) {
            tvLetterOverlay.setTextSize(40);
        } else {
            tvLetterOverlay.setTextSize(20);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        this.onTouchingLetterChangedListener = listener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}