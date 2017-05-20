package party.idear.recyclerviewtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import party.idear.recyclerviewtest.LoadingFooter.FooterState;

/**
 * Created by user on 2017/5/5.
 */

public class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;
    public static final int TYPE_FOOTER = 4;

    public FooterHolder mFooterHolder;

    private List<DataModel> mList;
    private Context context;
    private LayoutInflater mInflater;
    /**
     * 点击监听器
     */
    private OnItemClickListener mClickListener;


    public DemoAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(this.context);
        //只在一种类型情况下使用
        mList = new ArrayList<DataModel>();
        listOne = new ArrayList<DataModelOne>();
        listTwo = new ArrayList<DataModelTwo>();
        listThree = new ArrayList<DataModelThree>();
    }

    public void addList(List<DataModel> list) {
        mList.addAll(list);//往已存在的list中添加数据
    }


    private List<DataModelOne> listOne;
    private List<DataModelTwo> listTwo;
    private List<DataModelThree> listThree;
    //private List<AbstractDataModel> allList = new ArrayList<>();

    private List<Integer> types = new ArrayList<>();//存储每一个位置对应的类型
    private Map<Integer,Integer> mPositions = new HashMap<>();//代表了对应类型的大小(长度)，记录每一组的大小

    /**
     * 初始化操作
     * @param listOne
     * @param listTwo
     * @param listThree
     */
    public void addLists(List<DataModelOne> listOne,List<DataModelTwo> listTwo,List<DataModelThree> listThree){
        //mList.add(new DataModel());
        addListByType(TYPE_ONE,listOne);
        addListByType(TYPE_TWO,listTwo);
        addListByType(TYPE_THREE,listThree);
        //addListByType(TYPE_FOOTER, mList);

        //allList.addAll(listOne);
        //allList.addAll(listTwo);
        //allList.addAll(listThree);

        this.listOne = listOne;
        this.listTwo = listTwo;
        this.listThree = listThree;
    }

    /**
     * 附加额外数据
     * @param listOne
     */
    public void addAllOne(List<DataModelOne> listOne) {
        int lastIndex = this.types.size();
        addListByType(TYPE_ONE,listOne);
        if (this.listOne.addAll(listOne)) {
            //范围插入数据
            notifyItemRangeInserted(lastIndex, listOne.size());
        }
    }
    /**
     * 附加额外数据
     * @param listTwo
     */
    public void addAllTwo(List<DataModelTwo> listTwo) {
        int lastIndex = this.types.size();
        addListByType(TYPE_TWO,listTwo);
        if (this.listTwo.addAll(listTwo)) {
            //范围插入数据
            notifyItemRangeInserted(lastIndex, listTwo.size());
        }
    }

    /**
     * 附加额外数据
     * @param listThree
     */
    public void addAllThree(List<DataModelThree> listThree) {
        int lastIndex = this.types.size();
        addListByType(TYPE_THREE,listThree);
        this.listThree.addAll(listThree);
        //范围插入数据
        notifyDataSetChanged();
        //notifyItemRangeInserted(lastIndex, listThree.size());
    }


    private void addListByType(int type,List list) {
        //根据类型添加列表（第二个参数代表的是前一个列表的标记types的初始大小为零，则第一组为零）
        mPositions.put(type,types.size());
        //存储所有子item的类型
        for(int i=0;i<list.size();i++) {
            types.add(type);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case DataModel.TYPE_ONE:
                final View viewOne = mInflater.inflate(R.layout.item_type_one,parent,false);
//                //可以直接在这里设置点击事件
//                viewOne.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(viewOne.getContext(),"a",Toast.LENGTH_SHORT).show();
//                    }
//                });
                return new TypeOneViewHolder(viewOne,mClickListener);
            case DataModel.TYPE_TWO:
                View viewTwo= mInflater.inflate(R.layout.item_type_two,parent,false);
                return new TypeTwoViewHolder(viewTwo);
            case DataModel.TYPE_THREE:
                View viewThree = mInflater.inflate(R.layout.item_type_three,parent,false);
                return new TypeThreeViewHolder(viewThree);
            case DataModel.TYPE_FOOTER:
                View viewFooter = LayoutInflater.from(context).inflate(R.layout.footer_holder, parent, false);
                mFooterHolder = new FooterHolder(viewFooter);
                return mFooterHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //第一种方法
        //((TypeAbstractViewHolder)holder).bindHolder(mList.get(position));

        int viewType,realPosition=0;
        if(position==types.size())
            viewType = DataModel.TYPE_FOOTER;
        else {
            viewType = getItemViewType(position);
            realPosition = position - mPositions.get(viewType);
        }
        //转换成属于具体类型的那个列表 第一个列表6 个，第二个 6个 第三个 6个，第三个类型的列表的第二个对应 为13（从零开始） 13-6

            switch (viewType) {
                case DataModel.TYPE_ONE:
                        ((TypeOneViewHolder) holder).bindHolder(listOne.get(realPosition));
                        ((TypeOneViewHolder) holder).setRealPosition(realPosition);
                    break;
                case DataModel.TYPE_TWO:
                    ((TypeTwoViewHolder) holder).bindHolder(listTwo.get(realPosition));
                    break;
                case DataModel.TYPE_THREE:
                    ((TypeThreeViewHolder) holder).bindHolder(listThree.get(realPosition));
                    break;
                case DataModel.TYPE_FOOTER:
                    ((FooterHolder)holder).setState(FooterState.NORMAL);
                    break;
            }


    }

    @Override
    public int getItemViewType(int position) {
        //return mList.get(position).type;
        //return types.get(position);
        return position==types.size()? DataModel.TYPE_FOOTER:types.get(position);
    }

    @Override
    public int getItemCount() {
        //return mList.size();
        //这里返回的是个数 position + 1 = size
        return types.size()+1;
    }

    //点击监听回调接口
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    //设置监听器
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mClickListener = mOnItemClickListener;
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        View mLoadingViewStub;
        View mEndViewStub;
        View mNetworkErrorViewStub;

        public FooterHolder(View itemView) {
            super(itemView);
            //绑定视图id
            mLoadingViewStub = itemView.findViewById(R.id.loading_viewStub);
            mEndViewStub = itemView.findViewById(R.id.end_viewStub);
            mNetworkErrorViewStub = itemView.findViewById(R.id.network_error_viewStub);
        }

        //根据传过来的status控制哪个状态可见
        public void setState(FooterState status) {
            Log.d("TAG", "DemoAdapter" + status + "");
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

        //全部不可见
        void setAllGone() {
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

}
