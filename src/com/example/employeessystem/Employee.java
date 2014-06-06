package com.example.employeessystem;

// 社員一人分の情報を扱うクラスです。
public class Employee {

	private String _id;// 社員コード
	private String name;// 名前
	private int dependents;// 扶養家族の人数（本人を1人としてカウントする）

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDependents() {
		return dependents;
	}
	public void setDependents(int dependents) {
		this.dependents = dependents;
	}

}
