package client.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.MainClass;
import client.component.ProfilePanel;
import client.daos.DAO;
import client.dtos.IdDTO;
import client.dtos.ProfileDTO;

public class FriendUI extends JPanel{
	private JTextField tfSearch;
	private DAO dao;
	private ProfileDTO pDTO;
	private JPanel panFriendList;
	private List<ProfilePanel> friendListPanel;
	public static List<ProfileDTO> friendList;
	private JPanel panTitle;
	private JLabel lblNewLabel;
	private JButton btnFriendAdd;

	public FriendUI() {
		dao = new DAO();
		this.pDTO = dao.selectProfile(MainClass.myId);
		MainClass.myName = pDTO.getP_name();
		this.setLayout(new BorderLayout(0, 0));
		initialize();
	}

	private void initialize() {
		panTitle = new JPanel();
		add(panTitle, BorderLayout.NORTH);
		panTitle.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("친구");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));
		panTitle.add(lblNewLabel, BorderLayout.WEST);
		
		tfSearch = new JTextField();
		panTitle.add(tfSearch, BorderLayout.SOUTH);
		tfSearch.setText("이름검색");
		tfSearch.setColumns(10);
		
		btnFriendAdd = new JButton("친구추가");
		btnFriendAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfileDTO pfDTO;
				String resultStr = null;
				resultStr = JOptionPane.showInputDialog(null,"친구 ID를 입력해 주세요","친구 찾기", 
						JOptionPane.QUESTION_MESSAGE);
				pfDTO = dao.selectProfile(resultStr);
				if(pfDTO == null) {
					JOptionPane.showMessageDialog(null, "존재하지 않는 ID입니다.", "친구 찾기", 
							JOptionPane.ERROR_MESSAGE);
				}else {
					int result = JOptionPane.showConfirmDialog(null, resultStr + "님을 친구로 추가하시겠습니까?", "친구 찾기", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(result == JOptionPane.CLOSED_OPTION) {
						
					} else if(result == JOptionPane.YES_OPTION) {
						IdDTO idDTO = new IdDTO();
						idDTO.setMyId(MainClass.myId);
						idDTO.setfId(resultStr);
						dao.insertFriend(idDTO);
						addFriend(pfDTO);
					} else {
						
					}
				}
			}
		});
		panTitle.add(btnFriendAdd, BorderLayout.EAST);
		
		panFriendList = new JPanel();
		add(panFriendList, BorderLayout.CENTER);
		
		// 프로필
		friendList = (ArrayList<ProfileDTO>)dao.selectFriendList(MainClass.myId);
		friendListPanel = new ArrayList<ProfilePanel>();
		friendListPanel.add(new ProfilePanel(panFriendList, pDTO));
		if(friendList.size() >= 1) {
			for(int i = 0; i<friendList.size(); i++) {
				friendListPanel.add(new ProfilePanel(panFriendList, friendList.get(i)));
			}
		}else {
			
		}
		
		
	}
	
	public void addFriend(ProfileDTO pfDTO) {
		friendListPanel.add(new ProfilePanel(panFriendList, pfDTO));
	}
}
