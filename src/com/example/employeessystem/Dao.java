package com.example.employeessystem;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Dao {

	public static final String EMPLOYEE_TABLE_NAME = "employees_tbl";
	public static final String FAMILY_TABLE_NAME = "family_tbl";

	public static final String COLUMN_ID = "_id";//
	public static final String COLUMN_NAME = "name";//
	public static final String COLUMN_DEPENDENTS = "dependents";//
	public static final String COLUMN_RELATIONSHIP = "relationship";//

	private static final String[] EMPLOYEE_TABLE_COLUMNS = { COLUMN_ID, COLUMN_NAME, COLUMN_DEPENDENTS };
	private static final String[] FAMILY_TABLE_COLUMNS = { COLUMN_ID, COLUMN_RELATIONSHIP, COLUMN_NAME };

	private SQLiteDatabase db;

	/**
	 * コンストラクターでSQLiteDatabaseを引数とします。
	 *
	 * @param db
	 */
	public Dao(SQLiteDatabase db) {
		this.db = db;
	}

	// DBに登録された全社員を取得します。
	public List<Employee> findAllEmployees() {
		List<Employee> list = new ArrayList<Employee>();

		String sql = "SELECT * FROM employees_tbl;";

		Log.v("CMS", "SQL文=" + sql);

		// 第一引数SQL文、第二引数はSQL文内に埋め込まれた「?」にはめ込むString配列です。
		//
		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			Employee employee = new Employee();
			employee.set_id(cursor.getString(0));
			employee.setName(cursor.getString(1));
			employee.setDependents(cursor.getInt(2));

			Log.v("TEST", "社員>" + cursor.getString(0) + "|" + cursor.getString(1) + "|" + cursor.getInt(2));

			list.add(employee);
		}
		return list;
	}

	// DBに登録された全家族を取得します。
	public List<Family> findAllFamily() {
		List<Family> list = new ArrayList<Family>();

		String sql = "SELECT * FROM family_tbl;";

		Log.v("CMS", "SQL文=" + sql);

		// 第一引数SQL文、第二引数はSQL文内に埋め込まれた「?」にはめ込むString配列です。
		//
		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			Family family = new Family();
			family.set_id(cursor.getString(0));
			family.setRelationship(cursor.getString(1));
			family.setName(cursor.getString(2));

			Log.v("TEST", "社員>" + cursor.getString(0) + "|" + cursor.getString(1) + "|" + cursor.getString(2));

			list.add(family);
		}
		return list;
	}

	// 1人分の家族のデータを追加します。
	public long insertFamily(Family family) {
		// ContentValuesはカラム名をキーとして、値を保存するクラスです。
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, family.get_id());
		values.put(COLUMN_RELATIONSHIP, family.getRelationship());
		values.put(COLUMN_NAME, family.getName());

		return db.insert(FAMILY_TABLE_NAME, null, values);
	}

	// 1人分の家族のデータを削除します。

	public int deleteFamily(String _id, String relationship) {
		return db.delete(FAMILY_TABLE_NAME, " _id = '" + _id + "' AND relationship = '" + relationship + "'", null);

	}
}
