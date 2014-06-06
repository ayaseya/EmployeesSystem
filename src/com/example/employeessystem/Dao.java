package com.example.employeessystem;

import java.util.ArrayList;
import java.util.List;

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

}