package client.ui;

import java.awt.BorderLayout;
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
import javax.swing.border.EmptyBorder;

import client.MainClass;
import client.dtos.PwSearchDTO;

public class RePwUI extends JFrame {

	private JPanel contentPane;
	private JPasswordField tfPw;
	private JPasswordField tfRePw;
	private ObjectOutputStream oos;
	private BufferedOutputStream bos;

	public RePwUI(String id) {
		setTitle("비밀번호 재설정");
		setVisible(true);
		setBounds(100, 100, 233, 163);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("   비밀번호    ");
		panel.add(lblNewLabel);
		
		tfPw = new JPasswordField();
		panel.add(tfPw);
		tfPw.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("비밀번호 확인");
		panel.add(lblNewLabel_1);
		
		tfRePw = new JPasswordField();
		panel.add(tfRePw);
		tfRePw.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnSearch = new JButton("확인");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(new String(tfPw.getPassword()).equals(new String(tfRePw.getPassword()))) {
					PwSearchDTO pwDTO = new PwSearchDTO();
					pwDTO.setId(id);
					pwDTO.setPw(new String(tfPw.getPassword()));
					System.out.println(pwDTO.getPw());
					
					try {
						bos = new BufferedOutputStream(MainClass.socket.getOutputStream());
						oos = new ObjectOutputStream(bos);
						bos.write(7);
						bos.flush();
						oos.writeObject(pwDTO);
			            oos.flush();
			            oos.reset();
			            
			            JOptionPane.showMessageDialog(null, "비밀번호 재설정이 완료 되었습니다.");
			            dispose();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(null, "비밀번호가 일치 하지 않습니다.");
				}
			}
		});
		panel_1.add(btnSearch);
		
		JButton btnCancel = new JButton("취소");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel_1.add(btnCancel);
	}

}
