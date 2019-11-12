package client.ui;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import client.MainClass;
import client.dtos.LoginDTO;
import client.dtos.MessageDTO;

public class MainThread extends Thread {
	private Socket socket;
	private ObjectInputStream ois;
	public static ObjectOutputStream oos;
	private MessageDTO msgDTO;
	private PipedOutputStream pos;
	public static PipedInputStream pis;
	private int type;
	private BufferedInputStream bis;
	private BufferedInputStream bis2;
	private DataInputStream dis;

	public static Map<String, MessageDTO> receiver = Collections.synchronizedMap(new HashMap<String, MessageDTO>());

	public MainThread(Socket socket) {
		this.socket = socket;
		this.pis = new PipedInputStream();
	}

	public void run() {
		try {
			bis = new BufferedInputStream(socket.getInputStream());
			ois = new ObjectInputStream(bis);
			oos = new ObjectOutputStream(socket.getOutputStream());
			pos = new PipedOutputStream(pis);
			dis = new DataInputStream(bis);

			while (true) {
				type = bis.read();
				// 1=로그인, 2=가입, 3=메시지, 4=id찾기, 6=pw찾기, 7=패스워드 수정
				if (type == 1) {
					login();
				} else if (type == 2) {
					int joinResult = bis.read();
					if (joinResult == 21)
						JOptionPane.showMessageDialog(null, "회원 가입이 완료 되었습니다.");
					else
						JOptionPane.showMessageDialog(null, "이미 존재하는 아이디 입니다.");
				} else if (type == 3) {
					msgDTO = (MessageDTO) ois.readObject();
					if (msgDTO == null)
						break;

					System.out.println("받은사람 : " + MainClass.myId + "\t보낸사람 : " + msgDTO.getFromId() + "\t내용 : "
							+ msgDTO.getContent());
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos2 = new ObjectOutputStream(baos);
					
					byte[] serializedMember;
					oos2.writeObject(msgDTO);
					serializedMember = baos.toByteArray();
					pos.write(serializedMember);
					pos.flush();
					oos2.reset();
				} else if (type == 4) {
					String id = dis.readUTF();
					if (!id.equals(""))
						JOptionPane.showMessageDialog(null, "고객님의 아이디는 " + id + "입니다.");
					else
						JOptionPane.showMessageDialog(null, "입력하신 정보를 다시 한번 확인해 주세요.");
				} else if (type == 5) {
					if (dis.readBoolean())
						new RePwUI(dis.readUTF());
					else
						JOptionPane.showMessageDialog(null, "입력하신 정보를 다시 한번 확인해 주세요.");
				}
			}

		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private void login() {
		try {
			LoginDTO lDTO;
			lDTO = (LoginDTO) ois.readObject();
			boolean login = MainClass.loginResult(lDTO);
			
			if(login) {
				boolean fileIn = dis.readBoolean();
				System.out.println(fileIn);

				if (fileIn) {
					byte[] buf = new byte[1024];
					int read = 0;
					int length = dis.readInt();
					System.out.println(length);

					String fileName[] = new String[length];
					String fileFrom[] = new String[length];

					for (int i = 0; i < length; i++) {
						fileName[i] = dis.readUTF();
					}

					for (int i = 0; i < length; i++) {
						fileFrom[i] = dis.readUTF();
						long fileSize = dis.readLong();
						FileOutputStream fos = new FileOutputStream("src\\client\\chat\\" + fileName[i], true);
						System.out.println(fileName[i]);
						while ((fileSize > 0) && ((read = dis.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1)) {
							fos.write(buf, 0, read);
							fileSize -= read;
						}
						fos.close();
						
						msgDTO = new MessageDTO();
						msgDTO.setC_id(fileName[i].split("\\.")[0]);
						msgDTO.setFromId(fileFrom[i]);
						System.out.println(fileFrom[i]);
						
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ObjectOutputStream oos2 = new ObjectOutputStream(baos);
						
						byte[] serializedMember;
						oos2.writeObject(msgDTO);
						serializedMember = baos.toByteArray();
						pos.write(serializedMember);
						pos.flush();
						oos2.reset();
					}
					System.out.println("파일 수신 및 저장 성공");
				} else {
					System.out.println("로그아웃 중 온 메시지 없음");
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}