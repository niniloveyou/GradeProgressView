package deadline.grade;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author deadline
 * @time 2016/9/24
 */
public class GradeProgressView extends View {


    private static final String TAG = GradeProgressView.class.getSimpleName();

    private static final int DEFAULT_PROGRESS_COLOR = Color.WHITE;
    private static final int DEFAULT_BACKGROUND_COLOR = 0x5AFFFFFF;

    private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
    private int mProgressColor = DEFAULT_PROGRESS_COLOR;

    //进度条的每格线宽，间距，长度
    private int dashWith = 4;
    private int dashSpace = 6;
    private int lineWidth = 60;

    //最外圈线的宽度和与进度条之间的间距
    private int outLineWidth = 5;
    private int gapWidth = 25;

    //指针的线宽度， 半径， 以及指针与进度条的间距
    private int pointLineWidth = 10;
    private int pointRadius = 25;
    private int pointGap = 20;

    private int mProgress = 0;

    //外线
    private RectF mOuterRectF;
    private Paint mOuterPaint;

    //进度条
    private RectF mRectF;
    private Paint mPaint;
    private Paint mProgressPaint;

    //指针
    private Paint mInnerCirclePaint;
    private Paint mPointerPaint;
    private Path  mPointerPath;

    private float centerX;
    private float centerY;

    private ValueAnimator animator;

    private OnProgressChangeListener mListener;

    public GradeProgressView(Context context) {
        this(context, null);
    }

    public GradeProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradeProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradeProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void setup() {

        mRectF = new RectF();
        mOuterRectF = new RectF();

        mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterPaint.setStrokeWidth(outLineWidth);
        mOuterPaint.setColor(mBackgroundColor);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(mBackgroundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace}, dashSpace));

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStrokeWidth(lineWidth);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace}, dashSpace));

        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setStrokeWidth(pointLineWidth / 2);
        mPointerPaint.setColor(mProgressColor);
        mPointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPointerPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointerPaint.setShadowLayer(4, 3, 0, 0x20000000);

        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setStrokeWidth(pointLineWidth);
        mInnerCirclePaint.setColor(mProgressColor);
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);
        mInnerCirclePaint.setShadowLayer(4, 3, 0, 0x20000000);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int value = outLineWidth / 2;
        mOuterRectF.set(value, value, w - value, h - value);

        int gap = lineWidth / 2 + outLineWidth + gapWidth;

        mRectF.set(mOuterRectF.left + gap,
                mOuterRectF.top + gap,
                mOuterRectF.right - gap,
                mOuterRectF.bottom - gap);

        centerX = mRectF.centerX();
        centerY = mRectF.centerY();

        mPointerPath = new Path();
        mPointerPath.moveTo(centerX + pointRadius, centerY - 7);
        mPointerPath.lineTo(centerX + pointRadius, centerY + 7);
        mPointerPath.lineTo(mRectF.right - pointGap - lineWidth / 2, centerY);
        mPointerPath.lineTo(centerX + pointRadius, centerY - 7);
        mPointerPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float degree = 2.7f * mProgress;

        //draw out arc
        canvas.drawArc(mOuterRectF, 135, 270, false, mOuterPaint);

        //draw background arc
        canvas.drawArc(mRectF, 135 + degree, 270 - degree, false, mPaint);

        //draw progress arc
        canvas.drawArc(mRectF, 135, degree, false, mProgressPaint);


        //draw pointer
        canvas.drawCircle(centerX, centerY, pointRadius, mInnerCirclePaint);

        canvas.save();
        canvas.rotate(135 + degree, centerX, centerY);
        canvas.drawPath(mPointerPath, mPointerPaint);
        canvas.restore();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(measureHeight, measureWidth), Math.min(measureHeight, measureWidth));
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener){
        this.mListener = listener;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
        if(mProgressPaint != null){
            mProgressPaint.setColor(mProgressColor);
        }

        if(mPointerPaint != null){
            mPointerPaint.setColor(mProgressColor);
        }

        if(mInnerCirclePaint != null){
            mInnerCirclePaint.setColor(mProgressColor);
        }
        postInvalidate();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {

        this.mBackgroundColor = backgroundColor;
        if(mPaint != null){
            mPaint.setColor(mBackgroundColor);
        }

        if(mOuterPaint != null){
            mOuterPaint.setColor(mBackgroundColor);
        }
        postInvalidate();
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        if(mPaint != null){
            mPaint.setStrokeWidth(lineWidth);
        }

        if(mProgressPaint != null){
            mProgressPaint.setStrokeWidth(lineWidth);
        }
        postInvalidate();
    }

    public int getOutLineWidth() {
        return outLineWidth;
    }

    public void setOutLineWidth(int outLineWidth) {
        this.outLineWidth = outLineWidth;
        if(mOuterPaint != null){
            mOuterPaint.setStrokeWidth(outLineWidth);
        }
        postInvalidate();
    }

    public int getGapWidth() {
        return gapWidth;
    }

    public void setGapWidth(int gapWidth) {
        this.gapWidth = gapWidth;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(@IntRange(from = 0, to = 100) int progress) {
        if(progress > 100){
            progress = 100;
        }

        if(progress < 0){
            progress = 0;
        }
        this.mProgress = progress;
        if(mListener != null){
            mListener.onProgressChanged(GradeProgressView.this, mProgress);
        }
        postInvalidate();
    }

    public void setProgressWidthAnimation(@IntRange(from = 0, to = 100) int progress){

        if(progress > 100){
            progress = 100;
        }

        if(progress < 0){
            progress = 0;
        }

        if(animator != null && animator.isRunning()){
            animator.cancel();
            animator = null;
        }

        animator = ValueAnimator.ofInt(mProgress, progress);
        int duration = 10 * Math.abs(progress - mProgress);
        animator.setDuration(duration);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int)valueAnimator.getAnimatedValue();
                if(mProgress != value) {
                    mProgress = value;
                    if(mListener != null){
                        mListener.onProgressChanged(GradeProgressView.this, mProgress);
                    }
                    postInvalidate();
                }
            }
        });
        animator.start();

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(animator != null){
            animator.cancel();
            animator = null;
        }

    }


    public interface OnProgressChangeListener{

        void onProgressChanged(GradeProgressView gradeProgressView, int progress);
    }
}
