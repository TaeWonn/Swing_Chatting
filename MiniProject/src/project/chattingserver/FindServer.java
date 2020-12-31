package project.chattingserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FindServer extends Thread{

	Socket socket;
	ServerSocket serverSocket;
	Map<String,DataOutputStream> clitens;
	
	public static void main(String[] args) {
		new FindServer().server();
	}
	
	public FindServer() {
		clitens = Collections.synchronizedMap(new HashMap<>());
	}
	
	public void server() {
		try {
			//서버 생성
			serverSocket = new ServerSocket(4000);
			System.out.println("서버 시작");
			
			while(true) {
				socket = serverSocket.accept();
				System.out.println("----------");
				new ServerReiver(socket).start();
				System.out.println("asdasd");
			}
		}catch(IOException e) {
		}finally {
			System.out.println("접속 종료");
		}
	}
	
	public void send(String msg,String info) {
		try {
			DataOutputStream out =clitens.get(info);
			out.writeUTF(msg);
			
		}catch(IOException e) {
		}
	}
	
	class ServerReiver extends Thread{
		DataInputStream in ;
		DataOutputStream out;
		Socket socket;
		Properties pp = new Properties();
		
		public ServerReiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			}catch(IOException e) {
			}
		}
		
		public boolean login(Enumeration<?> it,String[] info) {
            while(it.hasMoreElements()) {
                String user = (String)it.nextElement();
                String[] userr = pp.getProperty(user).split(","); 
                if(userr[1].equals(info[0])&&userr[2].equals(info[1])) {
                    return true;
                }
               
            }
            return false;
        }
		
		public String findid(Enumeration it,String[] info) {
			while(it.hasMoreElements()) {
				String user = (String)it.nextElement();
				String[] userin = pp.getProperty(user).split(",");
				//이름 		   이름 		번호			번호
				if(userin[0].equals(info[0])&&userin[3].equals(info[1])) {
					return userin[1];
				}
			}
			return null;
		}
		
		public String findpwd(Enumeration it,String[] info) {
			while(it.hasMoreElements()) {
				String user = (String)it.nextElement();
				String[] userin = pp.getProperty(user).split(",");
				//아이디		   아이디		이름			이름
				if(userin[1].equals(info[1])&&userin[0].equals(info[0])) {
					System.out.println("*********"+userin[2]);
					return userin[2];
				}
			}
			return null;
		}
		
		public void signUp(Enumeration it,String[] info) {
			pp.setProperty(info[1], info[0]+","+info[1]+","+info[2]+","+info[3]+","+info[4]);
			System.out.println("check");
			try {
				System.out.println("check2");
				pp.store(new FileWriter("Customer.txt"), info[1]);
			}catch(IOException e) {
			}
		}
		
		public boolean search(Enumeration it,String id) {
			while(it.hasMoreElements()) {
				String user = (String)it.nextElement();
				String[] userinfo = pp.getProperty(user).split(",");
				if(userinfo[1].equals(id)) return true;
			}
			return false;
		}
		
		
		public Enumeration<?> userinfo(){
			try {
				pp.load(new  FileReader("Customer.txt"));
			} catch (IOException e) {
			}
			Enumeration<?> it = pp.propertyNames();
			return it;
		}
		
		public void info(String info) {
			String[] infoo = info.split(",");
			if(infoo.length==1) {			//아이디 중복 체크
				System.out.println("infoooooo");
				if(search(userinfo(),infoo[0])== false) send("false",info);
				else send("true",info);
				
			}else if(infoo.length==2) {	//아이디 찾기
				String id = findid(userinfo(),infoo);
				if(id != null) send(id,info);
				else send("null",info);
				
			}else if(infoo.length==3) {	//비밀번호 찾기
				String pwd = findpwd(userinfo(),infoo);
				if(pwd != null) send(pwd,info);
				send("null",info);
				
			}else if(infoo.length==4) {	//로그인 
				if(login(userinfo(),infoo)) send("true",info);
				else send("false",info);
				
			}else if(infoo.length==5) {	//회원 가입
				signUp(userinfo(),infoo);
				send("succes",info);
			}
		}
		
		@Override
		public void run() {
			String info = "";
			try {
				System.out.println("유저 입장");
				info = in.readUTF();
				System.out.println(info);
				clitens.put(info, out);
				info(info);
				
			}catch(IOException e) {
			}finally {
				System.out.println("유저나감");
				clitens.remove(info);
			}
			
		}
	}
}
