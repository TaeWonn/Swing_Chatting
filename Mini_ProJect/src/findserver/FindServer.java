package findserver;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class FindServer extends Thread {
	Socket socket;
	ServerSocket serverSocket;
	Map<String, DataOutputStream> clients;
	Map <String,Socket> use = new HashMap<>();

	public static void main(String[] args) {
		new FindServer().server();
	}

	public FindServer() {
		clients = Collections.synchronizedMap(new HashMap<>());
	}

	public void server() {
		try {
			serverSocket = new ServerSocket(3000);
			System.out.println("서버 가동중....");

			while (true) {
				socket = serverSocket.accept();
				System.out.println("=============");
				new ServerReceiver(socket).run();

			}
		} catch (IOException e) {
		} finally {
			System.out.println("접속 종료");
		}
	}

	// 입력한 정보를 서버로 전달해주는 메소드
	public void sender(String msg, String info) {

		try {
			// key값의 Value반환
			DataOutputStream dos = clients.get(info);
			dos.writeUTF(msg);
		} catch (IOException e) {
		}
	}

	// 서버에서 확인된 결과 보내주기
	class ServerReceiver {
		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;
		Properties prop = new Properties();

		public ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
			}
		}

		// 받아오는 순서: ID,name,pwd,gender,pn

		// ID값과 Pwd값 비교
		public boolean login(Enumeration iter, String[] info) {
			while (iter.hasMoreElements()) {
				String user = (String) iter.nextElement();
				String[] userInfo = prop.getProperty(user).split(",");
				if (userInfo[0].equals(info[0]) && userInfo[2].equals(info[2])) {
					return true;
				}
			}
			return false;
		}

		// 이름과 pn비교
		public String findId(Enumeration iter, String[] info) {
			while (iter.hasMoreElements()) {
				String user = (String) iter.nextElement();
				String[] userInfo = prop.getProperty(user).split(",");
				if (userInfo[1].equals(info[0]) && userInfo[4].equals(info[1]) == true) {
					// 값이 맞으면 아이디 리턴
					return userInfo[0];
				}
			}
			return null;
		}

		// 아이디와 이름 비교
		public String findPwd(Enumeration iter, String[] info) {
			while (iter.hasMoreElements()) {
				String user = (String) iter.nextElement();
				String[] userInfo = prop.getProperty(user).split(",");
				if(userInfo[0].equals(info[0]) && userInfo[1].equals(info[2])){
					//값이 맞으면 비밀번호 리턴
					return userInfo[2];
				}
			}
			return null;
		}

		public void signUp(Enumeration iter, String[] info) {
			String infoStr = info[0] + "," + info[1] + "," + info[2] + "," + info[3] + "," + info[4];
			prop.setProperty(info[0], infoStr);
			try {
				prop.store(new FileWriter("Customer.txt"), info[0]);
			} catch (IOException e) {
			}
		}

		// ID중복 확인용 메소드. 이 값이 true이면 아이디가 이미 존재한다는 뜻.
		public boolean searchId(Enumeration iter, String id) {
			while (iter.hasMoreElements()) {
				String user = (String) iter.nextElement();
				String userInfo[] = prop.getProperty(user).split(",");
				if (user.equals(id))
					return true;
			}
			return false;
		}

		// User정보 확인
		public Enumeration<?> userInfo() {
			try {
				prop.load(new FileReader("Customer.txt"));
			} catch (IOException e) {
			}
			// prop.propertyNames() 는 key값을 리턴함
			// 현재 key값이 String문자열이므로 문자열 리턴한다.
			Enumeration<?> iter = prop.propertyNames();
			return iter;
		}

		// Join,Search,Login에서 서버로 보내놨던 정보 확인 -문자열로 받음
		public void checkInfo(String info) throws IOException {
			// ID중복체크(Join에서 id값 하나만 보냄
			String[] userInfo = info.split(",");
			if (userInfo.length == 1) {
				if (searchId(userInfo(), userInfo[0]) == false) sender("false", info);
				else sender("true", info); // sender는 정보를 클라이언트에 입력하는 메소드
				
				// ID찾기
			} else if (userInfo.length == 2) {
				String id = findId(userInfo(), userInfo);
				if (id != null) sender(id, info);
				else sender("존재하지 않습니다.", info);

				// 비밀번호 찾기
			} else if (userInfo.length == 3) {
				String pwd = findPwd(userInfo(), userInfo);
				if (pwd != null) sender(pwd, info);
				else sender("존재하지 않습니다.", info);

				// 로그인
			} else if (userInfo.length == 4) {
				if (login(userInfo(), userInfo)) sender("true", info);
				else sender("false", info);

				// 회원가입
			} else if (userInfo.length == 5) {
				// 정보 저장 메소드
				signUp(userInfo(), userInfo);
				sender("succes", info);
			}
		}
		
		public void run() {
			String userInfo = "";
			try {
				System.out.println("유저 입장");
				userInfo = dis.readUTF();
				clients.put(userInfo, dos);
				checkInfo(userInfo);
			} catch (IOException e) {

			} finally {
				System.out.println("유저 정보 확인 후 값 리턴");
				clients.remove(userInfo);
			}
		}
	}

}
