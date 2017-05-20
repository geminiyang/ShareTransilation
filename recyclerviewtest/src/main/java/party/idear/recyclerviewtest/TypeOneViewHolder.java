package party.idear.recyclerviewtest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2017/5/5.
 */

public class TypeOneViewHolder  extends TypeAbstractViewHolder<DataModelOne>
                                            implements View.OnClickListener,View.OnLongClickListener {

    public TextView name;
    public ImageView avatar;

    private int RealPosition;
    private DemoAdapter.OnItemClickListener mItemClickListener;

    public TypeOneViewHolder(View itemView, DemoAdapter.OnItemClickListener itemClickListener) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        this.mItemClickListener = itemClickListener;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        itemView.setBackgroundColor(itemView.getResources().getColor(R.color._grey));
    }

    @Override
    public void bindHolder(final DataModelOne model) {
        avatar.setBackgroundResource(model.avatarColor);
        name.setText(model.name);
    }

    @Override
    public void onClick(View v) {
        // getLayoutPosition()为ViewHolder自带的一个方法，用来获取RecyclerView当前的位置，将此作为参数，传出去
        mItemClickListener.onItemClick(v,RealPosition);
    }

    public void setRealPosition(int realPosition) {
        this.RealPosition = realPosition;
    }

    @Override
    public boolean onLongClick(View v) {
        //如果返回值为true的话这个点击事件会被长点击独占
        mItemClickListener.onItemLongClick(v,RealPosition);
        return true;
    }
}
