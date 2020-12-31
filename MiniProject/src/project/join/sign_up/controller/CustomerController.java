package project.join.sign_up.controller;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class CustomerController {

	private Properties pp = new  Properties();
	
	public CustomerController() {}
	
	//회원 추가(파라미터)
		public void InputCustomer(String name,String id,String pwd,String pn,String gender) {
			if(seachCustomer(id)==true) {
				//중복된 아이디 있음 출력
//				System.out.println("중복");
				return;
			}
			pp.setProperty(id, name+","+id+","+pwd+","+pn+","+gender);
			try {
				pp.store(new FileWriter("Customer.txt",true),id);
			}catch(IOException e) {
				e.printStackTrace();
			}
			//회원생성완료 메시지 띄우기
		}
		
		
		//회원 찾기(아이디 중복확인)
		public boolean seachCustomer(String id) {
			if(id.equals("")) {
				return true;
			}
			try {
				pp.load(new FileReader("Customer.txt"));
			}catch(IOException e) {
				e.printStackTrace();
			}
			Enumeration<?> e = pp.propertyNames();
			while(e.hasMoreElements()) {
				String temp=(String)e.nextElement();
				//값을 배열에 넣어주기
				String[] userInfo = pp.getProperty(temp).split(",");
				
				//반복문을 통한 유저아이디 검색5
				for(int i=0;i<userInfo.length;i++) {
					if(userInfo[1].equals(id)) {
						return true;  //있으면true리턴
					}
				}
			}
			return false;  //유저가 없으면 false 리턴
		}
		
		
		//회원정보  null값 걸러내기
		public  String nullPoint(String id,String pwd,String pn,String name,String gender) {
			String sel ="";
			if(id.equals("")||id.contains(",")) {
				sel += "아이디";
			}else if(pwd.equals("")||id.contains(",")){
				sel += "패스워드";
			}else if(pn.equals("")||id.contains(",")) {
				sel += "전화번호";
			}else if(name.equals("")||id.contains(",")) {
				sel += "이름";
			}else if(gender.equals("")||id.contains(",")) {
				sel += "성별";
			}
			
			return sel;
		}
		
		//로그인
		public boolean login(String id,char[] password) {
			String s = null;
			try {
				pp.load(new FileReader("Customer.txt"));
			}catch(IOException e) {
				e.printStackTrace();
			}
			for(int i=0;i<password.length;i++) {
				s =String.valueOf(password);
			}
			Enumeration<?> e = pp.propertyNames();
			while(e.hasMoreElements()) {
				String search =(String)e.nextElement();
				String[] userInfo = pp.getProperty(search).split(",");
				
				if(userInfo[1].equals(id) &&userInfo[2].equals(s)) {
					return true;
				}
			}
			return false;
		}
		
		//회원찾기 (비밀번호찾기)
		public String findpwd(String id,String name) {
			try {
				pp.load(new FileReader("Customer.txt")); //불러오기
			}catch(IOException e) {
				e.printStackTrace();
			}
			Enumeration<?> e = pp.propertyNames();
			while(e.hasMoreElements()) {
				String temp = (String) e.nextElement();
				String[] userInfo = pp.getProperty(temp).split(",");
				
				for(int i=0;i<userInfo.length;i++) {
					if(id.equals(userInfo[1])&&name.equals(userInfo[0])) {
						return userInfo[2]; //비밀번호 리턴
					}
				}
			}
			return null;  //회원 정보없으면 null리턴
			//아이디 또는 비밀번호 재입력 메시지띄우기
		}
		
		//아이디 찾기
		public String findId(String name,String pn) {
			try {
				pp.load(new FileReader("Customer.txt"));
			}catch(IOException e ) {
				e.printStackTrace();
			}
			Enumeration<?> ee = pp.propertyNames();
			while(ee.hasMoreElements()) {
				String search = (String)ee.nextElement();
				String[] userInfo = pp.getProperty(search).split(",");
				if(userInfo[0].equals(name)&&userInfo[3].equals(pn)) {
					return userInfo[1].toString();
				}
			}
			return null;
		}
		
}
