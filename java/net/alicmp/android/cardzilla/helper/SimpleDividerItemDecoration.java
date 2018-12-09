package net.alicmp.android.cardzilla.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.alicmp.android.cardzilla.R;

/**
 * Created by ali on 8/21/15.
 */
public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private Context mContext;
    private int mPadding;

    public SimpleDividerItemDecoration(Context context, int padding) {
        this.mContext = context;
        this.mDivider = ContextCompat.getDrawable(mContext, R.drawable.list_divider);
        this.mPadding = padding;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + paddingInDP(mPadding);
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for(int i=0; i<childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public int paddingInDP(int padding_in_dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return ((int) (padding_in_dp * scale + 0.5f));
    }
}
