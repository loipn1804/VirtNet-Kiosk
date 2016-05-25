package com.kiosk.daocontroller;

import android.content.Context;

import com.kiosk.activity.MyApplication;

import java.util.List;

import greendao.Dormitory;
import greendao.DormitoryDao;

/**
 * Created by USER on 12/17/2015.
 */
public class DormitoryController {
    private static DormitoryDao getCommentDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDormitoryDao();
    }

    public static void insert(Context context, Dormitory Dormitory) {
        getCommentDao(context).insert(Dormitory);
    }

    public static void update(Context context, Dormitory Dormitory) {
        getCommentDao(context).update(Dormitory);
    }

    public static List<Dormitory> getAll(Context context) {
        return getCommentDao(context).loadAll();
    }

    public static Dormitory getByDormitoryId(Context context, Long dormitory_id) {
        return getCommentDao(context).load(dormitory_id);
    }

    public static void clearByObject(Context context, Dormitory Dormitory) {
        getCommentDao(context).delete(Dormitory);
    }

    public static void clearAll(Context context) {
        getCommentDao(context).deleteAll();
    }
}
