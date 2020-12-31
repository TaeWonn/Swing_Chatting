package project.join.sign_up.model.vo;

import java.util.Properties;

public class Customer {

	private String name;	//이름
	private String id;		//아이디  (key)
	private String pwd;		//비밀번호
	private String pn;		//전화번호
	private String gender;   	//성별
	
	
	public Customer() {
		
	}
	//파라미터 생성자
	public  Customer(String name,String id,String pwd,String pn,String gender) {
		this.name = name;
		this.id=id;
		this.pwd = pwd;
		this.pn = pn;
		this.gender =gender;
	}
	
	@Override
	public String toString() {//회원정보
		return "\n이름 :"+name+"\n아이디 : "+id+"\n비밀번호 :"+pwd+"\n전화번호 : "+pn; 
	}
	
	// getter/setter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
