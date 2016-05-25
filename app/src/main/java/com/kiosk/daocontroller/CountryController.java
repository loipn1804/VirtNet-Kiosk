package com.kiosk.daocontroller;

import android.content.Context;

import com.kiosk.activity.MyApplication;

import java.util.List;

import greendao.Country;
import greendao.CountryDao;

/**
 * Created by USER on 12/18/2015.
 */
public class CountryController {
    private static CountryDao getCommentDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCountryDao();
    }

    public static void insert(Context context, Country Country) {
        getCommentDao(context).insert(Country);
    }

    public static void update(Context context, Country Country) {
        getCommentDao(context).update(Country);
    }

    public static List<Country> getAll(Context context) {
        return getCommentDao(context).loadAll();
    }

    public static List<Country> getAllByName(Context context, String name) {
        return getCommentDao(context).queryRaw(" where name like ?", "%" + name + "%");
    }

    public static String getCodeByName(Context context, String name) {
        List<Country> list = getCommentDao(context).queryRaw(" where name = ?", name);
        if (list.size() > 0) {
            return list.get(0).getCode();
        } else {
            return "";
        }
    }

    public static void clearByObject(Context context, Country Country) {
        getCommentDao(context).delete(Country);
    }

    public static void clearAll(Context context) {
        getCommentDao(context).deleteAll();
    }
}
