package com.example.employeessystem;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
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
	private Context context;

	/**
	 * コンストラクターでSQLiteDatabaseを引数とします。
	 *
	 * @param db
	 */
	public Dao(SQLiteDatabase db, Context context) {
		this.db = db;
		this.context = context;
	}

	// DBに登録された全社員を取得します。
	public List<Employee> findAllEmployees() {
		List<Employee> list = new ArrayList<Employee>();

		String sql = "SELECT * FROM employees_tbl;";

		Log.v("TEST", "SQL文=" + sql);

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

		cursor.close();
		return list;
	}

	// DBに登録された全家族を取得します。
	public List<Family> findAllFamily() {
		List<Family> list = new ArrayList<Family>();

		String sql = "SELECT * FROM family_tbl;";

		Log.v("TEST", "SQL文=" + sql);

		// 第一引数SQL文、第二引数はSQL文内に埋め込まれた「?」にはめ込むString配列です。
		//
		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			Family family = new Family();
			family.set_id(cursor.getString(0));
			family.setRelationship(cursor.getString(1));
			family.setName(cursor.getString(2));

			Log.v("TEST", "家族>" + cursor.getString(0) + "|" + cursor.getString(1) + "|" + cursor.getString(2));

			list.add(family);
		}
		cursor.close();
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

	// 1人分の家族のデータを追加して社員Mの家族数に反映させます。
	public boolean insertF(Family family) {

		boolean success = true;

		// 社員Mを操作するため、家族Mに社員MをATTACHします。
		// 第二引数は第一引数の?の箇所をバインド(置き換え)します。
		// この場合、社員Mのファイルパスに置き換えています。
		db.execSQL("ATTACH DATABASE ? AS employees_db",
				new String[] { context.getDatabasePath("employees_db").getPath() });

		// トランザクション処理の記述方法
		// http://asai-atsushi.blog.so-net.ne.jp/2012-06-11
		// トランザクション処理でロック種類の指定方法
		// http://yuki312.blogspot.jp/2012/07/androidsqlitetransaction.html

		// 「Immediateモード」でロックされます。
		// ロック中、他ユーザはデータの読み取りはできても書き込みはできません。
		db.beginTransactionNonExclusive();

		try {
			// ContentValuesはカラム名をキーとして、値を保存するクラスです。
			ContentValues values = new ContentValues();
			values.put(COLUMN_ID, family.get_id());
			values.put(COLUMN_RELATIONSHIP, family.getRelationship());
			values.put(COLUMN_NAME, family.getName());

			// 家族MにデータをINSERTします。
						db.insert(FAMILY_TABLE_NAME, null, values);

//			if (db.insert(FAMILY_TABLE_NAME, null, values) == -1) {
//				Log.v("TEST", "INSERT失敗");
//				throw new Exception("INSERT失敗");
//
//			}
//			Log.v("TEST", "INSERT処理後");

			// 社員Mを更新するためvaluesを新規に作成します。
			values = new ContentValues();
			// 家族数を設定します。(本人を含めるため+1する。)
			values.put(COLUMN_DEPENDENTS, countFamily(family.get_id()) + 1);

			String whereClause = "_id = '" + family.get_id() + "'";// 更新条件に_idを設定します。
			db.update(EMPLOYEE_TABLE_NAME, values, whereClause, null);



			db.setTransactionSuccessful();// DB処理が成功した場合に実行します。

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			Log.v("TEST", "エラー:" + e.toString());

		} finally {
			//setTransactionSuccessful()が実行されていない場合、ロールバックが実行されます。
			//setTransactionSuccessful()が実行された場合、コミットが実行されます。
			db.endTransaction();

		}
		return success;
	}

	// 引数で渡した社員コードと一致する家族数をカウントします。
	public int countFamily(String _id) {

		int dependents = 0;// 家族数(家族Mで社員コードと一致した人数)

		String sql = "SELECT * FROM family_tbl WHERE _id ='" + _id + "';";
		Log.v("TEST", "SQL文=" + sql);

		// 第一引数SQL文、第二引数はSQL文内に埋め込まれた「?」にはめ込むString配列です。

		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			Family family = new Family();
			family.set_id(cursor.getString(0));
			family.setRelationship(cursor.getString(1));
			family.setName(cursor.getString(2));

			Log.v("TEST", "家族>" + cursor.getString(0) + "|" + cursor.getString(1) + "|" + cursor.getString(2));

			dependents++;
		}
		cursor.close();
		return dependents;
	}

	// 社員MのUPDATE処理を実行します。
	public long updateEmployee(Employee employee) {
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, employee.get_id());
		values.put(COLUMN_NAME, employee.getName());
		values.put(COLUMN_DEPENDENTS, employee.getDependents());

		String whereClause = "_id = '" + employee.get_id() + "'";// 更新条件に_idを設定します。
		return db.update(EMPLOYEE_TABLE_NAME, values, whereClause, null);
	}

	// 1人分の家族のデータを削除します。

	public int deleteFamily(String _id, String relationship) {
		return db.delete(FAMILY_TABLE_NAME, " _id = '" + _id + "' AND relationship = '" + relationship + "'", null);

	}

	public void deleteF(String _id, String relationship) {

		db.execSQL("ATTACH DATABASE ? AS employees_db",
				new String[] { context.getDatabasePath("employees_db").getPath() });

		db.beginTransactionNonExclusive();
		try {
			db.delete(FAMILY_TABLE_NAME, " _id = '" + _id + "' AND relationship = '" + relationship + "'", null);

			// ContentValuesはカラム名をキーとして、値を保存するクラスです。
			ContentValues values = new ContentValues();

			// 家族数を設定します。(本人を含めるため+1する。)
			values.put(COLUMN_DEPENDENTS, countFamily(_id) + 1);

			String whereClause = "_id = '" + _id + "'";// 更新条件に_idを設定します。
			db.update(EMPLOYEE_TABLE_NAME, values, whereClause, null);

			db.setTransactionSuccessful();// DB処理が成功した場合に実行します。
		} catch (Exception e) {
			e.printStackTrace();
			Log.v("TEST", "エラー:" + e.toString());
		} finally {
			//setTransactionSuccessful()が実行されていない場合、ロールバックが実行されます。
			//setTransactionSuccessful()が実行された場合、コミットが実行されます。
			db.endTransaction();
		}
	}

}
