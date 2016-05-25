package com.kiosk.daocontroller;

import android.content.Context;

import com.kiosk.activity.MyApplication;

import java.util.List;

import greendao.Category;
import greendao.CategoryDao;

/**
 * Created by USER on 12/17/2015.
 */
public class CategoryController {
    private static CategoryDao getCommentDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCategoryDao();
    }

    public static void insert(Context context, Category Category) {
        getCommentDao(context).insert(Category);
    }

    public static void update(Context context, Category Category) {
        getCommentDao(context).update(Category);
    }

    public static List<Category> getAll(Context context) {
        return getCommentDao(context).loadAll();
    }

    public static void clearByObject(Context context, Category Category) {
        getCommentDao(context).delete(Category);
    }

    public static void clearAll(Context context) {
        getCommentDao(context).deleteAll();
    }
}
