package com.example.employeessystem;

import static com.example.employeessystem.Dao.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmployeeSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "employees_db"; // データベース名
	private static final int DATABASE_VERSION = 1; // スキーマバージョン

	// テーブル作成用SQL文です。
	private static final String CREATE_TABLE_SQL =
			"CREATE TABLE " + EMPLOYEE_TABLE_NAME + " ("
					+ COLUMN_ID + " TEXT( 5 ) PRIMARY KEY,"
					+ COLUMN_NAME + " TEXT( 20 ),"
					+ COLUMN_DEPENDENTS + " INTEGER"
					+ ");";
	/**
	 * SQL
	 * CREATE TABLE employees_tbl (
	 * _id TEXT( 5 ) PRIMARY KEY,
	 * name TEXT( 20 ),
	 * dependents INTEGER
	 * );
	 */

	static final String DROP_TABLE = "DROP TABLE " + EMPLOYEE_TABLE_NAME + ";";

	public EmployeeSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE_SQL); // テーブル作成用SQL実行します。

	}

	// onUpgrade()メソッドはデータベースをバージョンアップした時に呼ばれます。
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

}
