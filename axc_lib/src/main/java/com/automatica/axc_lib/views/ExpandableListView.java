package com.automatica.axc_lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ExpandableListView extends ListView
{

        public ExpandableListView (Context context, AttributeSet attrs)
        {
            super(context, attrs);
        }

        public ExpandableListView (Context context)
        {
            super(context);
        }

        public ExpandableListView (Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }


}
