package com.example.employeessystem;

// 社員の家族一人分の情報を扱うクラスです。
public class Family {


	private String _id;// 社員コード
	private String relationship;// 社員と家族の関係（続柄）
	private String name;// 家族の名前

	public Family() {
		_id = "00000";
		relationship = "続柄";
		name = "家族の名前";
	}

	public Family(String _id, String relationship, String name) {
		this._id = _id;
		this.relationship = relationship;
		this.name = name;
	}

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
