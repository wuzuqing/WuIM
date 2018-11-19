package com.wuzuqing.component_im.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.wuzuqing.component_im.bean.GroupBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GROUP_BEAN".
*/
public class GroupBeanDao extends AbstractDao<GroupBean, Long> {

    public static final String TABLENAME = "GROUP_BEAN";

    /**
     * Properties of entity GroupBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _ID = new Property(0, Long.class, "_ID", true, "_id");
        public final static Property Id = new Property(1, int.class, "id", false, "ID");
        public final static Property GroupName = new Property(2, String.class, "groupName", false, "GROUP_NAME");
        public final static Property Avatar = new Property(3, String.class, "avatar", false, "AVATAR");
        public final static Property CreateTime = new Property(4, long.class, "createTime", false, "CREATE_TIME");
        public final static Property GroupManagerId = new Property(5, int.class, "groupManagerId", false, "GROUP_MANAGER_ID");
    }


    public GroupBeanDao(DaoConfig config) {
        super(config);
    }
    
    public GroupBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GROUP_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: _ID
                "\"ID\" INTEGER NOT NULL ," + // 1: id
                "\"GROUP_NAME\" TEXT," + // 2: groupName
                "\"AVATAR\" TEXT," + // 3: avatar
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 4: createTime
                "\"GROUP_MANAGER_ID\" INTEGER NOT NULL );"); // 5: groupManagerId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GROUP_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GroupBean entity) {
        stmt.clearBindings();
 
        Long _ID = entity.get_ID();
        if (_ID != null) {
            stmt.bindLong(1, _ID);
        }
        stmt.bindLong(2, entity.getId());
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(3, groupName);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
        stmt.bindLong(5, entity.getCreateTime());
        stmt.bindLong(6, entity.getGroupManagerId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GroupBean entity) {
        stmt.clearBindings();
 
        Long _ID = entity.get_ID();
        if (_ID != null) {
            stmt.bindLong(1, _ID);
        }
        stmt.bindLong(2, entity.getId());
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(3, groupName);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
        stmt.bindLong(5, entity.getCreateTime());
        stmt.bindLong(6, entity.getGroupManagerId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GroupBean readEntity(Cursor cursor, int offset) {
        GroupBean entity = new GroupBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _ID
            cursor.getInt(offset + 1), // id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // groupName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // avatar
            cursor.getLong(offset + 4), // createTime
            cursor.getInt(offset + 5) // groupManagerId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GroupBean entity, int offset) {
        entity.set_ID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.getInt(offset + 1));
        entity.setGroupName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAvatar(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreateTime(cursor.getLong(offset + 4));
        entity.setGroupManagerId(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GroupBean entity, long rowId) {
        entity.set_ID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GroupBean entity) {
        if(entity != null) {
            return entity.get_ID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GroupBean entity) {
        return entity.get_ID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}