package client.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import client.MainClass;
import client.component.ChatPanel;
import client.daos.DAO;
import client.dtos.ChatRoomDTO;
import client.dtos.MessageDTO;

public class ChatListThread extends Thread {
	private List<ChatPanel> chatListPanel;
	private JPanel panChatList;
	private List<ChatRoomDTO> chatList;
	private boolean chatIsNull;
	private MessageDTO msgDTO;
	private ObjectInputStream ois;
	private PipedInputStream inMessage;
	public static Map<String, MessageDTO> messageMap = new ConcurrentHashMap<String, MessageDTO>();
	private String name;

	public ChatListThread(List<ChatPanel> chatListPanel, JPanel panChatList, List<ChatRoomDTO> chatList) {
		this.chatListPanel = chatListPanel;
		this.panChatList = panChatList;
		this.chatList = ChatListUI.chatList;
		this.inMessage = MainThread.pis;
	}

	public void run() {
		try {
			
			while (true) {
				ois = new ObjectInputStream(inMessage);
				// 새로온 메시지의 기존 채팅방이 없다면 채팅방 목록에 추가
				msgDTO = (MessageDTO) ois.readObject();
				chatIsNull = true;
				
				if(msgDTO == null) {
					System.out.println("ChatListThread : msgDTO is null");
					break;
				}
				
				this.chatList = ChatListUI.chatList;
				this.chatListPanel = ChatListUI.chatListPanel;
				if (msgDTO.getC_id() != null) {
					// 기존에 열려있는 채팅방 있는지 확인
					for (int i = 0; i < chatList.size(); i++) {
						if (msgDTO.getC_id().equals(chatList.get(i).getC_id())) {
							chatIsNull = false;
							break;
						}
					}
					
					// 기존에 열려있는 채팅방이 없으면 추가
					if (chatIsNull) {
						addChat(msgDTO);
					}
					
					if(msgDTO.getContent() != null) {
						// 채팅방에 마지막 메시지 추가
						for (int i = 0; i < chatListPanel.size(); i++) {
							if (msgDTO.getC_id().equals(chatListPanel.get(i).cDTO.getC_id())) {
								updatelastMessage(i);
								break;
							}
						}
					}
					
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\tChatListThread 예외 발생");
		}finally {
			System.out.println("채팅방리스트 스레드 종료");
		}
		
	}

	public void addChat(MessageDTO msgDTO) {
		ChatRoomDTO crDTO = new ChatRoomDTO();
		crDTO.setC_id(msgDTO.getC_id());
		crDTO.setId(msgDTO.getFromId());
		for(int i = 0; i<FriendUI.friendList.size(); i++) {
			if(FriendUI.friendList.get(i).getId().equals(msgDTO.getFromId())) {
				crDTO.setC_name(FriendUI.friendList.get(i).getP_name());
				crDTO.setImg_dup_name(FriendUI.friendList.get(i).getImg_dup_name());
			}
		}
		

		chatListPanel.add(new ChatPanel(panChatList, crDTO));
		ChatListUI.chatList.add(crDTO);
		System.out.println("스레드에서 채팅방 추가 됨 : " + crDTO.getC_id());
		DAO dao = new DAO();
		//dao.insertChat(crDTO);	// 임베디드 디비
		chatIsNull = true;
	}
	
	public synchronized void updatelastMessage(int i) {
		chatListPanel.get(i).lblMessage.setText(msgDTO.getContent());
		System.out.println(chatListPanel.get(i).cDTO.getC_id() + " 채팅방 메세지 업데이트 : " + msgDTO.getContent());
		messageMap.put(msgDTO.getC_id(), msgDTO);
		
		
		
		File file = new File("src\\client\\chat\\" + msgDTO.getC_id() + ".txt");
		
		FileOutputStream fos = null;
		String message = msgString(msgDTO);
		
		try {
			fos = new FileOutputStream(file);
			byte[] content = message.getBytes();

			fos.write(content);
			fos.flush();
			fos.close();

			System.out.println("DONE");
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
		
	}
	
	private String msgString(MessageDTO msgDTO) {
		if(msgDTO.getFromId().equals(MainClass.myId))
			name = MainClass.myName;
		else {
			for(int i = 0; i < FriendUI.friendList.size(); i++) {
				if(FriendUI.friendList.get(i).getId().equals(msgDTO.getFromId()))
					name = FriendUI.friendList.get(i).getP_name();
			}
		}
		
		String all = "";
		try {
			// 파일 객체 생성
			File file = new File("src\\client\\chat\\" + msgDTO.getC_id() + ".txt");
			// 입력 스트림 생성
			FileReader filereader;
			if(file.exists()) {
				filereader = new FileReader(file);

				StringBuffer buffer = new StringBuffer();
				// 입력 버퍼 생성
				BufferedReader reader = new BufferedReader(filereader);
				String inputLine = "";
				while ((inputLine = reader.readLine()) != null) {
					buffer.append(inputLine + "\n");
				}
				reader.close();
				all = buffer.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String content;
		// 날자 출력
		if (!all.contains(msgDTO.getDay()))
			content = msgDTO.getDay() + all + "\n";
		else
			content = all;
		if(content.length() != 11)
			if(content.substring(content.length()-6, content.length()-1).equals(msgDTO.getTime()))
				content = content.substring(0, content.length()-6);
			else
				content = content + "\n";
		
		if (msgDTO.getContent().length() < 50) {
			all = content + "[" + name + "] " + msgDTO.getContent() + "\n" + msgDTO.getTime() + "\n";
		} else {
			String[] change_Line = msgDTO.getContent().split("\\n");
			StringBuffer str = new StringBuffer();
			int a, b;
			for (int i = 0; i < change_Line.length; i++) {
				if (change_Line[i].length() > 50) {
					a = change_Line[i].length() / 50;
					b = change_Line[i].length() % 50;
					for (int j = 0; j <= a; j++) {
						if (j < a) {
							if (j < a - 1)
								str.append(change_Line[i].substring(j * 50, (j * 50) + 50) + "\n");
							else
								str.append(change_Line[i].substring(j * 50, (j * 50) + 50) + "\n");
						} else if (j == a && b != 0)
							str.append(change_Line[i].substring(j * 50, (j * 50) + b) + "\n");
					}
				} else if (change_Line.length <= 1)
					str.append(change_Line[i]);
				else
					str.append(change_Line[i] + "\n");
			}
			if (msgDTO.getContent().length() == 50)
				all = content + "[" + name + "] " + str + "\n" + msgDTO.getTime() + "\n";
			else
				all = content + "[" + name + "] " + str + msgDTO.getTime() + "\n";
		}
		return all;
	}
}
