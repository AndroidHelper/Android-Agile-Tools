package cn.core.mobile.library.tools;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Will.Wu on 2014/7/4.
 */
public class ViewHolder {
    private ViewHolder() {

    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View contentView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) contentView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            contentView.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = contentView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
