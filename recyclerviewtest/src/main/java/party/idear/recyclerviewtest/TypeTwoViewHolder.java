package party.idear.recyclerviewtest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2017/5/5.
 */

public class TypeTwoViewHolder extends TypeAbstractViewHolder<DataModelTwo>{

    public TextView name;
    public TextView content;
    public ImageView avatar;

    public TypeTwoViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        content = (TextView) itemView.findViewById(R.id.content);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);

        itemView.setBackgroundColor(itemView.getResources().getColor(R.color._grey));
    }

    @Override
    public void bindHolder(DataModelTwo model) {
        avatar.setBackgroundResource(model.avatarColor);
        name.setText(model.name);
        content.setText(model.content);
    }


}
