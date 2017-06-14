package com.yqq.idear;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者:geminiyang on 2017/6/12.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CustomRecyclerView extends RecyclerView implements DataStateChangeCheck.LoadDataListener{

    // 服务器端一共多少条数据 拥有默认值
    private int totalCount = 4;
    // 每一页展示多少条数据
    private int requestCount = 2;
    //当前个数
    private int currentCount = 0;

    /**
     * footer的四种状态
     */
    public enum FooterState {
        NORMAL,
        LOADING,
        END,
        NETWORK_ERROR
    }

    /**
     * footer的四种状态
     */
    public enum HeaderState {
        NONE,
        LOADING,
        FINISH
    }

    /**
     * 对外的数据操作接口
     */
    public interface DataOperation {
        void onLoadMore();
        void onRefresh();
    }

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_HEADER = 3;

    private Context mContext;
    private Adapter mAdapter;

    private View headerView;
    private View footerView;

    protected FooterState mFooterState = FooterState.NORMAL;//默认为Normal状态
    protected HeaderState mHeaderState = HeaderState.NONE;//默认为None状态

    private DataStateChangeCheck mDataEndListener;//数据状态监听器

    private DataOperation dataOperation;
    private InnerHandler handler = new InnerHandler();

    public CustomRecyclerView(Context context) {
        super(context,null);
        init(context);
    }



    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        init(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        headerView = null;
        footerView = null;
    }

    /**
     * 重写点
     * @param adapter
     */
    @Override
    public void setAdapter(Adapter adapter) {
        CustomRecyclerView.WrapAdapter tempAdapter = new WrapAdapter(adapter);
        super.setAdapter(tempAdapter);
        this.mAdapter = tempAdapter;
    }

    /**
     * 添加头布局
     */
    public void addHeaderView(Context context) {
        headerView = null;
        headerView = LayoutInflater.from(context).inflate(R.layout.header_holder,null);
        if(this.mAdapter != null && (this.mAdapter instanceof CustomRecyclerView.WrapAdapter)) {
            ((WrapAdapter)mAdapter).addHeaderView(headerView);
        }
    }

    /**
     *  添加尾布局
     */
    public void addFooterView(Context context) {
        footerView = null;
        footerView = LayoutInflater.from(context).inflate(R.layout.footer_holder,null);;
        if(this.mAdapter != null && (this.mAdapter instanceof CustomRecyclerView.WrapAdapter)) {
            ((WrapAdapter)mAdapter).addFooterView(footerView);
        }
    }

    /**
     * 改变Footer状态的操作
     * @param mState
     */
    private void setFooterState(FooterState mState) {
        this.mFooterState = mState;
        post(new Runnable() {
            @Override
            public void run() {
                changeFooterState();
            }
        });
    }

    private void setHeaderState(HeaderState mState) {
        this.mHeaderState = mState;
        post(new Runnable() {
            @Override
            public void run() {
                changeHeaderState();
            }
        });
    }

    //改变Adapter中Footer底部bottom的样式
    private void changeFooterState() {
        if(footerView!=null) {
            CustomRecyclerView.WrapAdapter adapter = ((WrapAdapter) mAdapter);
            if (adapter != null && adapter.getFooterHolder() != null) {
                adapter.getFooterHolder().setState(mFooterState);
            }
        }
    }

    //改变Adapter中顶部bottom的样式
    private void changeHeaderState() {
        if(headerView!=null) {
            CustomRecyclerView.WrapAdapter adapter = ((WrapAdapter) mAdapter);
            if (adapter != null && adapter.getHeaderHolder() != null) {
                adapter.getHeaderHolder().setState(mHeaderState);
            }
        }
    }

    public FooterState getFooterState() {
        return this.mFooterState;
    }
    public HeaderState getHeaderState() {
        return this.mHeaderState;
    }

    public void setTotalCount(int values) {
        this.totalCount = values;
    }

    public void setRequestCount(int values) {
        this.requestCount = values;
    }

    /**
     * 必须设置的数据操作接口
     * @param dataOperation
     */
    public void setDataOperation(DataOperation dataOperation) {
        this.dataOperation = dataOperation;
    }


    /**
     * 将数据操作与外部方法关联
     */
    public void Initialize() {
        if(this.dataOperation!=null) {
            dataOperation.onLoadMore();
        }
    }

    /**
     * 将数据操作与外部方法关联(下拉加载)
     */
    private void load() {
        if(this.dataOperation!=null) {
            dataOperation.onLoadMore();
        }
    }

    /**
     * 将数据操作与外部方法关联（上拉刷新）
     */
    private void refresh() {
        if(this.dataOperation!=null) {
            dataOperation.onRefresh();
        }
    }

    /**
     * 数据上拉加载加载操作
     */
    private void loadMoreData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //模拟网络连接
                if (NetWorkUtil.isNetworkConnected(mContext)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //模拟请求远程数据
                    load();
                    Looper.prepare();
                    Message msg = new Message();
                    msg.arg1 = 1;
                    handler.handleMessage(msg);
                    Looper.loop();
                } else {
                    //模拟一下网络请求失败的情况
                    setFooterState(FooterState.NETWORK_ERROR);
                }
            }
        }).start();

    }

    /**
     * 数据下拉刷新操作
     */
    private void refreshMoreData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //模拟网络连接
                if (NetWorkUtil.isNetworkConnected(mContext)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //模拟请求远程数据
                    refresh();
                    Log.d("info", "Mode:Refresh ,the state is finish, just wait..");
                    setHeaderState(HeaderState.FINISH);
                    Looper.prepare();
                    Message msg = new Message();
                    msg.arg1 = 2;
                    handler.handleMessage(msg);
                    Looper.loop();
                } else {
                    Looper.prepare();
                    //模拟一下网络请求失败的情况
                    ToastUtil.getInstance().showToast(mContext,"网络错误");
                    Looper.loop();
                }
            }
        }).start();
    }

    /**
     * 上拉加载自定义实现
     * @param view
     */
    @Override
    public void onLoadNextPage(View view) {
        //对应加载更多操作
        if (getFooterState() == FooterState.LOADING) {
            Log.d("info", "Mode:LOAD ,the state is Loading, just wait..");
            //正在加载中
            return;
        }
        //加载数据(根据界面显示个数和服务器返回的数据总数实现动态加载)
        if(currentCount == 0) {
            currentCount = requestCount;
        } else {
            //更新当前拥有项
            currentCount =  mAdapter.getItemCount() - ((CustomRecyclerView.WrapAdapter)mAdapter).
                    getHeaderCount() - ((CustomRecyclerView.WrapAdapter)mAdapter).getFooterCount()
                    + requestCount;
        }
        if (currentCount <= totalCount) {
            setFooterState(FooterState.LOADING);
            //载入更多数据
            loadMoreData();
        } else {
            //无数据
            setFooterState(FooterState.END);
        }

    }

    /**
     * 下拉刷新自定义实现(到达顶部触发)
     * @param view
     */
    @Override
    public void onRefreshPage(View view) {
        //对应加载更多操作
        if (getHeaderState() == HeaderState.LOADING) {
            Log.d("info", "Mode:refresh ,the state is Loading, just wait..");
            //正在加载中
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("info", "Mode:REFRESHING ,the state is Loading, just wait..");
                setHeaderState(HeaderState.LOADING);
                //模拟耗时操作
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //执行添加新数据操作
                refreshMoreData();
            }
        }).start();
    }

    /**
     * 添加预定义的滑动监听器
     * @param mDataEndListener  预定义的滑动监听器
     */
    public void addDataChangeListener(DataStateChangeCheck mDataEndListener) {
        this.mDataEndListener = mDataEndListener;
        addOnScrollListener(mDataEndListener);
    }

    /**
     * 内部包装Adapter，需要和外部Adapter结合
     */
    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter mAdapter;//容器Adapter
        private View mHeaderView, mFooterView;

        private InnerFooterHolder mFooterHolder;
        private InnerHeaderHolder mHeaderHolder;
        private int mHeaderCount;
        private int mFooterCount;

        public WrapAdapter(Adapter adapter) {
            this.mAdapter = adapter;
            this.mHeaderCount = 0;
            this.mFooterCount = 0;
        }

        public WrapAdapter(Adapter adapter,View mHeaderView, View mFooterView) {
            this.mAdapter = adapter;
            this.mHeaderView = mHeaderView;
            this.mFooterView = mFooterView;
            this.mHeaderCount = 1;
            this.mFooterCount = 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_NORMAL:
                    return this.mAdapter.onCreateViewHolder(parent,viewType);
                case TYPE_FOOTER:
                    mFooterHolder = new InnerFooterHolder(mFooterView);
                    return mFooterHolder;
                case TYPE_HEADER:
                    mHeaderHolder = new InnerHeaderHolder(mHeaderView);
                    return mHeaderHolder;
                default:
                    break;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            int realPosition;
            if((mFooterCount == 0 && mHeaderCount == 0) ||
                    (mFooterCount == 1 && mHeaderCount == 0)) {
                realPosition = position;
            } else {
                realPosition = position - 1;
            }
            switch (viewType) {
                case TYPE_NORMAL:
                    this.mAdapter.onBindViewHolder(holder,realPosition);
                    break;
                case TYPE_FOOTER:
                    //默认为Normal状态
                    ((InnerFooterHolder)holder).setState(FooterState.NORMAL);
                    break;
                case TYPE_HEADER:
                    ((InnerHeaderHolder)holder).setState(HeaderState.NONE);
                    break;
                default:
                    break;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if(mFooterCount == 1 && mHeaderCount == 1) {
                return position == (getItemCount() - 1) ?
                        TYPE_FOOTER : (position == 0 ? TYPE_HEADER : TYPE_NORMAL);
            } else if(mFooterCount == 1 && mHeaderCount == 0) {
                return position == (getItemCount() - 1) ? TYPE_FOOTER : TYPE_NORMAL;
            } else  if(mFooterCount == 0 && mHeaderCount == 1) {
                return position == 0 ? TYPE_HEADER : TYPE_NORMAL;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public int getItemCount() {
            return this.mAdapter.getItemCount() == 0 ? 0 : (this.mAdapter.getItemCount() + mFooterCount + mHeaderCount);
        }

        public int getHeaderCount() {
            return this.mHeaderCount;
        }

        public int getFooterCount() {
            return this.mFooterCount;
        }

        public void addHeaderView(View mHeaderView) {
            this.mHeaderCount = 1;
            this.mHeaderView = mHeaderView;
            this.notifyDataSetChanged();
        }

        public void addFooterView(View mFooterView) {
            this.mFooterCount = 1;
            this.mFooterView = mFooterView;
            this.notifyDataSetChanged();
        }

        public InnerFooterHolder getFooterHolder() {
            return this.mFooterHolder;
        }

        public InnerHeaderHolder getHeaderHolder() {
            return this.mHeaderHolder;
        }

        class InnerFooterHolder extends ViewHolder {
            private View mLoadingViewStub;
            private View mEndViewStub;
            private View mNetworkErrorViewStub;

            public InnerFooterHolder(View itemView) {
                super(itemView);
                //绑定视图id
                mLoadingViewStub = itemView.findViewById(R.id.loading_viewStub);
                mEndViewStub = itemView.findViewById(R.id.end_viewStub);
                mNetworkErrorViewStub = itemView.findViewById(R.id.network_error_viewStub);
            }

            //根据传过来的status控制哪个状态可见
            public void setState(FooterState status) {
                switch (status) {
                    case NORMAL:
                        setAllGone();
                        break;
                    case LOADING:
                        setAllGone();
                        mLoadingViewStub.setVisibility(View.VISIBLE);
                        break;
                    case END:
                        setAllGone();
                        mEndViewStub.setVisibility(View.VISIBLE);
                        break;
                    case NETWORK_ERROR:
                        setAllGone();
                        mNetworkErrorViewStub.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }

            }
            //设置全部布局不可见
            private void setAllGone() {
                if (mLoadingViewStub != null) {
                    mLoadingViewStub.setVisibility(View.GONE);
                }
                if (mEndViewStub != null) {
                    mEndViewStub.setVisibility(View.GONE);
                }
                if (mNetworkErrorViewStub != null) {
                    mNetworkErrorViewStub.setVisibility(View.GONE);
                }
            }
        }
        class InnerHeaderHolder extends ViewHolder{
            private View one;
            private View two;

            public InnerHeaderHolder(View itemView) {
                super(itemView);
                //绑定视图id
                one = itemView.findViewById(R.id.rl_one);
                two = itemView.findViewById(R.id.rl_two);
            }

            //根据传过来的status控制哪个状态可见
            public void setState(HeaderState status) {
                switch (status) {
                    case NONE:
                        setAllGone();
                        break;
                    case LOADING:
                        setAllGone();
                        one.setVisibility(VISIBLE);
                        break;
                    case FINISH:
                        setAllGone();
                        two.setVisibility(VISIBLE);
                        break;
                    default:
                        break;
                }
            }

                //设置全部布局不可见
                private void setAllGone() {
                    if (one != null) {
                        one.setVisibility(GONE);
                    }
                    if (two != null) {
                        two.setVisibility(GONE);
                    }
                }
        }


    }
    /**
     * 自定义Handler，实现内部消息传递
     */
    class InnerHandler extends Handler {
        private InnerHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //mAdapter.notifyDataSetChanged();
                            //上拉加载加载完毕时
                            setFooterState(FooterState.NORMAL);
                        }
                    },1500);
                    break;
                case 2:
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //下拉刷新完毕时
                            setHeaderState(HeaderState.NONE);
                            scrollToPosition(mDataEndListener.getFirstVisibleItemPosition());
                        }
                    },500);
                    break;
                default:
                    break;
            }
        }
    }
}
