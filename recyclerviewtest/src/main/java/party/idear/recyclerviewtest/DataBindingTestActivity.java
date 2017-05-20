package party.idear.recyclerviewtest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import party.idear.recyclerviewtest.databinding.ActivityDataBindingTestBinding;


public class DataBindingTestActivity extends AppCompatActivity {

    User user = new User("man","yangqq");

    private ActivityDataBindingTestBinding activityDataBindingTestBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_data_binding_test);
        activityDataBindingTestBinding = DataBindingUtil
                .setContentView(this,R.layout.activity_data_binding_test);
        activityDataBindingTestBinding.setUser(user);
        activityDataBindingTestBinding.setPresenter(new Presenter());
        //activityDataBindingTestBinding.setVariable(party.idear.recyclerviewtest.BR.user,user);
    }

    public class Presenter {
        //方法引用
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            user.name = s.toString();
            activityDataBindingTestBinding.setUser(user);
        }
        public void onClick(View view) {
            Toast.makeText(DataBindingTestActivity.this,"CLICK",Toast.LENGTH_SHORT).show();
        }
        //监听器绑定
        public void onClickListener(User user) {
            Toast.makeText(DataBindingTestActivity.this,user.name,Toast.LENGTH_SHORT).show();
        }
    }
}
