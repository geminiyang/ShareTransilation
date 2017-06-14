package com.idear.move.Dummy;

import com.idear.move.R;

/**
 *
 */
public class UserListContent {

    /**
     * 生产数据源的方法
     * @param position
     * @return
     */
    public static UserList createUserItem(int position) {
        return new UserList(String.valueOf(position+1) +"号嘉宾", "今晚去湛江鸡吃饭，老铁!",
                "2017.5.29", R.drawable.paintbox,R.mipmap.msg_fill);
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
