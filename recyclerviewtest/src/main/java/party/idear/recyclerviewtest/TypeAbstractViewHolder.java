package party.idear.recyclerviewtest;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by user on 2017/5/5.
 */

public abstract class TypeAbstractViewHolder<T> extends RecyclerView.ViewHolder {

    public TypeAbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindHolder(T model);
}
