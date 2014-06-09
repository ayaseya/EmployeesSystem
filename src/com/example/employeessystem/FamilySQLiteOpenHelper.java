package com.example.employeessystem;

import static com.example.employeessystem.Dao.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FamilySQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "family_db"; // データベース名
	private static final int DATABASE_VERSION = 1; // スキーマバージョン

	// テーブル作成用SQL文です。
	private static final String CREATE_TABLE_SQL =
			"CREATE TABLE " + FAMILY_TABLE_NAME + " ("
					+ COLUMN_ID + " TEXT( 5 ),"
					+ COLUMN_RELATIONSHIP + " TEXT( 10 ),"
					+ COLUMN_NAME + " TEXT( 20 ),"
					+ "PRIMARY KEY (" + COLUMN_ID + "," + COLUMN_RELATIONSHIP + ")"
					+ ");";
	/**
	 * SQL
	 * CREATE TABLE family_tbl (
	 * _id          TEXT( 5 ),
	 * relationship TEXT( 10 ),
	 * name         TEXT( 20 ),
	 * PRIMARY KEY ( _id, relationship )
	 * );
	 */

	static final String DROP_TABLE = "DROP TABLE " + FAMILY_TABLE_NAME + ";";

	public FamilySQLiteOpenHelper(Context context) {
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
