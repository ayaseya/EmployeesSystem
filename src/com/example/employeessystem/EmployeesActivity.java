package com.example.employeessystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class EmployeesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employees);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment())
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.employees, menu);
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
			ListView listView = (ListView) rootView.findViewById(R.id.employeeListView);

			// SQLiteHelperのコンストラクターを呼び出します。
			EmployeeSQLiteOpenHelper dbHelper = new EmployeeSQLiteOpenHelper(getActivity());
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			// Daoクラスのコンストラクターを呼び出します。
			Dao dao = new Dao(db);

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

}
