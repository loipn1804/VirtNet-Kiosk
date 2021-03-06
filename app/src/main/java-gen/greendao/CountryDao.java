package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Country;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table COUNTRY.
*/
public class CountryDao extends AbstractDao<Country, String> {

    public static final String TABLENAME = "COUNTRY";

    /**
     * Properties of entity Country.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Order = new Property(0, Long.class, "order", false, "ORDER");
        public final static Property Code = new Property(1, String.class, "code", true, "CODE");
        public final static Property Slug = new Property(2, String.class, "slug", false, "SLUG");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
    };


    public CountryDao(DaoConfig config) {
        super(config);
    }
    
    public CountryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'COUNTRY' (" + //
                "'ORDER' INTEGER," + // 0: order
                "'CODE' TEXT PRIMARY KEY NOT NULL ," + // 1: code
                "'SLUG' TEXT," + // 2: slug
                "'NAME' TEXT);"); // 3: name
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'COUNTRY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Country entity) {
        stmt.clearBindings();
 
        Long order = entity.getOrder();
        if (order != null) {
            stmt.bindLong(1, order);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }
 
        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(3, slug);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
    }    

    /** @inheritdoc */
    @Override
    public Country readEntity(Cursor cursor, int offset) {
        Country entity = new Country( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // order
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // slug
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // name
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Country entity, int offset) {
        entity.setOrder(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSlug(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Country entity, long rowId) {
        return entity.getCode();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Country entity) {
        if(entity != null) {
            return entity.getCode();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
