package project.join.sign_up.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import project.chatting.LoginPanel;
import project.join.sign_up.controller.CustomerController;


public class Sign1 {
	private CustomerController c = new CustomerController();
	private JButton checkId,man,girl,join,cannel;
	private JTextField inId,inPwd,inName,inPn;
	private JLabel idd;
	private JFrame frame;
	private String gender ;
	public Sign1() {

		frame = new JFrame("회원가입");

		frame.setBounds(200, 300, 450, 450);
		frame.setLayout(null);

		JLabel id = new JLabel("ID");
		id.setBounds(50, 35, 150, 50);
		idd = new JLabel("5글자 이상, 특수문자 사용 불가");
		idd.setBounds(120, 75, 200, 30);
		JLabel pwd = new JLabel("비밀번호");
		pwd.setBounds(50, 85, 150, 50);
		JLabel name = new JLabel("이름");
		name.setBounds(50, 145, 150, 50);
		JLabel pn = new JLabel("전화번호");
		pn.setBounds(50, 195, 150, 50);
		JLabel gen = new JLabel("성별");
		gen.setBounds(50, 245, 150, 50);

		inId = new JTextField(20);
		inId.setBounds(130, 50, 150, 30);
		inPwd = new JTextField(20);
		inPwd.setBounds(130, 100, 150, 30);
		inName = new JTextField(20);
		inName.setBounds(130, 155, 150, 30);
		inPn = new JTextField(20);
		inPn.setBounds(130, 210, 150, 30);
		checkId = new JButton("중복 확인");
		checkId.setBounds(310, 47, 90, 50);
		checkId.addActionListener(new Myaction());
		JLabel print = new JLabel();
		
		man = new JButton("남");
		man.setBounds(130, 255, 50, 30);
		girl = new JButton("여");
		girl.setBounds(190, 255, 50, 30);
		join = new JButton("회원가입");
		join.setBounds(90, 330,110, 30);
		cannel= new  JButton("취소");
		cannel.setBounds(220, 330, 70, 30);

		man.addActionListener(new  Myaction());
		girl.addActionListener(new  Myaction());
		join.addActionListener(new Myaction());
		cannel.addActionListener(new  Myaction());
		
		frame.add(id);
		frame.add(idd);
		frame.add(pwd);
		frame.add(name);
		frame.add(pn);
		frame.add(inId);
		frame.add(inPwd);
		frame.add(inName);
		frame.add(inPn);
		frame.add(checkId);
		frame.add(gen);
		frame.add(man);
		frame.add(girl);
		frame.add(join);
		frame.add(cannel);

		print.setBounds(130, 250, 150, 70);
		frame.add(print);

		frame.setResizable(false);

		frame.setVisible(true);
	}
	
	class Myaction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Socket s = null;
			String ip = "";
			String info= "";
			if(e.getSource()==checkId) {
				info = new LoginPanel().serverjoin(inId.getText());
				//회원이 있을떄
				if(info.equals("true")) {
				idd.setText("이미 사용중인 아이디 입니다");
				}else {
				//회원이 없을떄
				idd.setText("사용가능한 아이디입니다");
				}
			}else if(e.getSource()==man) {
				gender ="남";
			}else if(e.getSource()==girl) {
				gender = "여";
			}else if(e.getSource()==join) {
				String id =inId.getText();
				String pwd = inPwd.getText();
				String name =inName.getText();
				String pn = inPn.getText();
				if(c.nullPoint(id, pwd, pn, name, gender).equals("")) {	  //빈값 있는지 확인하기
					info = new  LoginPanel().serverjoin(name+","+id+","+pwd+","+pn+","+gender.toString());
					System.out.println(info);
					if(info.equals("succes")==false) {
						idd.setText(c.nullPoint(id, pwd, pn, name, pn)+"을 확인해주세요");	
						return;
					}
					frame.dispose();			
				}else {
					idd.setText(c.nullPoint(id, pwd, pn, name, pn)+"을 확인해주세요");	
				}
				
			}else if(e.getSource()==cannel) {
				frame.dispose();
			}
		}//end of action
	}//end of actionClass

}

