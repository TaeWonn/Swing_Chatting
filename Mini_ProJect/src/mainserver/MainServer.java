package mainserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class MainServer {
	ServerSocket serverSocket;
	// 접속자의 id를 담기위한 Map
	Map<String, DataOutputStream> clients;
	Socket socket;

	public static void main(String[] args) {
		new MainServer().server();
	}

	public MainServer() {
		clients = Collections.synchronizedMap(new HashMap<String, DataOutputStream>());
	}

	public void server() {
		try {
			serverSocket = new ServerSocket(5000);
			System.out.println("채팅서버 구동중....");
			while (true) {
				socket = serverSocket.accept();
				new ServerReceiver(socket).start();
			}
		} catch (IOException e) {

		} finally {
			try {
				serverSocket.close();
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	// 접속한 유저 모두에게 전달내용을 뿌려주는 메소드
	/*
	 * clients 에 들어있는 유저의 key값을 가져와서 msg를 넣어준다.
	 */
	public void sendAll(String msg) {
		Iterator<String> iter = clients.keySet().iterator();
		while (iter.hasNext()) {
			try {
				String id = iter.next();
				DataOutputStream dos = clients.get(id);
				// 유저가 작성한 텍스트를 서버에다 씀.
				dos.writeUTF(msg);
				// 유저들의 대화내용 서버에서 확인 가능
				System.out.println(msg);
			} catch (IOException e) {
			}
		}
	}

	public class ServerReceiver extends Thread {
		DataOutputStream dos;
		DataInputStream dis;
		Socket socket;
		Properties prop;

		public ServerReceiver() {
		}

		public ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
			}
		}

		@Override
		public void run() {
			String id = null;
			try {
				id = dis.readUTF();
				sendAll("****" + id + "님이 접속하셨습니다.");
				// 접속한 user를 Map에 저장
				clients.put(id, dos);
				System.out.println("접속자수: " + clients.size());
				// 입력대기, 입력이 들어오면 실행
				while (dis != null) {
					sendAll(dis.readUTF());
				}
			} catch (IOException e) {
			} finally {
				sendAll("******" + id + "님이 접속을 종료하셨습니다******");
				clients.remove(id);
				System.out.println("접속자수: " + clients.size());
			}

		}
	}
}
