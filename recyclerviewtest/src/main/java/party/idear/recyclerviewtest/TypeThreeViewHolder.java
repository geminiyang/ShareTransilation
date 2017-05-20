package party.idear.recyclerviewtest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2017/5/5.
 */

public class TypeThreeViewHolder extends TypeAbstractViewHolder<DataModelThree>{

    public TextView name;
    public TextView content;
    public ImageView avatar;
    public ImageView contentImage;

    public TypeThreeViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        content = (TextView) itemView.findViewById(R.id.content);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        contentImage = (ImageView) itemView.findViewById(R.id.contentImage);

        itemView.setBackgroundColor(itemView.getResources().getColor(R.color.light_grey));
    }

    @Override
    public void bindHolder(DataModelThree model) {
        avatar.setBackgroundResource(model.avatarColor);
        contentImage.setBackgroundResource(model.contentColor);
        content.setText(model.content);
        name.setText(model.name);
    }


}
