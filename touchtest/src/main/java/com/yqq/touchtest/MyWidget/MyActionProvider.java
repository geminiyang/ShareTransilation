package com.yqq.touchtest.MyWidget;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.yqq.touchtest.R;

/**
 * Created by user on 2017/2/7.
 */

public class MyActionProvider extends ActionProvider{


    /**
     * Creates a new instance. ActionProvider classes should always implement a
     * constructor that takes a single Context parameter for inflating from menu XML.
     *
     * @param context Context for accessing resources.
     */
    public MyActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        super.onPrepareSubMenu(subMenu);
        subMenu.clear();
        subMenu.add("sub item 1").setIcon(R.mipmap.note).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //在此可以进行自己想要的操作
                return true;
            }
        });
        subMenu.add("sub item 2").setIcon(R.mipmap.note)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
