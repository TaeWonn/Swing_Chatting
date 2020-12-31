package project.search.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import project.chatting.LoginPanel;


public class Search {
	JTextField id, name, pn;
	JButton serchId, serchPwd;
	public static String idStr;
	public static String nameStr;
	public static String pnStr;

	JFrame frame = new JFrame("비밀번호 찾기");

	public Search() {
		frame.setBounds(300, 150, 400, 350);
		frame.setLayout(null);
		
		
		panel1();
		panel2();

		frame.setVisible(true);
		frame.setResizable(false);
	}

	public void panel1() {
		JPanel p1 = new JPanel();

		JLabel main = new JLabel("비밀번호 찾기:이름,ID 입력 // ID 찾기:이름,휴대폰 번호(구분문자X)입력");
		main.setBounds(5,3, 400, 28);
		main.setFont(new Font("맑은 고딕",Font.BOLD , 12));
		
		JLabel idLabel = new JLabel("ID");
		idLabel.setBounds(30, 30, 70, 50);
		id = new JTextField(10);
		id.setBounds(150, 30, 200, 50);
		JLabel nameLabel = new JLabel("이름");
		nameLabel.setBounds(30, 90, 70, 50);
		name = new JTextField(10);
		name.setBounds(150, 90, 200, 50);
		JLabel pnLabel = new JLabel("전화번호");
		pnLabel.setBounds(30, 160, 70, 50);
		pn = new JTextField(20);
		pn.setBounds(150, 160, 200, 50);

		p1.setBounds(0, 0, 400, 250);
		p1.setLayout(null);

		p1.add(main);
		p1.add(idLabel);
		p1.add(id);
		p1.add(name);
		p1.add(nameLabel);
		p1.add(pnLabel);
		p1.add(pn);
		frame.add(p1);

	}

	public void panel2() {
		JPanel p2 = new JPanel();
		serchId = new JButton("아이디 찾기");
		serchPwd = new JButton("비밀번호 찾기");

		serchId.setBounds(20, 5, 150, 40);
		serchId.addActionListener(listener);
		serchPwd.setBounds(200, 5, 150, 40);
		serchPwd.addActionListener(listener);

		p2.setBounds(0, 250, 400, 100);
		p2.setLayout(null);
		p2.add(serchId);
		p2.add(serchPwd);

		frame.add(p2);
	}

	ActionListener listener = new ActionListener() {
		String str;

		@Override
		public void actionPerformed(ActionEvent e) {
			UIManager.put("OptionPane.messageFont", new Font("맑은고딕", Font.PLAIN, 20));
			if (e.getSource() == serchPwd) {
				idStr = id.getText();
				nameStr = name.getText();
				str = new LoginPanel().serverjoin(nameStr+","+idStr+","+"0");
				if (str == null) {
					JOptionPane.showMessageDialog(null, "정보가 검색되지 않습니다.");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "비밀번호: " + str);
				}
			} else if (e.getSource() == serchId) {
				nameStr = name.getText();
				pnStr = pn.getText();
				System.out.println(pnStr);
				str = new LoginPanel().serverjoin(nameStr+","+pnStr);
				if (str.equals("null")) {
					JOptionPane.showMessageDialog(null, "정보가 검색되지 않습니다.");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "아이디: " + str);
				}
			}
		}
	};
}
