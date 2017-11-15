package com.riverlet.disordercard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Author liujian
 * Email: riverlet.liu@qq.com
 * Date: 2017/9/21.
 * Despribe:
 */

public class DisorderCardView extends ViewGroup implements View.OnClickListener {
    private static final String TAG = "DisorderCardView";
    private OnCarkClickListener onCarkClickListener;
    private int cardTextColor = 0xff4a4a4a;
    private int cardTextSize = 12;
    private int cardBackground = R.drawable.selector_card;
    private int gravity = Gravity.CENTER_HORIZONTAL;
    private Adapter adapter;
    private int realWidth;
    private int realHeight;
    private int horizontalSpace = 10;
    private int vertcalSpace = 10;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    public DisorderCardView(Context context) {
        super(context);
        init();
    }

    public DisorderCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DisorderCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //遍历子View，测量每个View的大小
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        realWidth = r - l - paddingLeft - paddingRight;
        realHeight = b - t - paddingTop - paddingBottom;
        Log.d(TAG, "realWidth:" + realWidth + "...realHeight:" + realHeight);
        computeAndLayout();
    }

    //计算每一行要显示的卡片
    private void computeAndLayout() {
        int childCount = getChildCount();
        if (realWidth > 0 && realHeight > 0 && childCount > 0) {
            int hadUsedWidth = 0;
            int hadUsedHeight = 0;
            int startIndex = 0;
            int endIndex = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View itemView = getChildAt(i);
                int cardWidth = itemView.getMeasuredWidth();
                int cardHeight = itemView.getMeasuredHeight();

                hadUsedWidth += cardWidth;
                if (hadUsedWidth > realWidth) {
                    endIndex = i - 1;
                    layoutItemInRaw(startIndex, endIndex, hadUsedHeight, hadUsedWidth - cardWidth - vertcalSpace);
                    hadUsedHeight += cardHeight + horizontalSpace;
                    hadUsedWidth = cardWidth;
                    startIndex = i;
                }

                if (i == childCount - 1) {
                    endIndex = i;
                    layoutItemInRaw(startIndex, endIndex, hadUsedHeight, hadUsedWidth);
                }

                hadUsedWidth += vertcalSpace;
            }
        }
    }

    //布局一行的卡片位置
    private void layoutItemInRaw(int startIndex, int endIndex, int hadUsedHeight, int widthInRaw) {
        Log.d(TAG, "layoutItem...startIndex:" + startIndex + "...endIndex:" + endIndex + "...widthInRaw:" + widthInRaw + "...realWidth:" + realWidth);
        int hadUsedWidth = 0;
        int leftSpace = 0;
        switch (gravity) {
            case Gravity.CENTER_HORIZONTAL:
                leftSpace = realWidth / 2 - widthInRaw / 2;
                break;
            case Gravity.LEFT:
                leftSpace = 0;
                break;
            case Gravity.RIGHT:
                leftSpace = realWidth - widthInRaw;
                break;
        }

        for (int i = startIndex; i <= endIndex; i++) {
            View itemView = getChildAt(i);
            int cardWidth = itemView.getMeasuredWidth();
            int cardHeight = itemView.getMeasuredHeight();
            hadUsedWidth += cardWidth;
            int left = hadUsedWidth - cardWidth + leftSpace + paddingLeft;
            int right = hadUsedWidth + leftSpace + paddingLeft;
            int top = hadUsedHeight + paddingTop;
            int bottom = hadUsedHeight + cardHeight + paddingTop;
            hadUsedWidth += vertcalSpace;
            Log.d(TAG, "left:" + left + "...right:" + right + "...top:" + top + "...bottom:" + bottom);
            itemView.layout(left, top, right, bottom);
        }
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        this.adapter.setView(this);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (onCarkClickListener != null) {
            onCarkClickListener.onCardClick(((TextView) view).getText().toString());
        }
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnCarkClickListener(OnCarkClickListener onCarkClickListener) {
        this.onCarkClickListener = onCarkClickListener;
    }

    public interface OnCarkClickListener {
        void onCardClick(String text);
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        requestLayout();
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public void setCardTextColor(int cardTextColor) {
        this.cardTextColor = cardTextColor;
        notifyDataSetChanged();
    }


    public void setCardTextSize(int cardTextSize) {
        this.cardTextSize = cardTextSize;
        notifyDataSetChanged();
    }

    public void setCardBackground(int cardBackground) {
        this.cardBackground = cardBackground;
        notifyDataSetChanged();
    }

    public void setData(List<String> dataList) {
        if (adapter == null) {
            adapter = new Adapter(getContext());
        }
        adapter.setData(dataList);
        setAdapter(adapter);
    }

    public static class Adapter {
        private Context context;
        private List<String> dataList;
        private DisorderCardView cardView;

        public Adapter(Context context) {
            this.context = context;
        }

        public Adapter(Context context, List<String> dataList) {
            this.context = context;
            setData(dataList);
        }

        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public View getItem(int position) {
            TextView itemView = new TextView(context);
            itemView.setTextSize(cardView.cardTextSize);
            itemView.setTextColor(cardView.cardTextColor);
            itemView.setText(dataList.get(position));
            itemView.setBackgroundResource(cardView.cardBackground);
            itemView.setPadding(
                    dip2px(context, 10),
                    dip2px(context, 5),
                    dip2px(context, 10),
                    dip2px(context, 5)
            );
            itemView.setOnClickListener(cardView);
            return itemView;
        }

        public void setView(DisorderCardView cardView) {
            this.cardView = cardView;
        }

        public void setData(List<String> dataList) {
            this.dataList = dataList;
        }

        public void notifyDataSetChanged() {
            cardView.removeAllViews();
            for (int i = 0; i < dataList.size(); i++) {
                cardView.addView(getItem(i), new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            cardView.requestLayout();
        }
    }

}
