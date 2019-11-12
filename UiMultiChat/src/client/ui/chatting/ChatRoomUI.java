package client.ui.chatting;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import client.dtos.ChatRoomDTO;

public class ChatRoomUI extends JFrame implements WindowListener {
	private JPanel contentPane;
	private JTextArea textArea;
	private JPanel panel;
	private ChatTextInputArea textField;
	private JButton btnNewButton;
	private JScrollPane scrollPane;
	private ChatRoomDTO cDTO;

	public ChatRoomUI(String friendName, ChatRoomDTO cDTO) {
		System.out.println(cDTO.getC_id() + " 채팅방 열림");
		this.cDTO = cDTO;
		initialize(friendName);
		setVisible(true);
		addWindowListener(this);
	}

	public void initialize(String friendName) {
		setTitle(friendName);
		setBounds(100, 100, 451, 588);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		textField = new ChatTextInputArea();
		panel.add(textField, BorderLayout.CENTER);

		btnNewButton = new JButton("전송");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.submit();
			}
		});
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 16));
		panel.add(btnNewButton, BorderLayout.EAST);

		try {
			textField.bindSocket(cDTO);
			Thread thread = new ChatRoomThread(textArea, scrollPane, cDTO.getC_id());
			thread.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		try {
			// 바이트 단위로 파일읽기
			String filePath = "src\\client\\chat\\" + cDTO.getC_id() + ".txt"; // 대상 파일
			FileInputStream fileStream = new FileInputStream(filePath);// 파일 스트림 생성
			// 버퍼 선언
			byte[] readBuffer = new byte[fileStream.available()];
			while (fileStream.read(readBuffer) != -1) {
			}
			textArea.setText(new String(readBuffer)); // 출력

			fileStream.close(); // 스트림 닫기
		} catch (Exception e1) {
			e1.getStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		String message = textArea.getText();

		File file = new File("src\\client\\chat\\" + cDTO.getC_id() + ".txt");
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			byte[] content = message.getBytes();

			fos.write(content);
			fos.flush();
			fos.close();

			System.out.println("대화 내용 저장 됨");
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		dispose();
		System.out.println(cDTO.getC_id() + " 채팅방 닫힘");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
