package tieup.business_network.appmart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CATEGORIES_TABLE_NAME = "categories";
    public static final String CATEGORIES_COLUMN_NAME = "category";
    public static final String COUNT_COLUMN_NAME = "count";
    private static final int DATABASE_VERSION = 2; // indicate database update
    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+CATEGORIES_TABLE_NAME  +
                        "(category_id integer primary key, category text, count integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);

    }

    public boolean insertCategory(String category,Integer count) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", category);
        contentValues.put("count", count);
        db.insert("categories", null, contentValues);
        return true;

    }

    public Cursor getData() {
      db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from categories", null);
        return res;
    }

   /* public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CATEGORIES_TABLE_NAME);
        return numRows;
    }*/

   public boolean updateCount(String category, int count ) {
       int newcount = count+1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("count", newcount);
        db.update("categories", contentValues, CATEGORIES_COLUMN_NAME+" =?", new String[] { category });

       return true;
    }

    /*public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }*/


    public ArrayList<String> getAllCategories() {
        ArrayList<String> allCategoryArrayList = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from categories", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            allCategoryArrayList.add(res.getString(res.getColumnIndex(COUNT_COLUMN_NAME)));
            res.moveToNext();
        }

        return allCategoryArrayList;
    }

    public int getCurrentCount(String cat) {
      //  ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM categories WHERE category = '"+cat+"'", null);
        res.moveToFirst();
        int currentCount = 0;
        while (res.isAfterLast() == false) {
             currentCount=(res.getInt(res.getColumnIndex(COUNT_COLUMN_NAME)));
            res.moveToNext();
        }
        return currentCount;

    }

    /*  public String getTableAsString(String catt) {
          SQLiteDatabase db = this.getWritableDatabase();
            Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", catt);
        Cursor allRows  = db.rawQuery("SELECT * FROM categories", null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }
*/
}
