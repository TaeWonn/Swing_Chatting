package search;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import login.Login;

public class Search extends JFrame {
	JTextField id, name, pn;
	JButton searchId, searchPwd;
	public static String idStr;
	public static String nameStr;
	public static String pnStr;

	public Search() {
		super("ID/Password찾기");
		setBounds(300, 150, 450, 350);
		setLayout(null);
		Image img = new ImageIcon("src/images/macaron.jpg").getImage();
        img = img.getScaledInstance(500, 500, Image.SCALE_DEFAULT);
        setContentPane(new JLabel(new ImageIcon(img)));		

		panel1();
		panel2();

		setVisible(true);
		
	}

	// 입력 패널
	public void panel1() {
		JPanel p1 = new JPanel();
		p1.setOpaque(false);
		p1.setBounds(0, 0, 450, 250);
		p1.setLayout(null);
		
		
		JLabel main = new JLabel("비밀번호 찾기: 이름, ID입력 / ID찾기:이름, 휴대폰 번호(구분문자X)입력");
		main.setBounds(5, 3, 450, 28);
		main.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		
		//ID
		JLabel idLabel = new JLabel("ID");
		idLabel = new JLabel("ID");
		idLabel.setBounds(30, 30, 70, 50);
		id=new JTextField(20);
		id.setBounds(150, 30, 200, 50);
		
		//이름
		JLabel nameLabel = new JLabel("이름");
		nameLabel.setBounds(30, 90, 70, 50);
		name=new JTextField(20);
		name.setBounds(150, 90, 200, 50);
		
		//전화번호
		JLabel pnLabel = new JLabel("전화번호");
		pnLabel.setBounds(30, 160, 70, 50);
		pn=new JTextField(14);
		pn.setBounds(150, 160, 200, 50);
		
		p1.add(main);
		p1.add(idLabel);
		p1.add(id);
		p1.add(name);
		p1.add(nameLabel);
		p1.add(pnLabel);
		p1.add(pn);
		add(p1);
	}

	public void panel2() {
		JPanel p2 = new JPanel();
		searchId = new JButton("아이디 찾기");
		searchPwd = new JButton("비밀번호 찾기");

		p2.setOpaque(false);
		searchId.setBounds(20, 5, 150, 40);
		searchId.addActionListener(new Action());
		searchPwd.setBounds(200, 5, 150, 40);
		searchPwd.addActionListener(new Action());

		p2.setBounds(0, 250, 450, 100);
		p2.setLayout(null);
		p2.add(searchId);
		p2.add(searchPwd);

		add(p2);
	}

	class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UIManager.put("OptionPane.messageFont", new Font("맑은고딕", Font.PLAIN, 20));
			if (e.getSource() == searchId) {
				pnStr = pn.getText();
				nameStr = name.getText();
				
				String str = new Login().serverJoin(nameStr +","+ pnStr);
				if (str == null) {
					JOptionPane.showMessageDialog(null, "정보가 검색되지 않습니다. 바르게 입력해주세요");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "아이디: " + str);
				}
			} else if (e.getSource() == searchPwd) {
				nameStr = name.getText();
				idStr =id.getText();
				String str = new Login().serverJoin(idStr +",0,"+nameStr);
				JOptionPane.showMessageDialog(null, "비밀번호: " + str);
			}
		}
	}
}
