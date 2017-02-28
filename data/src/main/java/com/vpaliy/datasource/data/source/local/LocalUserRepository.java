package com.vpaliy.datasource.data.source.local;

import android.content.ContentValues;
import android.content.Context;

import com.vpaliy.common.Preconditions;
import com.vpaliy.datasource.data.entity.UserEntity;
import com.vpaliy.datasource.data.source.Repository;
import com.vpaliy.datasource.data.specification.SQLSpecification;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import static com.vpaliy.datasource.data.source.local.PersistenceContract.UserEntry;



@SuppressWarnings("WeakerAccess")
public class LocalUserRepository implements Repository<UserEntity, SQLSpecification> {

    private static LocalUserRepository INSTANCE;

    private UserSQLHelper dbHelper;

    private LocalUserRepository(@NonNull Context context) {
        this.dbHelper=new UserSQLHelper(context);
    }

    @Override
    public void add(UserEntity item) {
        Preconditions.checkNotNull(item);
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put(UserEntry.COLUMN_FIRST_NAME,item.getFirstName());
        values.put(UserEntry.COLUMN_LAST_NAME,item.getLastName());
        values.put(UserEntry.COLUMN_NAME_ENTRY_ID,item.getID());
        values.put(UserEntry.COLUMN_AGE,item.getAge());
        values.put(UserEntry.COLUMN_EMAIL_ADDRESS,item.getEmailAddress());

        db.insert(UserEntry.TABLE_NAME,null,values);
        db.close();
    }

    @Override
    public void add(Iterable<UserEntity> items) {
        for(UserEntity entity:items) {
            add(entity);
        }
    }

    @Override
    public void update(UserEntity item) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_FIRST_NAME,item.getFirstName());
        values.put(UserEntry.COLUMN_LAST_NAME,item.getLastName());
        values.put(UserEntry.COLUMN_AGE,item.getAge());
        values.put(UserEntry.COLUMN_EMAIL_ADDRESS,item.getEmailAddress());

        String selection = UserEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(item.getID())};

        db.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();

    }

    @Override
    public void update(@NonNull UserEntity item, @NonNull SQLSpecification specification) {
        String[] projections=specification.getProjection();

        SQLiteDatabase db=dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        for(String projection:projections) {
            switch (projection) {
                case UserEntry.COLUMN_AGE:
                    values.put(UserEntry.COLUMN_AGE,item.getAge());
                    break;
                case UserEntry.COLUMN_EMAIL_ADDRESS:
                    values.put(UserEntry.COLUMN_EMAIL_ADDRESS,item.getEmailAddress());
                    break;
                case UserEntry.COLUMN_FIRST_NAME:
                    values.put(UserEntry.COLUMN_FIRST_NAME,item.getFirstName());
                    break;
                case UserEntry.COLUMN_LAST_NAME:
                    values.put(UserEntry.COLUMN_LAST_NAME,item.getFirstName());
                    break;
            }
        }

        String selection = UserEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(item.getID())};

        db.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    @Override
    public List<UserEntity> query(@NonNull SQLSpecification specification) {
        String[] projections=specification.getProjection();
        Preconditions.checkIfContains(projections, UserEntry.COLUMN_LAST_NAME,
                UserEntry.COLUMN_LAST_NAME, UserEntry.COLUMN_NAME_ENTRY_ID);
        List<UserEntity> result=new LinkedList<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selection=specification.getSelection();
        String[] selectionArgs=specification.getSelectionArgs();
        String order=specification.getOrder();
        Cursor cursor=db.query(UserEntry.TABLE_NAME,
                projections,selection,selectionArgs,null,null,order);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                while(cursor.moveToNext()) {
                    //main entries
                    String firstName=cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_FIRST_NAME));
                    String lastName=cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_LAST_NAME));
                    int ID=cursor.getInt(cursor.getColumnIndex(UserEntry.COLUMN_NAME_ENTRY_ID));
                    //create an instance
                    UserEntity userEntity=new UserEntity(firstName,lastName,ID);
                    //fill up with details
                    for(String projection:projections) {
                        switch (projection) {
                            case UserEntry.COLUMN_AGE:
                                userEntity.setAge(cursor.getInt(cursor.getColumnIndex(projection)));
                                break;
                            case UserEntry.COLUMN_EMAIL_ADDRESS:
                                userEntity.setEmailAddress(cursor.getString(cursor.getColumnIndex(projection)));
                                break;
                        }
                    }
                    result.add(userEntity);
                }
            }
            cursor.close();
        }
        db.close();
        return result;
    }

    public static LocalUserRepository getInstance(@NonNull Context context) {
        synchronized (LocalUserRepository.class) {
            if (INSTANCE == null) {
                INSTANCE = new LocalUserRepository(context);
            }
        }
        return INSTANCE;
    }
}