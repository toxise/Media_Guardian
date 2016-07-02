package com.vintech.mediaguardian.framework;

/**
 * Created by Vincent on 2016/5/26.
 */
public class FrameEvent {
    public static class EventRequestPermission {
        public String mPermission;

        public EventRequestPermission(String permission) {
            mPermission = permission;
        }
    }

    public static class EventPermissionResult {
        public String mPermission;
        public int mResult;

        public EventPermissionResult(String permission, int result) {
            mPermission = permission;
            mResult = result;
        }
    }

    public static class EventSetLayout {
        public int mLayoutId;

        public EventSetLayout(int layoutId) {
            mLayoutId = layoutId;
        }
    }
}
