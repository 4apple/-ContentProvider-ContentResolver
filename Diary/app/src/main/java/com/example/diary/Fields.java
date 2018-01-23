package com.example.diary;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author wanlijun
 * @description 数据库字段
 * @time 2018/1/22 15:43
 */

public class Fields {
    public static final String AUTHORITY = "com.lijun.diary";
    public static class DiaryColumns implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/diaries");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/diary";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/diary";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String TIME = "time";
    }
}
