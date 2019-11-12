package client.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.MainClass;
import client.component.ChatPanel;
import client.daos.DAO;
import client.dtos.ChatRoomDTO;
import client.dtos.ProfileDTO;

public class ChatListUI extends JPanel{
	private JTextField tfSearch;
	private DAO dao;
	private JPanel panChatList;
	public volatile static List<ChatPanel> chatListPanel;
	private JPanel panTitle;
	private JLabel lblNewLabel;
	private JButton btnChatAdd;
	private ProfileDTO pfDTO;
	private List<ProfileDTO> friendList;
	public volatile static List<ChatRoomDTO> chatList;

	// 채팅방 목록
	public ChatListUI() {
		this.dao = new DAO();
		this.setLayout(new BorderLayout(0, 0));
		initialize();
	}

	private void initialize() {
		panTitle = new JPanel();
		add(panTitle, BorderLayout.NORTH);
		panTitle.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("채팅");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));
		panTitle.add(lblNewLabel, BorderLayout.WEST);
		
		// 검색 필드
		tfSearch = new JTextField();
		panTitle.add(tfSearch, BorderLayout.SOUTH);
		tfSearch.setText("채팅방 이름 검색");
		tfSearch.setColumns(10);
		
		// 채팅방 추가 버튼
		btnChatAdd = new JButton("새로운 채팅");
		
		// 채팅방 추가 버튼 리스너
		btnChatAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 입력 받은 친구 이름 
				String resultStr = null;
				resultStr = JOptionPane.showInputDialog(null,"친구 이름을 입력해 주세요","친구 찾기", 
						JOptionPane.QUESTION_MESSAGE);
				
				// 내가 추가한 친구들 중에 검색한 사람이 있으면 그사람의 프로필을 ProfileDTO에 저장
				for(int i = 0; i < friendList.size(); i++) {
					if(friendList.get(i).getP_name().equals(resultStr)) {
						pfDTO = friendList.get(i);
					}
				}
				
				// 친구 목록에 내가 검색한 사람이 없을 때
				if(pfDTO == null) {
					JOptionPane.showMessageDialog(null, "존재하지 않는 친구 입니다.", "친구 찾기", 
							JOptionPane.ERROR_MESSAGE);
				}else {
					// 친구 목록에 내가 검색한 사람이 있을 떄
					int result = JOptionPane.showConfirmDialog(null, resultStr + "님과 채팅방을 여시겠습니까?", "친구 찾기", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if(result == JOptionPane.CLOSED_OPTION) {
						// 옵션 창 닫았을때
					} else if(result == JOptionPane.YES_OPTION) {
						// 옵션 창 예 선택
						
						// 채팅방 고유 아이디 생성(만든시간+만든사람)
						long time = System.currentTimeMillis(); 
						SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddhhmmss");
						String str = dayTime.format(new Date(time));
						str = str + MainClass.myId;
						
						// 채팅방 생성을 위해 전용 DTO에 저장
						ChatRoomDTO crDTO = new ChatRoomDTO();
						crDTO.setC_id(str);		// 채팅방 고유 번호
						crDTO.setId(pfDTO.getId());		// 상대방 id
						crDTO.setImg_dup_name(pfDTO.getImg_dup_name());	// 상대방 프로필 사진
						crDTO.setC_name(pfDTO.getP_name());		// 채팅방 이름
						
						// 채팅방DTO를 mybatis에 전달해서 insert
						//dao.insertChat(crDTO);	// 임베디드 디비
						
						// 채팅방 목록에 방금 만든 채팅방 추가
						addChat(crDTO);
					} else {
						// 옵션 창 취소 선택
					}
				}
			}
		});
		panTitle.add(btnChatAdd, BorderLayout.EAST);
		
		// 초기에 채팅방 리스트 뿌리기
		panChatList = new JPanel();
		add(panChatList, BorderLayout.CENTER);
		
		// DB에서 채팅방 리스트 가져오기
		chatList = dao.selectChatList();
		
		// 친구목록 불러와서 언제든 사용할수 있게 준비
		friendList = FriendUI.friendList;

		// 친구목록에서 가져온 친구 프로필을 채팅방 목록 DB에서 불러온 리스트와 매핑
		for(int i = 0; i < chatList.size(); i++) {
			for(int j = 0; j < friendList.size(); j++) {
				if(chatList.get(i).getId().equals(friendList.get(j).getId())) {
					chatList.get(i).setImg_dup_name(friendList.get(j).getImg_dup_name());
				}
			}
		}
		
		//체팅방 패널을 리스트형 객체로 만든다
		chatListPanel = new ArrayList<ChatPanel>();
		
		// 리스트 사이즈 만큼 리스트에 패널 추가
		if(chatList.size() >= 1){
			for(int i = 0; i < chatList.size(); i++) {
				chatListPanel.add(new ChatPanel(panChatList, chatList.get(i)));
			}
		}
		
		// 채팅방 리스트 갱신하는 스레드
		try {
			Thread thread = new ChatListThread(chatListPanel, panChatList, chatList);
			thread.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// 새롭게 추가된 채팅방 패널에 추가
	public void addChat(ChatRoomDTO crDTO) {
		chatListPanel.add(new ChatPanel(panChatList, crDTO));
		chatList.add(crDTO);
	}
}
