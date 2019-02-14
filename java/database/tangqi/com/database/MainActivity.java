package database.tangqi.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private EditText name;
    private EditText phone;
    private TextView textview;
    public SQLiteDatabase db = null;
    private Context context;
    public MyDbHelper helper;
    private int i = 0;
    private static final int PERMISSON_REQUESTCODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        helper = new MyDbHelper(context);
        btn1 = (Button) findViewById(R.id.insert);
        btn2 = (Button) findViewById(R.id.query);
        btn3 = (Button) findViewById(R.id.update);
        btn4 = (Button) findViewById(R.id.delete);
        name = (EditText) findViewById(R.id.Name);
        phone = (EditText) findViewById(R.id.Phone);
        textview = (TextView) findViewById(R.id.textview);
        db = helper.getWritableDatabase();
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},
                    PERMISSON_REQUESTCODE);//自定义的code
        }
//        SQLiteDatabase.openOrCreateDatabase("/data/data/database.tangqi.com.database/test.db",null);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = helper.getWritableDatabase();
                helper.insert();
//                Toast.makeText(MainActivity.this, "helper.insert()成功!", Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = helper.getWritableDatabase();
                helper.query();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = helper.getWritableDatabase();
                helper.update();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = helper.getWritableDatabase();
                helper.delete();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        SQLiteDatabase.openOrCreateDatabase("/data/data/database.tangqi.com.database/test.db",null);
//        db=helper.getWritableDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public class MyDbHelper extends SQLiteOpenHelper {

        public MyDbHelper(Context context) {
            super(context, "test.db", null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE usertable(personid INTEGER PRIMARY KEY AUTOINCREMENT  ,name text ,phone,text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists usertable");
            onCreate(db);
        }

        private void insert() {
            ContentValues val = new ContentValues();
            name = (EditText) findViewById(R.id.Name);
            phone = (EditText) findViewById(R.id.Phone);
            String str = name.getText().toString();
            String str2 = phone.getText().toString();
            if (str.length() != 0 && str2.length() != 0) {
                String sql = "INSERT INTO usertable(name, phone)"
                        + " VALUES ('" + str + "','" + str2 + "')";
                db.execSQL(sql);
                Toast.makeText(MainActivity.this, "插入数据成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "请输入完整数据!", Toast.LENGTH_SHORT).show();
            }
        }

        private void query() {
//            db=helper.getWritableDatabase();
            String phone1 = null;
            String name1 = null;
            String personid1 = null;
            List<String> list = new ArrayList<String>();
            Cursor c = db.query("usertable", null, null, null, null, null, null);
            if (c.moveToFirst()) {
                do {
                    personid1 = c.getString(c.getColumnIndex("personid"));
                    name1 = c.getString(c.getColumnIndex("name"));
                    phone1 = c.getString(c.getColumnIndex("phone"));
                    list.add(personid1 + " " + name1 + "  " + phone1+"\n");
                } while (c.moveToNext());
            }
            c.close();
//            db.close();
            textview.setText(list.toString());
        }

        private void update() {
            Cursor c = db.query("usertable", null, null, null, null, null, null);
            name = (EditText) findViewById(R.id.Name);
            phone = (EditText) findViewById(R.id.Phone);
            String str = name.getText().toString();
            String str2 = phone.getText().toString();
            if (str.length() != 0 && str2.length() != 0) {
                String sql = "update usertable set name='" + str + "' where personid =1";
                db.execSQL(sql);
                String sql2 = "update usertable set phone='" + str2 + "' where personid =1";
                db.execSQL(sql2);
                Toast.makeText(MainActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "请输入完整数据!", Toast.LENGTH_SHORT).show();
            }
        }

        private void delete() {
            String sql = ("drop table usertable");
            db.execSQL(sql);
            db.execSQL("CREATE TABLE usertable(personid INTEGER PRIMARY KEY AUTOINCREMENT  ,name text ,phone,text)");
            Toast.makeText(MainActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);//可在此继续其他操作。
    }
    private void doNext(int requestCode, int[] grantResults) {
        helper = new MyDbHelper(this);
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                db.openOrCreateDatabase("/data/data/database.tangqi.com.database/test.db", null);
//                Toast.makeText(MainActivity.this, "openOrCreateDatabase!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied
            }
        }
    }
}
