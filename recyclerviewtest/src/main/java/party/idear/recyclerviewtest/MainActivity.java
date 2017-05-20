package party.idear.recyclerviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DemoAdapter mAdapter;

    private int[] colors = {R.color.colorAccent,
            R.color.colorPrimary,R.color.colorPrimaryDark};

    protected LoadingFooter.FooterState mState = LoadingFooter.FooterState.NORMAL;
    // 服务器端一共多少条数据
    private static final int TOTAL_COUNTER = 25;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 5;
    // 已经获取到多少条数据了
    private int mCurrentCounter = 0;
    //模拟的数据源
    private List<DataModelOne> listOneData;
    private List<DataModelTwo> listTwoData;
    private List<DataModelThree> listThreeData;


    protected void setState(LoadingFooter.FooterState mState) {
        this.mState = mState;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeAdapterState();
            }
        });
    }

    //改变底部bottom的样式
    protected void changeAdapterState() {
        if (mAdapter != null && mAdapter.mFooterHolder != null) {
            mAdapter.mFooterHolder.setState(mState);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        recyclerView.addOnScrollListener(mListener);
        //垂直布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = recyclerView.getAdapter().getItemViewType(position);
                if(viewType == DataModel.TYPE_THREE || viewType == DataModel.TYPE_FOOTER) {
                    return gridLayoutManager.getSpanCount();//spanCount是初始化时候设置的参数
                } else if(position == recyclerView.getAdapter().getItemCount() - 1) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }

            }
        });

        //网格布局管理器
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new DemoAdapter(this);
        recyclerView.setAdapter(mAdapter);
        //初始化数据
        //initData();
        initDataOther();
        //getRemoteDataTypeOne();
       // mAdapter.addAllThree(getRemoteDataTypeThree());
//        getRemoteDataTypeTwo();
//        getRemoteDataTypeThree();

        RecyclerViewDivider divider = new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 2, getResources().getColor(R.color.black));
        divider.setMyGridLayoutManager(gridLayoutManager);
        divider.setItemMargin(2);
        recyclerView.addItemDecoration(divider);
    }

    private void initDataOther(){
        List<DataModelOne> listOne = new ArrayList<>();
        List<DataModelTwo> listTwo = new ArrayList<>();
        List<DataModelThree> listThree = new ArrayList<>();

        for(int i=0; i<10; i++) {
            DataModelOne data = new DataModelOne();
            data.avatarColor = colors[0];
            data.name = "name:" + (i+1);
            listOne.add(data);
            mCurrentCounter ++;
        }

        for(int i=0; i<10; i++) {
            DataModelTwo data = new DataModelTwo();
            data.avatarColor = colors[1];
            data.name = "name:" + (i+1);
            data.content = "content:" + (i+1);
            listTwo.add(data);
            mCurrentCounter ++;
        }

        for(int i=0; i<5; i++) {
            DataModelThree data = new DataModelThree();
            data.avatarColor = colors[2];
            data.name = "name:" + (i+1);
            data.content = "content:" + (i+1);
            data.contentColor = colors[0];
            listThree.add(data);
            mCurrentCounter ++;
        }
        mAdapter.addLists(listOne,listTwo,listThree);
        mAdapter.notifyDataSetChanged();
        //为每一个Item设置点击事件
        mAdapter.setOnItemClickListener(new DemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"click" + (position+1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this,"long_click" + (position+1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        List<DataModel> lists = new ArrayList<DataModel>();
        for(int i=0; i<29; i++) {
           DataModel data = new DataModel();
            int type;// = (int) ((Math.random()*3) + 1);//[1,4)
            if(i<6) {
                type =DataModel.TYPE_ONE;
            } else if(i<10||i>20) {
                type = DataModel.TYPE_TWO;
            } else {
                type = DataModel.TYPE_THREE;
            }
            data.type = type;
            data.avatarColor = colors[type-1];
            data.name = "name:" + (i+1);
            data.content = "content:" + (i+1);
            data.contentColor = colors[(type+1)%3];
            lists.add(data);
        }
        mAdapter.addList(lists);
        mAdapter.notifyDataSetChanged();
        //为每一个Item设置点击事件
        mAdapter.setOnItemClickListener(new DemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this,"click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DataEndlessListener mListener = new DataEndlessListener() {

        /**
         * 到底部就会触发
         * @param view
         */
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            if (mState == LoadingFooter.FooterState.LOADING) {
                Log.d("info", "the state is Loading, just wait..");
                return;
            }
            //加载数据(根据界面显示个数和服务器返回的数据总数实现动态加载)
            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                //requestData(3);
                Log.d("TAG", "请求数据");
            } else {
                //the end
                setState(LoadingFooter.FooterState.END);
                ToastUtil.getInstance().showToast(MainActivity.this,"已经到了底部");
            }
        }
    };

    /**
      * 模拟请求网络
      */
    private void requestData(final int type) {
        setState(LoadingFooter.FooterState.LOADING);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (NetWorkUtil.isNetworkConnected(MainActivity.this)) {
                    //模拟请求远程数据
                    switch (type) {
                        case DataModel.TYPE_ONE:
                            mAdapter.addAllOne(getRemoteDataTypeOne());
                        case DataModel.TYPE_TWO:
                            mAdapter.addAllTwo(getRemoteDataTypeTwo());
                        case DataModel.TYPE_THREE:
                            mAdapter.addAllThree(getRemoteDataTypeThree());
                    }
                    //加载完毕时
                    setState(LoadingFooter.FooterState.NORMAL);
                    Log.d("info", mCurrentCounter + "");
                } else {
                    //模拟一下网络请求失败的情况
                    setState(LoadingFooter.FooterState.NETWORK_ERROR);
                }
            }
        }.start();
    }

    //模拟请求数据，类型1
    private List<DataModelOne> getRemoteDataTypeOne() {
        if (listOneData == null)
            listOneData = new ArrayList<>();
        //每次都清空一下
        listOneData.clear();
        //要减去adapter最后一页
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (listOneData.size() + mCurrentCounter >= TOTAL_COUNTER) {
                break;
            }
            DataModelOne data = new DataModelOne();
            data.avatarColor = colors[0];
            data.name = "name:" + (i+1);
            listOneData.add(data);
            mCurrentCounter++;
        }
        return listOneData;
    }
    //类型2
    private List<DataModelTwo> getRemoteDataTypeTwo() {
        if (listTwoData == null)
            listTwoData = new ArrayList<>();
        //每次都清空一下
        listTwoData.clear();
        //要减去adapter最后一页
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (listTwoData.size() + mCurrentCounter >= TOTAL_COUNTER) {
                break;
            }
            DataModelTwo data = new DataModelTwo();
            data.avatarColor = colors[1];
            data.name = "name:" + (i+1);
            data.content = "content:" + (i+1);
            listTwoData.add(data);
            mCurrentCounter++;
        }
        return listTwoData;
    }
    //类型3
    private List<DataModelThree> getRemoteDataTypeThree() {
        if (listThreeData == null)
            listThreeData = new ArrayList<>();
        //每次都清空一下
        listThreeData.clear();
        //要减去adapter最后一页
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (listThreeData.size() + mCurrentCounter >= TOTAL_COUNTER) {
                break;
            }
            DataModelThree data = new DataModelThree();
            data.avatarColor = colors[2];
            data.name = "name:" + (i+1);
            data.content = "content:" + (i+1);
            data.contentColor = colors[0];
            listThreeData.add(data);
            mCurrentCounter++;
        }
        return listThreeData;
    }
}
