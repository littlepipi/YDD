package app.example.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareLayout extends RelativeLayout {
    public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //	widthMeasureSpec,heightMeasureSpec为宽详细测量值和高详细测量值
              setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
//              MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求
//              MeasureSpec由size和mode组成。
        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        //高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
