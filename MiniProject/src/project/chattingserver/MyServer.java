package project.chattingserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class MyServer {

	ServerSocket serverSocket = null;
	Socket socket = null;
	Map<String,DataOutputStream> clitens;
	
	public static void main(String[] args) {
		new MyServer().server();
	}
	
	public MyServer() {
		//동기화를 사용하여 데이터 변경 방지
		clitens = Collections.synchronizedMap( 
				new HashMap<String,DataOutputStream>());
	}
	
	
	public void server() {
		try {
			//서버 생성
			serverSocket = new ServerSocket(5000);
			System.out.println( " =====서버 시작=====");
			
			//유저 접속 대기
			while(true) {
				socket = serverSocket.accept();
				System.out.println("["+socket.getInetAddress()+"]"+socket.getPort()+"에서 접속");
				
				// 서버에서 클라이언트로 메시지를 전송할 Thread 생성
				new ServerReceiver(socket).start();
			}
			
		}catch(IOException e ) {
			e.printStackTrace();
		}finally {
			try {
				serverSocket.close();
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}//end of finally
	}//end of server
	
	public void findinfo() {
		Socket s;
		try {
			s = serverSocket.accept();
			new ServerReceiver(s);
			
			
		}catch(IOException e) {
		}
	}
	
	
	public void sendAll(String msg) {
		Iterator<String> it = clitens.keySet().iterator();
		//접속한 유저 닉네임 배열 생성
		
		while(it.hasNext()) {
			try {
				String name = it.next();
				DataOutputStream out = clitens.get(name);
				//들어온 내용 전체 유저들에게 뿌려주기
				out.writeUTF(msg);
				System.out.println(msg);
				//서버에도 출력 (테스트용)
			}catch(IOException e) {	//에러잡기 용도
			}
		}//end of while
	}//end of while
	
	
	//서버에서 클라이언트로 메시지 전송할 Thread
	class ServerReceiver extends Thread{
		DataOutputStream out;
		DataInputStream in;
		Socket socket;
		Properties pp = new Properties();
		
		public ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				//서버로 데이터 전송통로 지정
				out = new DataOutputStream(socket.getOutputStream());
				//서베에서 들어오는 데이터 통로 지정
				in = new DataInputStream(socket.getInputStream());
			
			}catch( IOException e){
			}
		}
		
		
		@Override
		public void run() {
			String name ="";
			try {
				name =in.readUTF();
				//name에 읽어온 아이디 입력해주기
				
				//대화방 참여 메시지 전송
				sendAll("#"+name+"님이 입장하셨습니다.");
				
				//접속한 유저 Map에 저장
				clitens.put(name, out);
				System.out.println("접속자 수 :"+clitens.size());
				
				while(in != null) {	//입력대기 상태(무한루프)
					sendAll(in.readUTF());
				}
				
			}catch(IOException e) {	//유저나가면 Exception 발생
			}finally {
				sendAll(name+"님이 나감");
				//접속을 종료한 유저 지우기
				clitens.remove(name);
				
				System.out.println(socket.getInetAddress()+":"+socket.getPort()+"에서 접속 종료");
				System.out.println("접속 자 수 :"+clitens.size());
			}//end of finally
		}//end of run
	} // end of Thread
	
}//end of class
