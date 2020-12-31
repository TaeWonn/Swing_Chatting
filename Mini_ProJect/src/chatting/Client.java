package chatting;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Client extends JFrame {
	String id;
	GridBagConstraints gbc = new GridBagConstraints();
	JButton send;
	JButton close;
	JTextField input;
	public JTextArea txtArea;
	MessageSender sender;

	
	public Client() {
	}

	
	// 로그인한 ID를 이용한 접속(Login에서 쓸 메소드)
	public Client(String id) {
		this.id = id;
		Socket socket;

		try {
			socket = new Socket("200.200.200.226", 5000);
			System.out.println("서버에 연결되었습니다. 채팅을 시작합니다.");

			// 시작하자 마자 서버에 아이디 보내기
			sender = new MessageSender(socket);

			Thread thread = new Thread(new ClientJoin(socket));
			thread.start();

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} finally {
			ChatFrame();
		}

	}

	public void setMessage(String message) {

		// String문자열의 length는 1부터 시작.
		int num = 1;
		for (int i = 1; i <= message.length(); i++) {
			if (i % 28 == 0) {
				String str = message.substring(num, i) + "\n";
				num = i;
				txtArea.append(str);
			}
		}
		if (!(num % 28 == 0)) {
			txtArea.append(message.substring(num) + "\n");
		}
		int txtArealength = txtArea.getText().length();
		txtArea.setCaretPosition(txtArealength);
	}

	// 메시지 전송용 클래스
	public class MessageSender {
		Socket socket;
		DataInputStream di;
		DataOutputStream dos;

		public MessageSender() {
		}

		// 접속하자마자 서버에 ID전송하는 메소드
		public MessageSender(Socket socket) {
			this.socket = socket;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				// 서버에 ID전송
				if (dos != null) {
					dos.writeUTF(id);
				}
			} catch (IOException e) {
			}

		}

		// 메세지 서버에 전송하는 메소드
		public void sender(String msg) {
			if (dos != null) {
				try {
					dos.writeUTF(" [" + id + "] " + msg);
				} catch (IOException e) {
				}
			}
		}
	}

	// 클라이언트 쓰레드(1명 접속시 1개의 쓰레드를 만들고 서버에 입력된 값을 읽어온다.)
	public class ClientJoin implements Runnable {

		Socket socket;
		DataInputStream di;

		public ClientJoin() {
		}

		public ClientJoin(Socket socket) {
			this.socket = socket;
			try {
				this.di = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {

			}
		}

		@Override
		public void run() {
			while (di != null) {
				try {
					String message = di.readUTF();
					Client.this.setMessage(message);
				} catch (IOException e) {
				}
			}
		}
	}

	// 채팅창 구현
	public void ChatFrame() {
		Image img = new ImageIcon("src/macaron.jpg").getImage();
        img = img.getScaledInstance(500, 500, Image.SCALE_DEFAULT);
        setContentPane(new JLabel(new ImageIcon(img)));
		addRootPanel();
		showMsg();
		addinputMsg();

		setVisible(true);
	}

	public void addRootPanel() {
		setTitle(id + "님의 마카롱 채팅");
		setBounds(100,100,500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new GridBagLayout());
		
	}

	public void showMsg() {
		gbc.insets = new Insets(0, 3, 0, 3);
		// gbc.ipadx=20;
		// gbc.ipady=20;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.9;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		txtArea = new JTextArea();
		txtArea.setText("");
		txtArea.setBackground(Color.PINK);
		txtArea.setBorder(new LineBorder(Color.black));
		txtArea.setEditable(false);
		txtArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		JScrollPane scroll = new JScrollPane(txtArea);
		

		this.add(scroll, gbc);

	}

	public void addinputMsg() {
		send = new JButton("전송");
		close = new JButton("닫기");
		input = new JTextField(20);
		gbc.weighty = 0.1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		// gbc.weightx=0.5;
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(input, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		// gbc.weightx=0.1;
		this.add(send, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		this.add(close, gbc);

		input.addKeyListener(key);
		send.addActionListener(act);
		close.addActionListener(act);

	}
	/////////////// 채팅창 구현 끝//////////////////

	// 액션 리스너 - 마우스 클릭
	ActionListener act = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == send) {
				String message = null;
				message = input.getText();
				sender.sender(message);
				// 입력창 비워주기
				input.setText("");
				input.requestFocus();

			} else if (e.getSource() == close) {
				// 닫기 창을 누를 시에 채팅 프로그램이 종료됨.
				System.exit(0);
			}
		}
	};
	// 액션리스너 - input에서 엔터 쳤을때.
	KeyListener key = new KeyAdapter() {
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String message = input.getText();
				sender.sender(message);
				input.setText("");
				input.requestFocus();
			}
		}
	};

}
