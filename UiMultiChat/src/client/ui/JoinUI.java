package client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import client.MainClass;
import client.dtos.JoinDTO;

public class JoinUI extends JFrame {
	private JPanel contentPane;
	private JTextField tfName;
	private JTextField tfId;
	private JPasswordField tfPw;
	private JTextField tfTel;
	private JTextField tfEmail;
	private ObjectOutputStream oos;
	private BufferedOutputStream bos;
	
	public JoinUI() {
		setTitle("회원가입");
		initialize();
		setVisible(true);
	}

	private void initialize() {
		setBounds(100, 100, 210, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(15);
		contentPane.add(panel);
		
		JLabel lblNewLabel_5 = new JLabel();
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_5.setText("회원가입");
		panel.add(lblNewLabel_5);
		lblNewLabel_5.setPreferredSize(new Dimension(200, 50));
		
		JLabel lblNewLabel_1 = new JLabel("아이디  ");
		panel.add(lblNewLabel_1);
		
		tfId = new JTextField();
		panel.add(tfId);
		tfId.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("패스워드");
		panel.add(lblNewLabel_2);
		
		tfPw = new JPasswordField();
		panel.add(tfPw);
		tfPw.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("이름     ");
		panel.add(lblNewLabel);
		
		tfName = new JTextField();
		panel.add(tfName);
		tfName.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("전화번호");
		panel.add(lblNewLabel_3);
		
		tfTel = new JTextField();
		panel.add(tfTel);
		tfTel.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("이메일  ");
		panel.add(lblNewLabel_4);
		
		tfEmail = new JTextField();
		panel.add(tfEmail);
		tfEmail.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JoinDTO jDTO = new JoinDTO();
		
		JButton btnJoin = new JButton("가입");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jDTO.setId(tfId.getText());
				jDTO.setPw(new String(tfPw.getPassword()));
				jDTO.setName(tfName.getText());
				jDTO.setTel(tfTel.getText());
				jDTO.setEmail(tfEmail.getText());
				if(tfId.getText().equals("") || new String(tfPw.getPassword()).equals("") ||tfName.getText().equals("") ||tfTel.getText().equals("") ||tfEmail.getText().equals(""))
					JOptionPane.showMessageDialog(null, "모두 입력해 주세요.");
				else {
					try {
						bos = new BufferedOutputStream(MainClass.socket.getOutputStream());
						oos = new ObjectOutputStream(bos);
						bos.write(2);
						bos.flush();
						oos.writeObject(jDTO);
			            oos.flush();
			            oos.reset();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					dispose();
				}
			}
		});
		panel_1.add(btnJoin);
		
		JButton btnCancel = new JButton("취소");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel_1.add(btnCancel);
		
	}

}
