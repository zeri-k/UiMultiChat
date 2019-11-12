package client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.MainClass;
import client.dtos.LoginDTO;

public class LoginUI extends JFrame {
	private JPanel contentPane;
	private JTextField tfId;
	private JPasswordField tfPw;
	private ObjectOutputStream oos;
	private BufferedOutputStream bos;

	public LoginUI() {
		initialize();
		setVisible(true);
	}

	private void initialize() {
		setTitle("GtTalk");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 474, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panTitle = new JPanel();
		panel.add(panTitle, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("GtTalk");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 62));
		panTitle.add(lblNewLabel);
		
		JPanel panMember = new JPanel();
		panel.add(panMember, BorderLayout.SOUTH);
		
		JButton btnJoin = new JButton("회원가입");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new JoinUI();
			}
		});
		panMember.add(btnJoin);
		
		JButton btnIdSearch = new JButton("ID 찾기");
		btnIdSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new IdSearchUI();
			}
		});
		panMember.add(btnIdSearch);
		
		JButton btnPwSearch = new JButton("PW 찾기");
		btnPwSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PwSearchUI();
			}
		});
		panMember.add(btnPwSearch);
		
		JPanel panLogin = new JPanel();
		panel.add(panLogin, BorderLayout.CENTER);
		panLogin.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel_1 = new JLabel("");
		panLogin.add(lblNewLabel_1);
		lblNewLabel_1.setPreferredSize(new Dimension(250, 100));
		
		tfId = new JTextField(20);
		tfId.setText("아이디");
		tfId.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(tfId.getText().equals(""))
					tfId.setText("아이디");
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				tfId.setText("");
			}
		});
		
		panLogin.add(tfId);
		
		tfPw = new JPasswordField(20);
		tfPw.setEchoChar((char)0);
		tfPw.setText("패스워드");
		tfPw.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(new String(tfPw.getPassword()).equals("")) {
					tfPw.setEchoChar((char)0);
					tfPw.setText("패스워드");
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				tfPw.setText("");
				tfPw.setEchoChar('*');
			}
		});
		
		tfPw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Login();
			}
		});
		
		panLogin.add(tfPw);
		
		
		
		JButton btnLogin = new JButton("로그인");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login();
			}
		});
		panLogin.add(btnLogin);
		btnLogin.setPreferredSize(new Dimension(250, 30));
	}
	
	public void Login() {
		LoginDTO lDTO = new LoginDTO();
		lDTO.setId(tfId.getText());
		lDTO.setPw(new String(tfPw.getPassword()));
		
		try {
			bos = new BufferedOutputStream(MainClass.socket.getOutputStream());
			oos = new ObjectOutputStream(bos);
			bos.write(1);
			bos.flush();
			oos.writeObject(lDTO);
            oos.flush();
            oos.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
