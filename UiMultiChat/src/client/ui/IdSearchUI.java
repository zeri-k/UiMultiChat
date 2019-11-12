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
import client.dtos.IdSearchDTO;

public class IdSearchUI extends JFrame {

	private JPanel contentPane;
	private JTextField tfName;
	private JTextField tfTel;
	private ObjectOutputStream oos;
	private BufferedOutputStream bos;

	public IdSearchUI() {
		setTitle("아이디 찾기");
		setVisible(true);
		setBounds(100, 100, 230, 158);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("   이름   ");
		panel.add(lblNewLabel);
		
		tfName = new JTextField();
		panel.add(tfName);
		tfName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("전화번호");
		panel.add(lblNewLabel_1);
		
		tfTel = new JTextField();
		panel.add(tfTel);
		tfTel.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnSearch = new JButton("찾기");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IdSearchDTO isDTO = new IdSearchDTO();
				isDTO.setName(tfName.getText());
				isDTO.setTel(tfTel.getText());
				
				try {
					bos = new BufferedOutputStream(MainClass.socket.getOutputStream());
					oos = new ObjectOutputStream(bos);
					bos.write(4);
					bos.flush();
					oos.writeObject(isDTO);
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
