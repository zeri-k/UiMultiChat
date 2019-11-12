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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.MainClass;
import client.dtos.PwSearchDTO;

public class PwSearchUI extends JFrame {

	private JPanel contentPane;
	private JTextField tfId;
	private JTextField tfEmail;
	private ObjectOutputStream oos;
	private BufferedOutputStream bos;

	public PwSearchUI() {
		setTitle("비밀번호 찾기");
		setVisible(true);
		setBounds(100, 100, 230, 158);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("   아이디   ");
		panel.add(lblNewLabel);
		
		tfId = new JTextField();
		panel.add(tfId);
		tfId.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("   이메일   ");
		panel.add(lblNewLabel_1);
		
		tfEmail = new JTextField();
		panel.add(tfEmail);
		tfEmail.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnSearch = new JButton("찾기");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PwSearchDTO pwDTO = new PwSearchDTO();
				pwDTO.setId(tfId.getText());
				pwDTO.setEmail(tfEmail.getText());
				
				try {
					bos = new BufferedOutputStream(MainClass.socket.getOutputStream());
					oos = new ObjectOutputStream(bos);
					bos.write(6);
					bos.flush();
					oos.writeObject(pwDTO);
		            oos.flush();
		            oos.reset();

		            dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
