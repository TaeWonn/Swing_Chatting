package join;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.naming.InterruptedNamingException;
import javax.swing.*;

import login.Login;

public class Join extends JFrame {
	private String gender;
	JButton checkId, man, girl, join, cancle;
	JTextField inId, inName, inPn;
	JLabel idInfo, id, pwd, name, gen, pn; // idInfo 는 아이디가 있는지 확인해주는 라벨
	JPasswordField inPwd;


	public Join() {
		super("마카롱채팅 회원가입");

		setBounds(200, 200, 450, 450);
		setLayout(null);

		// 사용할 ID 입력구역
		id = new JLabel("ID");
		idInfo = new JLabel("5글자 이상, 특수문자 사용 가능");
		inId = new JTextField(20);
		id.setBounds(50, 35, 150, 50);
		inId.setBounds(130, 50, 175, 30);
		idInfo.setBounds(120, 75, 200, 30);
		checkId = new JButton("중복 확인");
		checkId.setBounds(310, 47, 90, 50);
		checkId.addActionListener(new MyAction());

		// 비밀번호 영역
		pwd = new JLabel("Password");
		inPwd = new JPasswordField(30);
		pwd.setBounds(50, 85, 150, 50);
		inPwd.setBounds(130, 100, 175, 30);

		// 이름 영역
		name = new JLabel("이름");
		inName = new JTextField(20);
		name.setBounds(50, 145, 150, 50);
		inName.setBounds(130, 155, 175, 30);

		// 전화번호
		pn = new JLabel("전화번호");
		pn.setBounds(50, 195, 150, 50);
		inPn = new JTextField(13);
		inPn.setBounds(130, 210, 175, 30);

		// 성별 선택 영역
		gen=new JLabel("성별");
		gen.setBounds(50, 245, 175, 50);
		man = new JButton("남성");
		man.setBounds(130, 255, 80, 30);
		man.addActionListener(new MyAction());
		girl = new JButton("여성");
		girl.setBounds(230, 255, 80, 30);
		girl.addActionListener(new MyAction());

		// 로그인 버튼
		join = new JButton("회원가입");
		join.setBounds(90, 330, 110, 30);
		join.addActionListener(new MyAction());

		// 취소 버튼
		cancle = new JButton("취소");
		cancle.setBounds(220, 330, 70, 30);
		cancle.addActionListener(new MyAction());
		
		Image img = new ImageIcon("src/images/macaron.jpg").getImage();
        img = img.getScaledInstance(500, 500, Image.SCALE_DEFAULT);
        setContentPane(new JLabel(new ImageIcon(img)));
		
		add(id);
		add(gen);
		add(idInfo);
		add(inId);
		add(checkId);
		add(pwd);
		add(inPwd);
		add(name);
		add(inName);
		add(man);
		add(girl);
		add(join);
		add(cancle);
		add(pn);
		add(inPn);

		setVisible(true);

	}

	class MyAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Socket socket;
			String ip;
			String info;
			
			//아이디 체크 버튼 누를때
			if (e.getSource() == checkId) {
				info = new Login().serverJoin(inId.getText());
				if(info.equals("true")) {
					idInfo.setText("사용할 수 없는 아이디입니다.");
				}else {
					idInfo.setText("사용가능한 아이디입니다.");
				}
				//성별 버튼 누를때.
				}else if(e.getSource()==man) {
					gender = "남";
			}else if(e.getSource()==girl) {
				gender="여";
				//로그인 버튼 누를때-서버에 보내고 저장이 안됐을 때는 빠진 부분 입력하라고 띄워주기
			}else if(e.getSource()==join) {
				String id = inId.getText();
				String pwd = String.valueOf(inPwd.getPassword());
				String name = inName.getText();
				String pn = inPn.getText();
				
				//빈곳이 없다면?
				if(nullInfo(id, name, pwd, gender, pn).equals("")) {
					info = new Login().serverJoin(id+","+name+","+pwd+","+gender+","+pn);
					//빈곳은 없지만 서버에서 저장이 안됐다고 했을 경우
					if(info.equals("succes")==false) {
						idInfo.setText(nullInfo(id, name, pwd, gender, pn)+"을 입력해주세요");
						return;
						
					}
					dispose();
					//빈곳이 있다면
				}else {
					idInfo.setText(nullInfo(id, name, pwd, gender, pn)+"을 입력해주세요");
				}
				
			}else if(e.getSource()==cancle) {
				dispose();
			}
		}//action메소드 끝
	}//actionListener 클래스 끝

	
	// 입력 값중 빈곳이 있는지 확인하는 메소드
	public String nullInfo(String id, String name, String pwd, String gender, String pn) {
		String check = "";
		if (id.equals("")||id.contains(",")) {
			check += "ID,";
		} else if (name.equals("")||name.contains(",")) {
			check += "이름,";
		} else if (pwd.equals("")||pwd.contains(",")) {
			check += "비밀번호,";
		} else if (gender.equals("")||gender.contains(",")) {
			check += "성별,";
		} else if (pn.equals("")||pn.contains(",")) {
			check += "전화번호,";
		}
		return check;
	}
}
