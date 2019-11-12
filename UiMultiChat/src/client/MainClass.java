package client;

import java.net.Socket;

import javax.swing.JOptionPane;

import client.dtos.LoginDTO;
import client.ui.GtTalkUI;
import client.ui.LoginUI;
import client.ui.MainThread;

public class MainClass {
	public static String myId;
	public static String myName;
	public static Socket socket;
	public static LoginUI lUI;
	
	public static void main(String[] args) {
		try {
			socket = new Socket("127.0.0.1", 9002);
			Thread thread = new MainThread(socket);
			thread.start();
			lUI = new LoginUI();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "서버와 연결이 되어 있지 않습니다.", "GtTalk", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static boolean loginResult(LoginDTO lDTO) {
		if(lDTO == null) {
			JOptionPane.showMessageDialog(null, "아이디와 패스워드를 확인해 주세요.", "로그인", JOptionPane.ERROR_MESSAGE);
			return false;
		}else {
			if(lDTO.getId().equals("로그인 됨")) {
				JOptionPane.showMessageDialog(null, "이미 로그인 되어있습니다.", "로그인", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else {
				MainClass.myId = lDTO.getId();
				lUI.dispose();
				lUI = null;
				System.out.println("로그인 성공");
				new GtTalkUI();
				return true;
			}
		}
	}
}
