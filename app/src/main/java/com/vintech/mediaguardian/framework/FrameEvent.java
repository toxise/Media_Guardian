package com.vintech.mediaguardian.framework;

/**
 * Created by Vincent on 2016/5/26.
 */
public class FrameEvent {

    /**
     * 请求权限的事件
     */
    public static class EventRequestPermission {
        public String mPermission;
        public EventRequestPermission(String permission) {
            mPermission = permission;
        }
    }


    /**
     * 权限请求结果返回的权限
     */
    public static class EventPermissionResult {
        public String mPermission;
        public int mResult;

        public EventPermissionResult(String permission, int result) {
            mPermission = permission;
            mResult = result;
        }
    }

    /**
     * 设置主页面布局内容的事件
     */
    public static class EventSetLayout {
        public int mLayoutId;
        public EventSetLayout(int layoutId) {
            mLayoutId = layoutId;
        }
    }

    /**
     * 请求修改menu菜单的事件
     */
    public static class EventSetMenu {
        public static final int ID_MENU_NONE = -1;
        public static final int ID_MENU_DEFAULT = -2;
        public int mMenuId;
        public EventSetMenu(int menuId) {
            mMenuId = menuId;
        }
    }
}
