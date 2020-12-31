package project.chatting;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import project.join.sign_up.controller.CustomerController;
import project.join.sign_up.view.Sign1;
import project.search.view.Search;

public class LoginPanel extends JFrame {

	JLabel signUp;
	JLabel findInfo;
	JTextField inputId;
	JPasswordField inputPassword;
	JLabel outputMsg;
	Socket socket;
	String a;
	
	public LoginPanel() {}
	
	
	public String serverjoin(String info) {
		try {
//			String ip = "192.168.25.32";
			String ip = "200.200.200.234";
			socket = new Socket(ip,4000);
			
			new ClitensJoin().sender(socket,info);
			String s= "";
			
			Thread thread = new Thread(new ClitensJoin(socket));
			thread.run();
			
			socket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	public static void main(String[] args) {
		LoginPanel lp = new LoginPanel(200, 200, 500, 400);
		String name = lp.inputId.getText();
	}

	public LoginPanel(int x, int y, int w, int h) {
		setBounds(x, y, w, h);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Login");
		setLayout(null);

		JLabel id = new JLabel("아이디 : ");
		id.setBounds(50, 100, 100, 50);

		// 사용자 입력폼
		inputId = new JTextField(20);
		inputId.setBounds(110, 100, 200, 50);

		JLabel password = new JLabel("비밀번호 : ");
		password.setBounds(40, 160, 70, 30);

		inputPassword = new JPasswordField(20);
		inputPassword.setBounds(110, 160, 200, 50);
		

		JButton btn = new JButton("로그인");
		btn.setBounds(330, 100, 110, 110);
		btn.addActionListener(new MyActionListener());

		signUp = new JLabel("계정이 없으신가요?");
		signUp.setForeground(Color.BLUE);
		signUp.setBounds(140, 270, 200, 20);
		signUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		signUp.addMouseListener(new MyListener());

		findInfo = new JLabel("아이디/비밀번호 찾기");
		findInfo.setForeground(Color.BLUE);
		findInfo.setBounds(140, 300, 200, 20);
		findInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		findInfo.addMouseListener(new MyListener());

		outputMsg = new JLabel();
		outputMsg.setBounds(140, 230, 200, 20);

		// 컨테이너 객체에 추가

		add(id);
		add(inputId);
		add(password);
		add(inputPassword);
		add(btn);
		add(outputMsg);
		add(signUp);
		add(findInfo);
		setResizable(false);
		setVisible(true);
	}

	class MyListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == signUp) {
				//회원 가입 프레임 생성
				Sign1 sign = new Sign1();
			}
			if (e.getSource() == findInfo) {
				//비밀 번호 찾기 프레임 생성
				Search se = new Search();
			}
		}
	}

	class MyActionListener implements ActionListener {
		boolean bool = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			CustomerController cc = new CustomerController();
			String s = String.valueOf(inputPassword.getPassword());

			//txt 파일과 비교후 아이디 패스워드 일치시 로그인
			String str = new LoginPanel().serverjoin(inputId.getText()+","+s+",0,0");
			
			if(str.equals("true")) {
				new ClientCopy(inputId.getText());
				dispose();
			} else {
				outputMsg.setText("잘못된 입력입니다.");
			}
		}
	}
	class ClitensJoin extends Thread{
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		public ClitensJoin() {}
		
		public ClitensJoin(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new  DataOutputStream(socket.getOutputStream());
			}catch(IOException e) {
			}
			
		}
		public void sender(Socket socket,String msg) {
			try {
				this.socket = socket;
				out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(msg);
			}catch(IOException e) {
			}
		}
		public void run() {
			String s = "";
			while(in != null) {
				try {
					// 메시지 수신용
					s = in.readUTF();
					//CliteCopy 클래스에 메세지 전송 메소드 호출
				}catch(IOException e) {
				}
				a= s;
				return;
			}
		}
	}
	

}