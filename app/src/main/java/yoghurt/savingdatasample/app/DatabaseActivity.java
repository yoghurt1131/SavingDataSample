package yoghurt.savingdatasample.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class DatabaseActivity extends ActionBarActivity {

    int[] ids = {R.id.btn_save,R.id.btn_display,R.id.btn_delete,R.id.btn_update};
    CreateProductHelper helper = null;
    SQLiteDatabase db = null;

    Context con = DatabaseActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_set_layout);
        Button[] btns = new Button[ids.length];
        for(int i=0;i< btns.length;i++){
            btns[i] = (Button)findViewById(ids[i]);
            btns[i].setOnClickListener(new DataHandleListener());
        }

        //DB作成
        helper = new CreateProductHelper(DatabaseActivity.this);
    }

    class DataHandleListener implements View.OnClickListener{
        public void onClick(View v){
            db = helper.getWritableDatabase();
            String msg=null;
            EditText et_name = (EditText)findViewById(R.id.et_name);
            EditText et_value = (EditText)findViewById(R.id.et_value);
            String item_name = et_name.getText().toString();
            String item_value = et_value.getText().toString();
            switch(v.getId()){
                case R.id.btn_save:
                    try {

                        //トランザクション開始
                        db.beginTransaction();
                        try {

                            ContentValues values = new ContentValues();
                            values.put(DataBaseEntry.COLUMN_NAME_ITEM, item_name);
                            values.put(DataBaseEntry.COLUMN_NAME_VALUE, item_value);

                            db.insert(DataBaseEntry.TABLE_NAME, null, values);

                            //コミット
                            db.setTransactionSuccessful();
                        }finally {
                            //トランザクション終了
                            db.endTransaction();
                        }
                        msg = getString(R.string.toast_save);
                    }catch(Exception e){
                        msg = getString(R.string.toast_failed);
                    }

                    break;
                case R.id.btn_display:
                    try{
                        db = helper.getReadableDatabase();

                        String columns[] = {
                                DataBaseEntry.COLUMN_NAME_ENTRY_ID,
                                DataBaseEntry.COLUMN_NAME_ITEM,
                                DataBaseEntry.COLUMN_NAME_VALUE
                        };
                        //データ取得
//                        Cursor cursor = db.query(DataBaseEntry.TABLE_NAME,columns,null,null,null,null, DataBaseEntry.COLUMN_NAME_ENTRY_ID);
                        Cursor cursor = db.rawQuery("select "+columns[0]+","+columns[1]+","+columns[2]+" from "+DataBaseEntry.TABLE_NAME+" where "+DataBaseEntry.COLUMN_NAME_VALUE +" < 300 ;",null);

                        TableLayout table = (TableLayout)findViewById(R.id.table);
                        table.removeAllViews();
                        table.setStretchAllColumns(true);
                        while(cursor.moveToNext()){
                            TableRow row = new TableRow(con);
                            TextView id_tv = new TextView(con);
                            id_tv.setText(cursor.getString(0));
                            TextView item_tv = new TextView(con);
                            item_tv.setText(cursor.getString(1));
                            TextView value_tv = new TextView(con);
                            value_tv.setText(cursor.getString(2));

                            row.addView(id_tv);
                            row.addView(item_tv);
                            row.addView(value_tv);

                            table.addView(row);

                        }
                        msg = getString(R.string.toast_display);

                    }catch(Exception e){
                        msg = getString(R.string.toast_failed);
                    }

                    break;
                case R.id.btn_delete:
                    String condition_delete = null;
                    try {
                        //トランザクション開始
                        db.beginTransaction();

                        //データ削除
                        db.delete(DataBaseEntry.TABLE_NAME, condition_delete, null);

                        //コミット
                        db.setTransactionSuccessful();

                        //トランザクション終了
                        db.endTransaction();

                        msg = getString(R.string.toast_delete);
                    }catch(Exception e){
                        msg = getString(R.string.toast_delete);
                    }

                    break;
                case R.id.btn_update:
                    try{
                        //更新条件
                        String condition_update = null;
                        if(et_name!=null && !et_name.equals("")){
                            condition_update = DataBaseEntry.COLUMN_NAME_ITEM + " = '" + item_name + "' ";
                        }
                        //トランザクション開始
                        db.beginTransaction();

                        ContentValues values = new ContentValues();
                        values.put(DataBaseEntry.COLUMN_NAME_ITEM, item_name);
                        values.put(DataBaseEntry.COLUMN_NAME_VALUE, item_value);

                        //データ更新
                        db.update(DataBaseEntry.TABLE_NAME,values,condition_update,null);

                        //コミット
                        db.setTransactionSuccessful();

                        //トランザクション終了
                        db.endTransaction();

                        msg = getString(R.string.toast_update);
                    }catch (Exception e){
                        msg = getString(R.string.toast_failed);
                    }
                    break;
            }
            //終了
            db.close();
            Toast.makeText(con,msg,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //この形式じゃなくていい？
    public static abstract class DataBaseEntry implements BaseColumns {
        public static final String TABLE_NAME="entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_ITEM = " item";
        public static final String COLUMN_NAME_VALUE = " value";
    }
    public class CreateProductHelper extends SQLiteOpenHelper {



        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String PRIMARY_KEY = " PRIMARY KEY";
        private static final String AUTO_INCREMENT = " AUTOINCREMENT";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DataBaseEntry.TABLE_NAME + " ("+
                        DataBaseEntry.COLUMN_NAME_ENTRY_ID+ INTEGER_TYPE + PRIMARY_KEY + AUTO_INCREMENT+COMMA_SEP+
                        DataBaseEntry.COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
                        DataBaseEntry.COLUMN_NAME_VALUE + INTEGER_TYPE +
                        ")";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS "+DataBaseEntry.TABLE_NAME;

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "DBSample.db";

        //コンストラクタ
    public CreateProductHelper(Context con){
        super(con,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //onCreate
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    //onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){

    }

    //onDowngrade
    @Override
    public void onDowngrade(SQLiteDatabase db,int oldversion,int newversion){

    }

}


}
