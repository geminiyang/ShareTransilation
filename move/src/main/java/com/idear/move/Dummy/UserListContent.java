package com.idear.move.Dummy;

import com.idear.move.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class UserListContent {

    /**
     * 存储数据源
     */
    public static final List<UserList> ITEMS = new ArrayList<UserList>();

    /**
     * 通过Id标识存储数据源
     */
    public static final Map<String, UserList> ITEM_MAP = new HashMap<String, UserList>();

    private static final int COUNT = 10;

    static {
        //初始化操作
        for (int i = 1; i <= COUNT; i++) {
            addItem(createUserItem(i));
        }
    }

    private static void addItem(UserList item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.title, item);
    }

    private static UserList createUserItem(int position) {
        return new UserList(String.valueOf(position)+"号嘉宾", "今晚去湛江鸡吃饭，老铁!", "2017.5.29", R.drawable.paitnbox,R.mipmap.msg_fill);
    }

    /**
     * POJO类
     */
    public static class UserList {
        public final String title;
        public final String content;
        public final String time;
        public final int userImgId;
        public final int tipImgId;

        public UserList(String title, String content, String time,int userImgId, int tipImgId) {
            this.title = title;
            this.content = content;
            this.time = time;
            this.userImgId = userImgId;
            this.tipImgId = tipImgId;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
