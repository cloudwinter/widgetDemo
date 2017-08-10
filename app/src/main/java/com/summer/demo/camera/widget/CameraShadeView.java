package com.summer.demo.camera.widget;

import com.summer.demo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 中间镂空外层覆盖阴影的view <br>
 * 动态设置镂空区域 <br>
 * 形状（矩形或者正方形）和大小
 */
public class CameraShadeView extends FrameLayout {

    private static final int SHAPE_RECTANGLE = 0;
    private static final int SHAPE_SQUARE = 1;
    private static final int GRAVITY_TOP = 0;
    private static final int GRAVITY_CENTER = 1;
    private static final int GRAVITY_BOTTOM = 2;

    private Context mContext;
    private Paint mShadePaint;
    // 镂空区域
    private Rect mHollowRect = new Rect();
    // 四周阴影和镂空区域的距离
    private RectF mHollowMarginRect = new RectF();
    private int mHollowShape;
    private int mHollowGravity;

    public CameraShadeView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public CameraShadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // 关闭View硬件加速 否则部分手机Region.Op.XOR无效果
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (attrs != null) {
            TypedArray array = mContext.obtainStyledAttributes(attrs,
                    R.styleable.CameraShadeView);
            mHollowShape = array
                    .getInt(R.styleable.CameraShadeView_hollow_shape, 0);
            mHollowGravity = array
                    .getInt(R.styleable.CameraShadeView_hollow_gravity, 2);
            int rectLeft = array.getDimensionPixelSize(
                    R.styleable.CameraShadeView_hollow_margin_left, 0);
            int rectTop = array.getDimensionPixelSize(
                    R.styleable.CameraShadeView_hollow_margin_top, 0);
            int rectRight = array.getDimensionPixelSize(
                    R.styleable.CameraShadeView_hollow_margin_right, 0);
            int rectBottom = array.getDimensionPixelSize(
                    R.styleable.CameraShadeView_hollow_margin_bottom, 0);

            mHollowMarginRect.left = rectLeft;
            mHollowMarginRect.top = rectTop;
            mHollowMarginRect.right = rectRight;
            mHollowMarginRect.bottom = rectBottom;

            array.recycle();
        }
        setWillNotDraw(false);
        mShadePaint = new Paint();
        mShadePaint.setAntiAlias(true);
        mShadePaint.setColor(getResources().getColor(R.color.colorShade));
    }

    /**
     * 获取镂空区域
     * <p>
     * 最好异步调用
     * </p>
     *
     * @return
     */
    public Rect getHollowRect() {
        return mHollowRect;
    }

    /**
     * 设置镂空的区域的margin
     *
     * @param marginRect
     */
    public void setHollowMarginRect(RectF marginRect) {
        if (marginRect == null) {
            return;
        }
        mHollowMarginRect.set(marginRect);
        postInvalidate();
    }

    /**
     * 设置镂空区域的形状<br>
     * 0：矩形<br>
     * 1：正方形
     *
     * @param shape
     */
    public void setHollowShape(int shape) {
        mHollowShape = shape;
    }

    /**
     * 设置镂空的位置
     * <p>
     * 只有形状为正方形时才生效
     * </p>
     * <p>
     * 0上方top生效,1中间，2下方 bottom生效
     * </p>
     *
     * @param gravity
     */
    public void setHollowGravity(int gravity) {
        mHollowGravity = gravity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        canvas.save();
        canvas.clipRect(0, 0, viewWidth, viewHeight);
        RectF clipRect = new RectF();
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        if (mHollowShape == SHAPE_RECTANGLE) {
            left = mHollowMarginRect.left;
            top = mHollowMarginRect.top;
            right = viewWidth - mHollowMarginRect.right;
            bottom = viewHeight - mHollowMarginRect.bottom;
        } else if (mHollowShape == SHAPE_SQUARE) {
            switch (mHollowGravity) {
                case GRAVITY_TOP:
                    left = mHollowMarginRect.left;
                    top = mHollowMarginRect.top;
                    right = viewWidth - mHollowMarginRect.right;
                    bottom = right - left + top;
                    break;
                case GRAVITY_CENTER:
                    left = mHollowMarginRect.left;
                    right = viewWidth - mHollowMarginRect.right;
                    top = (viewHeight - (right - left)) / 2 + (right - left);
                    bottom = right - left + top;
                    break;
                case GRAVITY_BOTTOM:
                    left = mHollowMarginRect.left;
                    right = viewWidth - mHollowMarginRect.right;
                    top = viewHeight - (right - left) - mHollowMarginRect.bottom;
                    bottom = (right - left) + top;
                    break;
                default:
                    break;
            }
        }
        clipRect.set(left, top, right, bottom);
        mHollowRect.set((int) left, (int) top, (int) right, (int) bottom);
        canvas.clipRect(clipRect, Region.Op.XOR);
        canvas.drawRect(0, 0, viewWidth, viewHeight, mShadePaint);
        canvas.restore();
    }
}
