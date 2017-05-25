package com.cml.androidcmldevelopertools.navigation;

import android.support.v4.app.Fragment;

/**
 * author：cml on 2017/5/23
 * github：https://github.com/cmlgithub
 */

public class NavigationChild {

    private int selectIcon;
    private int noSelectIcon;
    private String text;
    private Fragment fragment;

    public NavigationChild(int selectIcon, int noSelectIcon, String text, Fragment fragment) {
        this.selectIcon = selectIcon;
        this.noSelectIcon = noSelectIcon;
        this.text = text;
        this.fragment = fragment;
    }

    public void setSelectIcon(int selectIcon) {
        this.selectIcon = selectIcon;
    }

    public void setNoSelectIcon(int noSelectIcon) {
        this.noSelectIcon = noSelectIcon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getSelectIcon() {
        return selectIcon;
    }

    public int getNoSelectIcon() {
        return noSelectIcon;
    }

    public String getText() {
        return text;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
