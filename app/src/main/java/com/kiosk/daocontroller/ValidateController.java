package com.kiosk.daocontroller;

import android.content.Context;

import com.kiosk.activity.MyApplication;

import java.util.List;

import greendao.Validate;
import greendao.ValidateDao;

/**
 * Created by USER on 12/18/2015.
 */
public class ValidateController {
    private static ValidateDao getCommentDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getValidateDao();
    }

    public static void insert(Context context, Validate Validate) {
        getCommentDao(context).insert(Validate);
    }

    public static Validate getByWorkerId(Context context, Long worker_id) {
        return getCommentDao(context).load(worker_id);
    }

    public static void update(Context context, Validate Validate) {
        getCommentDao(context).update(Validate);
    }

    public static List<Validate> getAll(Context context) {
        return getCommentDao(context).loadAll();
    }

    public static void clearByObject(Context context, Validate Validate) {
        getCommentDao(context).delete(Validate);
    }

    public static void clearAll(Context context) {
        getCommentDao(context).deleteAll();
    }
}
