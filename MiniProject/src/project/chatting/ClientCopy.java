package project.chatting;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class ClientCopy extends JFrame{
	String name;
	MessageSender sender;
	GridBagConstraints gbc = new GridBagConstraints();
	JButton send;
	JButton close;
	JTextField input;
	public JTextArea txtArea;
	

	public ClientCopy(String name) {
		this.name = name;
		Socket socket = null;

		try {
			// 클라이언트 쪽에서 접속할 서버의 ip입력
			String serverIp = "200.200.200.234";
			// 서버의 ip,port번호 입력-연결
			socket = new Socket(serverIp, 5000);
			// ==> socket = new Socket("200.200.200.227",3000);
			System.out.println("서버에 연결되었습니다. 채팅 시작합니다.");

			// 메시지 전송용 Thread
			sender = new MessageSender(socket);
			// 수신용 Thread
			Thread thread = new Thread(new ClientJoin(socket));
			thread.start();

		} catch (ConnectException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		ChatFrame();
	}
	public ClientCopy(String id,String pwd) {
		try {
			String ip = "200.200.200.234";
			Socket s = new Socket(ip,5000);
			DataOutputStream inid = new DataOutputStream(s.getOutputStream());
			inid.writeUTF(id+","+pwd);
			
			
		}catch(IOException e) {
			
		}
	}
	public void ChatFrame() {
		addRootPanel();
		showMsg();
		addinputMsg();

		setVisible(true);
	}

	public void addRootPanel() {
		setTitle("Chat");
		setSize(500, 500);
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
		txtArea = new JTextArea() ;		
		txtArea.setText("");
		txtArea.setBorder(new LineBorder(Color.black));
		txtArea.setEditable(false);
		txtArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		JScrollPane scrollPane = new JScrollPane(txtArea);
		this.add(scrollPane,gbc);
//		this.add(txtArea, gbc);

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

		send.addActionListener(act);
		close.addActionListener(act);

		//엔터 입력시 서버로 전송
		input.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					actionPerformed(null);
				}
			}

		});
	}

	ActionListener act = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == send) {
				String message = null;
				message = input.getText();
				sender.send(message);

				input.setText("");
				input.requestFocus();
			} else if (e.getSource() == close) {
				dispose();
				System.exit(0);
				
			}
		}
	};

	public void actionPerformed(ActionEvent e) {
		String message = null;
		message = input.getText();

		//서버로 입력 내용 전송
		sender.send(message);
		//채팅창 비워주기
		input.setText("");
		input.requestFocus();

	}
	
	// 입력받은 메세지 채팅창에 보여주기
	public void setMessage(String message) {

		int num =0;
		for(int i=0;i<message.length();i++) {
			if(i%28==0) {
				String a= message.substring(num,i)+"\n";
				num = i;
				txtArea.append(a);
			}
		}
		if(!(num%28==0))txtArea.append(message.substring(num,message.length())+"\n");
	}

	// 입력받은 메세지 채팅창에 보여주기


	// 메시지 전송용 내부 클래스
	public class MessageSender {

		Socket socket;
		DataOutputStream doo;

		public MessageSender(Socket socket) {
			this.socket = socket;
			try {
				//서버로 메시지 보내기 통로 지정
				this.doo = new DataOutputStream(socket.getOutputStream());

				if (doo != null)
					doo.writeUTF(name);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		//서버로 메시지 전송
		public void send(String message) {
			if (doo != null) {
				try {
					doo.writeUTF("[" + name + "]" + message);
				} catch (IOException e) {

				}
			}
		}
		
	}
	

	

	// 메시지 전달받기용(채팅창에 띄우기용)클래스
	public class ClientJoin implements Runnable {
		Socket socket;
		DataInputStream di;

		public ClientJoin(Socket socket) {
			this.socket = socket;
			try {
				// 서버로 부터 다른(혹은 본인) 클라이언트가 입력한 메시지 받기
				this.di = new DataInputStream(socket.getInputStream());
				
			} catch (IOException e) {
			}
		}

		@Override
		public void run() {
			while (di != null) {
				try {
					// 메시지 수신용
					String s = di.readUTF();
					//CliteCopy 클래스에 메세지 전송 메소드 호출
					ClientCopy.this.setMessage(s);
				} catch (IOException e) {
				}
			}
		}
	}

}