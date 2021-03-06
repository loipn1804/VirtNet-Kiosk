package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Validate;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table VALIDATE.
*/
public class ValidateDao extends AbstractDao<Validate, Long> {

    public static final String TABLENAME = "VALIDATE";

    /**
     * Properties of entity Validate.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Worker_id = new Property(0, Long.class, "worker_id", true, "WORKER_ID");
        public final static Property Count = new Property(1, Integer.class, "count", false, "COUNT");
        public final static Property Time = new Property(2, Long.class, "time", false, "TIME");
    };


    public ValidateDao(DaoConfig config) {
        super(config);
    }
    
    public ValidateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'VALIDATE' (" + //
                "'WORKER_ID' INTEGER PRIMARY KEY ," + // 0: worker_id
                "'COUNT' INTEGER," + // 1: count
                "'TIME' INTEGER);"); // 2: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'VALIDATE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Validate entity) {
        stmt.clearBindings();
 
        Long worker_id = entity.getWorker_id();
        if (worker_id != null) {
            stmt.bindLong(1, worker_id);
        }
 
        Integer count = entity.getCount();
        if (count != null) {
            stmt.bindLong(2, count);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(3, time);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Validate readEntity(Cursor cursor, int offset) {
        Validate entity = new Validate( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // worker_id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // count
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2) // time
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Validate entity, int offset) {
        entity.setWorker_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCount(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setTime(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Validate entity, long rowId) {
        entity.setWorker_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Validate entity) {
        if(entity != null) {
            return entity.getWorker_id();
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
