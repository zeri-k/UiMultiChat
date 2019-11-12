package client.ui.chatting;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.MainClass;
import client.dtos.MessageDTO;
import client.ui.ChatListThread;
import client.ui.FriendUI;

public class ChatRoomThread extends Thread {
	private JTextArea chatContent;
	private JScrollPane scrollPane;
	private String C_id;
	private MessageDTO msgDTO;
	public static Map<String, MessageDTO> message;
	private String content;
	private Queue<String> queue;
	private String name;

	public ChatRoomThread(JTextArea chatContent, JScrollPane scrollPane, String C_id) {
		this.chatContent = chatContent;
		this.scrollPane = scrollPane;
		this.C_id = C_id;
		this.queue = new LinkedList<>();
	}

	public void run() {
		try {
			queue.add(null);
			while (true) {
				test();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\tChatRoomThread 예외 발생");
		}
	}
	
	public synchronized void test() {
		message = ChatListThread.messageMap;
		if (!message.keySet().isEmpty()) {
			for (String key : message.keySet()) {
				if (key.equals(C_id)) {
					msgDTO = message.get(key);
					message.remove(key);
				}
			}
		}

		if (msgDTO != null) {
			if(msgDTO.getFromId().equals(MainClass.myId))
				name = MainClass.myName;
			else {
				for(int i = 0; i < FriendUI.friendList.size(); i++) {
					if(FriendUI.friendList.get(i).getId().equals(msgDTO.getFromId()))
						name = FriendUI.friendList.get(i).getP_name();
				}
			}
			queue.add(msgDTO.getFromId());
			System.out.println(queue);
			
			// 날자 출력
			if (!chatContent.getText().contains(msgDTO.getDay()))
				content = msgDTO.getDay() + chatContent.getText() + "\n";
			else
				content = chatContent.getText();
			
			if(queue.peek() != null) {
				// 내용, 시간 출력
				if (!queue.poll().equals(queue.peek()))
					content = content + "\n";
				else
					if(content.substring(content.length()-6, content.length()-1).equals(msgDTO.getTime()))
						content = content.substring(0, content.length()-6);
					else
						content = content + "\n";
			}else
				queue.poll();
			if(msgDTO.getContent().length() < 50) {
				chatContent.setText(content + "[" + name + "] " + msgDTO.getContent() + "\n"
						+ msgDTO.getTime() + "\n");
			}else {
				String[] change_Line = msgDTO.getContent().split("\\n");
				StringBuffer str = new StringBuffer();
				int a,b;
				for(int i = 0; i < change_Line.length; i++) {
					if(change_Line[i].length() > 50) {
						a = change_Line[i].length() / 50;
						b = change_Line[i].length() % 50;
						for(int j = 0; j <= a; j++) {
							if(j < a) {
								if(j < a - 1)
									str.append(change_Line[i].substring(j * 50, (j * 50) + 50) + "\n");
								else
									str.append(change_Line[i].substring(j * 50, (j * 50) + 50) + "\n");
							}
							else if(j == a && b != 0)
								str.append(change_Line[i].substring(j * 50, (j * 50) + b) + "\n");
						}
					}else
						if(change_Line.length <= 1)
							str.append(change_Line[i]);
						else
							str.append(change_Line[i] + "\n");
				}
				if(msgDTO.getContent().length() == 50)
					chatContent.setText(content + "[" + name + "] " + str + "\n"
							+ msgDTO.getTime() + "\n");
				else
					chatContent.setText(content + "[" + name + "] " + str
							+ msgDTO.getTime() + "\n");
			}
			
			System.out.println(queue);
			
			// 스크롤바 제일 아래로 정렬
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			msgDTO = null;
		}
	}
}