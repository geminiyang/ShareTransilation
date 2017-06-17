package com.idear.move.Dummy;

import com.idear.move.R;

/**
 *
 */
public class GroupListContent {

    /**
     * 生产数据源的方法
     * @param position
     * @return
     */
    public static GroupList createGroupItem(int position) {
        return new GroupList(String.valueOf(position+1) +"号房间", "今天4,6级考试，老铁!",
                "2017.6.17", R.mipmap.ninepatch150,R.mipmap.msg_fill);
    }

    /**
     * POJO类（DATAModel）
     */
    public static class GroupList {
        public final String title;
        public final String content;
        public final String time;
        public final int GroupImgId;
        public final int tipImgId;

        public GroupList(String title, String content, String time,int GroupImgId, int tipImgId) {
            this.title = title;
            this.content = content;
            this.time = time;
            this.GroupImgId = GroupImgId;
            this.tipImgId = tipImgId;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
