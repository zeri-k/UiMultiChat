package client.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.dtos.ChatRoomDTO;
import client.ui.chatting.ChatRoomUI;

public class ChatPanel extends JPanel{
	public ChatRoomDTO cDTO;
	private JPanel panChatList;
	private JPanel panParents;
	private JLabel lblPic;
	private JPanel panChildren;
	private JLabel lblName;
	public JLabel lblMessage;
	private String message;
	
	public ChatPanel(JPanel panChatList, ChatRoomDTO cDTO) {
		this.cDTO = cDTO;
		this.panChatList = panChatList;
		initialize();
	}
	private void initialize() {
		panParents = new JPanel();
		panChatList.add(panParents);
		panParents.setPreferredSize(new Dimension(460, 60));
		panParents.setLayout(new BorderLayout(0, 0));
		
		// 채팅방 사진(대화 상대방 프로필 이미지)
		lblPic = new JLabel();
		panParents.add(lblPic, BorderLayout.WEST);
		
		Image ori_size = new ImageIcon(cDTO.getImg_dup_name()).getImage();
		Image dup_size = ori_size.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
		ImageIcon profile_ico = new ImageIcon(dup_size);
		
		lblPic.setIcon(profile_ico);
		
		// 채팅방 이름과 마지막 메세지가 들어가는 패널
		panChildren = new JPanel();
		
		// 패널 클릭시 채팅방 오픈
		panChildren.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new ChatRoomUI(cDTO.getC_name(), cDTO);
			}
		});
		panParents.add(panChildren, BorderLayout.CENTER);
		panChildren.setLayout(new BorderLayout(0, 0));
		
		// 채팅방 이름
		lblName = new JLabel(cDTO.getC_name());
		panChildren.add(lblName, BorderLayout.WEST);
		
		// 채팅방의 마지막 메세지
		lblMessage = new JLabel(message);
		panChildren.add(lblMessage, BorderLayout.SOUTH);
	}
}
