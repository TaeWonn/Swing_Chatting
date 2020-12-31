package login;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import chatting.Client;
import join.Join;
import search.Search;

public class Login extends JFrame {
	JLabel idL, pwdL, findL, joinL, outputMessage;
	JButton loginButton;
	Socket socket;

	JTextField id;
	JPasswordField pwd;

	DataOutputStream dos;
	DataInputStream dis;

	String result; // 서버에서 읽어온 값을 리턴하는 변수

	public static void main(String[] args) {
		new Login("");
	}

	public Login() {
	}

	public Login(String s) {
		
        
		setBounds(200, 200, 500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("마카롱 채팅");
		setLayout(null);
		// 아이디 입력 부
		idL = new JLabel("아이디 :");
		idL.setBounds(50, 100, 100, 50);

		id = new JTextField(20);
		id.setBounds(110, 100, 200, 50);

		// 비밀번호 입력부
		pwdL = new JLabel("비밀번호 : ");
		pwdL.setBounds(50, 160, 70, 50);
		pwd = new JPasswordField(30);
		pwd.setBounds(110, 160, 200, 50);
		pwd.addKeyListener(new KeyAdapter() {
			// 서버가서 확인 후 맞으면 로그인
			// 틀리면 잘못된 입력입니다 라벨 보이기

		});

		// 로그인 버튼
		loginButton = new JButton("Login");
		loginButton.setBounds(330, 100, 110, 110);
		// 액션리스너 삽입
		loginButton.addActionListener(new MyActionListener());

		// 회원가입 연결 라벨
		joinL = new JLabel("회원가입");
		joinL.setForeground(Color.BLACK);
		joinL.setBounds(140, 250, 200, 20);
		joinL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		joinL.addMouseListener(new MyListener());

		// 아이디/비밀번호 찾기 연결 라벨
		findL = new JLabel("아이디/비밀번호 찾기");
		findL.setForeground(Color.BLACK);
		findL.setBounds(140, 300, 200, 20);
		findL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		findL.addMouseListener(new MyListener());
		// 잘못 입력시에 보일 라벨
		outputMessage = new JLabel();
		outputMessage.setBounds(100, 230, 300, 20);
		
		
		//마카롱 이미지 추가
		Image img = new ImageIcon("src/images/macaron.jpg").getImage();
        img = img.getScaledInstance(500, 500, Image.SCALE_DEFAULT);
        setContentPane(new JLabel(new ImageIcon(img)));

        ///
		add(idL);
		add(pwdL);
		add(id);
		add(pwd);
		add(loginButton);
		add(outputMessage);
		add(findL);
		add(joinL);
		setResizable(false);

		setVisible(true);
	}

	// findServer에 접속해서 정보 확인해주는 메소드
	// 서버에서 ","를 단위로 쪼개서 정보를 저장하므로 String문자열에서 정보 구분할때 ","써준다.
	public String serverJoin(String msg) {
		// 접속할 서버의 ip주소 쓰기
		String ip = "200.200.200.226";
		try {
			// findserver로 접속하는 소켓 생성
			socket = new Socket(ip, 3000);
			// findServer에다 Login 소켓이 보낸 User의 정보를 보냄.
			new ClientJoin().sender(socket, msg);

			// find서버에서 가져온 true,false 값 result에 저장

			new ClientJoin(socket).run();

			socket.close();

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		return result;

	}

	// 서버로 데이터를 보내고 그 데이터에 대한 리턴값을 받는 내부 클래스
	class ClientJoin {
		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public ClientJoin() {
		}

		public ClientJoin(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
			}
		}

		// 서버에 socket이 보낸 정보 저장(userInfo)
		public void sender(Socket socket, String msg) {
			this.socket = socket;
			// 에러 방지용 객체 생성
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(msg);
			} catch (Exception e) {
			}
		}

		public void run() {
			String temp = "";
			while (dis != null) {
				try {
					// FindServer서버에서 보낸 확인용 메소드의 값 true or false 가져옴
					temp = dis.readUTF();

				} catch (Exception e) {
				}
				result = temp;

				return;
			}
		}
	}

	public class MyListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == joinL) {
				Join join = new Join();
			} else if (e.getSource() == findL) {
				Search serch = new Search();
			}
		}

	}

	class MyActionListener implements ActionListener {
		boolean bool = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			String pwdStr = String.valueOf(pwd.getPassword());

			// str의 값은 find서버에서 리턴한 true or false 이다.
			// 로그인 기능이므로 id, pwd만 비교하면 된다. 나머지 이름,gender,pn값은 0으로 준다.0이든 뭐든 관계없다.
			String str = new Login().serverJoin(id.getText() + ",0," + pwdStr + ",0");
			if (str.equals("true")) {
				dispose();
				 new Client(id.getText());
			} else {
				if(str.equals("중복")) outputMessage.setText("이미 접속중인 아이디 입니다");
				outputMessage.setText("잘못된 입력입니다. ID/ Password를 확인하여 주십시오");
			}
		}
	}

}
