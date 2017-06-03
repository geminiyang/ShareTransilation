package com.example.loadermanager;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * 使用加载器加载通话记录
 *
 * @author Administrator
 *
 */
public class MainActivity extends Activity {

    private static final String TAG = "dzt";
    // 查询指定的条目
    private static final String[] CALLLOG_PROJECTION = new String[] {
            CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE, CallLog.Calls.DATE };
    static final int DAY = 1440; // 一天的分钟值
    private static final int ALL = 0; // 默认显示所有
    private static final int INCOMING = CallLog.Calls.INCOMING_TYPE; // 来电
    private static final int OUTCOMING = CallLog.Calls.OUTGOING_TYPE; // 拔号
    private static final int MISSED = CallLog.Calls.MISSED_TYPE; // 未接
    private ListView mListView;
    private MyLoaderListener mLoader = new MyLoaderListener();
    private MyCursorAdapter mAdapter;
    private int mCallLogShowType = ALL;
    private boolean m_FinishLoaderFlag = false; // 第一次加载完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        initMyLoader();
    }

    private void initWidgets() {
        mListView = (ListView) findViewById(R.id.lv_list);
        Button btn = (Button) findViewById(R.id.btn_all);
        btn.setOnClickListener(new buttonListener());
        btn = (Button) findViewById(R.id.btn_incoming);
        btn.setOnClickListener(new buttonListener());
        btn = (Button) findViewById(R.id.btn_outcoming);
        btn.setOnClickListener(new buttonListener());
        btn = (Button) findViewById(R.id.btn_missed);
        btn.setOnClickListener(new buttonListener());
        mAdapter = new MyCursorAdapter(MainActivity.this, null);
        mListView.setAdapter(mAdapter);
    }

    private void initMyLoader() {
        getLoaderManager().initLoader(0, null, mLoader);
    }

    /**
     * 实现一个加载器
     * @author Administrator
     */
    private class MyLoaderListener implements
            LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            m_FinishLoaderFlag = false;
            CursorLoader cursor = new CursorLoader(MainActivity.this,
                    CallLog.Calls.CONTENT_URI, CALLLOG_PROJECTION, null, null,
                    CallLog.Calls.DEFAULT_SORT_ORDER);
            Log.d(TAG, "MyLoaderListener---------->onCreateLoader");
            return cursor;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null)
                return;
            Cursor tempData = data;
            if (tempData.getCount() == 0) {
                Log.d(TAG,
                        "MyLoaderListener---------->onLoadFinished count = 0");
                mAdapter.swapCursor(null);
                return;
            }
            if (m_FinishLoaderFlag) {
                tempData = null;
                String selection = null;
                String[] selectionArgs = null;
                if (mCallLogShowType == INCOMING) {
                    selection = CallLog.Calls.TYPE + "=?";
                    selectionArgs = new String[] { "1" };
                } else if (mCallLogShowType == OUTCOMING) {
                    selection = CallLog.Calls.TYPE + "=?";
                    selectionArgs = new String[] { "2" };
                } else if (mCallLogShowType == MISSED) {
                    selection = CallLog.Calls.TYPE + "=?";
                    selectionArgs = new String[] { "3" };
                }
                tempData = getContentResolver().query(
                        CallLog.Calls.CONTENT_URI, CALLLOG_PROJECTION,
                        selection, selectionArgs,
                        CallLog.Calls.DEFAULT_SORT_ORDER);
            }
            mAdapter.swapCursor(tempData);
            Log.d(TAG,
                    "MyLoaderListener---------->onLoadFinished data count = "
                            + data.getCount());
            m_FinishLoaderFlag = true;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.d(TAG, "MyLoaderListener---------->onLoaderReset");
            mAdapter.swapCursor(null);
        }
    }

    private class buttonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_all:
                    allCalllog();
                    break;
                case R.id.btn_incoming:
                    incomingCalllog();
                    break;
                case R.id.btn_outcoming:
                    outcomingCalllog();
                    break;
                case R.id.btn_missed:
                    missedCalllog();
                    break;
                default:
                    break;
            }
        }
    }

    private void allCalllog() {
        mCallLogShowType = ALL;
        String selection = null;
        String[] selectionArgs = null;
        Cursor allCursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, CALLLOG_PROJECTION, selection,
                selectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER);
        mAdapter.swapCursor(allCursor);
    }

    private void incomingCalllog() {
        mCallLogShowType = INCOMING;
        String selection = CallLog.Calls.TYPE + "=?";
        String[] selectionArgs = new String[] { "1" };
        Cursor incomingCursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, CALLLOG_PROJECTION, selection,
                selectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER);
        mAdapter.swapCursor(incomingCursor);
    }

    private void outcomingCalllog() {
        mCallLogShowType = OUTCOMING;
        String selection = CallLog.Calls.TYPE + "=?";
        String[] selectionArgs = new String[] { "2" };
        Cursor outcomingCursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, CALLLOG_PROJECTION, selection,
                selectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER);
        mAdapter.swapCursor(outcomingCursor);
    }

    private void missedCalllog() {
        mCallLogShowType = MISSED;
        String selection = CallLog.Calls.TYPE + "=?";
        String[] selectionArgs = new String[] { "3" };
        Cursor missedCursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI, CALLLOG_PROJECTION, selection,
                selectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER);
        mAdapter.swapCursor(missedCursor);
    }
}
