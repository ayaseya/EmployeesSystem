package com.example.employeessystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class EmployeesActivity extends Activity {

	private static SimpleAdapter adapterFamily;
	private static ArrayList<Map<String, String>> familyData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employees);

		// actionbarのホームアイコンを非表示にします。
		getActionBar().setDisplayShowHomeEnabled(false);
		// actionbarのタイトルを非表示にします。
		getActionBar().setDisplayShowTitleEnabled(false);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(), "employee")
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.employees, menu);

		menu.findItem(R.id.action_employee).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.findItem(R.id.action_family).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.findItem(R.id.action_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_employee) {
			getFragmentManager().beginTransaction()
					.replace(R.id.container, new PlaceholderFragment(), "employee")
					.commit();
			return true;
		} else if (id == R.id.action_family) {

			getFragmentManager().beginTransaction()
					.replace(R.id.container, new FamilyFragment(), "family")
					.commit();

			return true;
		} else if (id == R.id.action_add) {

			if (getFragmentManager().findFragmentByTag("family") != null) {
				Log.v("TEST", "family");

				// SQLiteHelperのコンストラクターを呼び出します。
				FamilySQLiteOpenHelper dbHelper = new FamilySQLiteOpenHelper(this);
				SQLiteDatabase db = dbHelper.getReadableDatabase();

				// Daoクラスのコンストラクターを呼び出します。
				Dao dao = new Dao(db, this);

				Family family = new Family("00001", "妻", "花田花子");

				Map<String, String> data = new HashMap<String, String>();
				data.put("_id", family.get_id());
				data.put("relationship", family.getRelationship());
				data.put("name", family.getName());

				// データを1人分追加します。
				if (dao.insertF(family) == true) {

					// リストビューに反映します。
					familyData.add(data);
					adapterFamily.notifyDataSetChanged();
				}else{
					Toast.makeText(this, "エラーが発生したためDBをロールバックします", Toast.LENGTH_SHORT).show();
				}
				db.close();

			} else if (getFragmentManager().findFragmentByTag("employee") != null) {
				Log.v("TEST", "employee");
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_employees, container, false);

			//			TextView tv = (TextView) rootView.findViewById(R.id.textView1);
			// リストビューのインスタンスを取得します。
			ListView listView = (ListView) rootView.findViewById(R.id.familyListView);

			// SQLiteHelperのコンストラクターを呼び出します。
			EmployeeSQLiteOpenHelper dbHelper = new EmployeeSQLiteOpenHelper(getActivity());
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			// Daoクラスのコンストラクターを呼び出します。
			Dao dao = new Dao(db, getActivity());

			// リストに全社員の情報を格納します。
			List<Employee> list = dao.findAllEmployees();

			db.close();

			// SimpleAdapterに渡すデータを作成します。
			List<Map<String, String>> employeeData = new ArrayList<Map<String, String>>();

			// 社員データを連想配列にして、employeeDataに格納します。
			for (Employee tmp : list) {
				Map<String, String> data = new HashMap<String, String>();
				data.put("_id", tmp.get_id());
				data.put("name", tmp.getName());
				data.put("dependents", String.valueOf(tmp.getDependents()));
				employeeData.add(data);
			}

			// アダプターを作成します。
			SimpleAdapter adapter = new SimpleAdapter(getActivity(),
					employeeData,
					R.layout.simple_list_item_employee,
					new String[] { "_id", "name", "dependents" },
					new int[] { R.id.employeeIdTV, R.id.employeeNameTV, R.id.employeeDependentsTV });

			// リストビューにアダプターを設定します。
			listView.setAdapter(adapter);

			//			StringBuilder lines = new StringBuilder();
			//			for (Employee tmp : list) {
			//				lines.append(tmp.get_id());
			//				lines.append("|");
			//				lines.append(tmp.getName());
			//				lines.append("|");
			//				lines.append(tmp.getDependents());
			//				lines.append(System.getProperty("line.separator"));
			//			}
			//
			//			tv.setText(lines.toString());

			return rootView;
		}
	}

	public static class FamilyFragment extends Fragment {

		final static int DELETE_CODE = 1;

		public FamilyFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_family, container, false);

			//			TextView tv = (TextView) rootView.findViewById(R.id.textView1);
			// リストビューのインスタンスを取得します。
			ListView listView = (ListView) rootView.findViewById(R.id.familyListView);

			// SQLiteHelperのコンストラクターを呼び出します。
			FamilySQLiteOpenHelper dbHelper = new FamilySQLiteOpenHelper(getActivity());
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			// Daoクラスのコンストラクターを呼び出します。
			Dao dao = new Dao(db, getActivity());

			// リストに家族の情報を格納します。
			List<Family> list = dao.findAllFamily();

			db.close();

			// SimpleAdapterに渡すデータを作成します。
			familyData = new ArrayList<Map<String, String>>();

			// 社員データを連想配列にして、employeeDataに格納します。
			for (Family tmp : list) {
				Map<String, String> data = new HashMap<String, String>();
				data.put("_id", tmp.get_id());
				data.put("relationship", tmp.getRelationship());
				data.put("name", tmp.getName());
				familyData.add(data);
			}

			// アダプターを作成します。
			adapterFamily = new SimpleAdapter(getActivity(),
					familyData,
					R.layout.simple_list_item_family,
					new String[] { "_id", "relationship", "name" },
					new int[] { R.id.familyIdTV, R.id.familyRelationshipTV, R.id.familyNameTV });

			// リストビューにアダプターを設定します。
			listView.setAdapter(adapterFamily);

			// コンテキストメニューのためにリストビューを登録します。
			registerForContextMenu(listView);

			return rootView;
		}

		// コンテキストメニューを作成します。
		@Override
		public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, view, menuInfo);

			AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			// リストビューにキャストします。
			ListView listView = (ListView) view;
			// クリックされた場所のインスタンスを取得し、Map型のデータにキャストします。
			Map<String, String> data = (Map<String, String>) listView.getItemAtPosition(info.position);

			menu.setHeaderTitle("" + data.get("name") + "のデータを削除しますか?");
			menu.add(0, DELETE_CODE, 0, "OK");

		}

		// コンテキストメニューをクリックした時の挙動を設定します。

		@Override
		public boolean onContextItemSelected(MenuItem item) {

			// リストビューでクリックした場所を取得するためAdapterContextMenuInfoを取得します。
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

			// positionから全家族のデータからクリックされた場所の個人データを取得します。
			Map<String, String> data = familyData.get(info.position);

			switch (item.getItemId()) {
			//削除
			case DELETE_CODE:

				// SQLiteHelperのコンストラクターを呼び出します。
				FamilySQLiteOpenHelper dbHelper = new FamilySQLiteOpenHelper(getActivity());
				SQLiteDatabase db = dbHelper.getReadableDatabase();

				// Daoクラスのコンストラクターを呼び出します。
				Dao dao = new Dao(db, getActivity());

				// DBから削除します。
				dao.deleteF(data.get("_id"), data.get("relationship"));
				// リストからも削除し、アダプターに反映させます。
				familyData.remove(info.position);
				adapterFamily.notifyDataSetChanged();

				Log.v("TEST", data.get("name") + "のデータを削除しました。");

				db.close();

				return true;
			default:
				Log.v("TEST", "コンテキストメニューを選択しました。");
				return super.onContextItemSelected(item);
			}

		}
	}

}
