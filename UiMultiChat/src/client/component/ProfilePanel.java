package client.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.dtos.ProfileDTO;
import client.ui.chatting.ChatRoomUI;

public class ProfilePanel extends JPanel{
	private ProfileDTO pDTO;
	private JPanel panFriendList;
	private JPanel panParents;
	private JLabel lblPic;
	private JPanel panChildren;
	private JLabel lblName;
	private JLabel lblStateMessage;
	
	public ProfilePanel(JPanel panFriendList, ProfileDTO pDTO) {
		this.pDTO = pDTO;
		this.panFriendList = panFriendList;
		initialize();
	}
	private void initialize() {
		panParents = new JPanel();
		panFriendList.add(panParents);
		panParents.setPreferredSize(new Dimension(460, 60));
		panParents.setLayout(new BorderLayout(0, 0));
		
		lblPic = new JLabel();
		panParents.add(lblPic, BorderLayout.WEST);
		
		Image ori_size = new ImageIcon(pDTO.getImg_dup_name()).getImage();
		Image dup_size = ori_size.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
		ImageIcon profile_ico = new ImageIcon(dup_size);
		
		lblPic.setIcon(profile_ico);
		
		panChildren = new JPanel();
		panParents.add(panChildren, BorderLayout.CENTER);
		panChildren.setLayout(new BorderLayout(0, 0));
		
		lblName = new JLabel(pDTO.getP_name());
		panChildren.add(lblName, BorderLayout.WEST);
		
		lblStateMessage = new JLabel(pDTO.getP_message());
		panChildren.add(lblStateMessage, BorderLayout.SOUTH);
	}
}
