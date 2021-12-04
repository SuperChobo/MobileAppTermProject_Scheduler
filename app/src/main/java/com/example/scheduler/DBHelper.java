package com.example.scheduler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    Calendar datdddde;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1 = "CREATE TABLE if not exists entity_list("
                + "id integer primary key,"
                + "name text,"
                + "type integer"
                + ");";

        String sql2 = "CREATE TABLE if not exists entities("
                + "list_id integer,"
                + "name text,"
                + "type integer,"
                + "value integer,"
                + "goal integer,"
                + "periodType integer,"
                + "period integer,"
                + "year integer,"
                + "month integer,"
                + "date integer,"
                + "hour integer,"
                + "minute integer,"
                + "value_list text"
                + ");";

        String sql3 = "CREATE TABLE if not exists columns("
                + "list_id integer,"
                + "column_name text"
                + ");";

        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql1 = "DROP TABLE if exists entity_list;";
        String sql2 = "DROP TABLE if exists entities;";
        String sql3 = "DROP TABLE if exists columns;";

        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);

        onCreate(sqLiteDatabase);
    }

    public List<EntityList> getTable(){
        List<EntityList> list = new ArrayList<EntityList>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor1 = db.rawQuery("select * from entity_list;", null);
        while (cursor1.moveToNext()){
            int idx = cursor1.getInt(0);
            Log.i("test", "1");
            List<Entity> entities = new ArrayList<Entity>();
            Cursor cursor2 = db.rawQuery("select * from entities where list_id = " + String.valueOf(idx) + ";", null);
            while(cursor2.moveToNext()){
                Log.i("test", "2");
                entities.add(new Entity(cursor2.getString(1), cursor2.getInt(2), cursor2.getInt(3),
                        cursor2.getInt(4), cursor2.getInt(5), cursor2.getInt(6),
                        cursor2.getInt(7), cursor2.getInt(8), cursor2.getInt(9),
                        cursor2.getInt(10), cursor2.getInt(11), cursor2.getString(12)));
            }

            List<String> columns = new ArrayList<String>();
            Cursor cursor3 = db.rawQuery("select * from columns where list_id = " + String.valueOf(idx) + ";", null);
            while(cursor3.moveToNext()){
                Log.i("test", "3");
                columns.add(new String(cursor3.getString(1)));
            }

            EntityList entityList = new EntityList(cursor1.getString(1), cursor1.getInt(2), entities, columns);
            list.add(entityList);
        }

        return list;
    }

    public void setTable(List<EntityList> list){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from entity_list;");
        db.execSQL("delete from entities");
        db.execSQL("delete from columns");

        int idx = 0;
        for(EntityList entityList : list){
            db.execSQL("insert into entity_list values(" +
                    String.valueOf(idx) + ",'" +
                    entityList.getName() + "'," +
                    String.valueOf(entityList.getType()) +
                    ");"
            );

            for(Entity entity : entityList.getList()){
                db.execSQL("insert into entities values(" +
                        String.valueOf(idx) + ",'" +
                        entity.getName() + "'," +
                        String.valueOf(entity.getType()) + "," +
                        String.valueOf(entity.getValue()) + "," +
                        String.valueOf(entity.getGoal()) + "," +
                        String.valueOf(entity.getPeriodType()) + "," +
                        String.valueOf(entity.getPeriod()) + "," +
                        String.valueOf(entity.getDate().get(Calendar.YEAR)) + "," +
                        String.valueOf(entity.getDate().get(Calendar.MONTH)) + "," +
                        String.valueOf(entity.getDate().get(Calendar.DATE)) + "," +
                        String.valueOf(entity.getDate().get(Calendar.HOUR)) + "," +
                        String.valueOf(entity.getDate().get(Calendar.MINUTE)) + ",'" +
                        entity.getValueList() +
                        "');"
                );
            }

            for(String str : entityList.getColumns()){
                db.execSQL("insert into columns values(" +
                        String.valueOf(idx) + ",'" +
                        str + "');"
                );
            }

            idx++;
        }
    }
}
