package client.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GtTalkUI extends JFrame{
	private FriendUI fUI;
	private ChatListUI cUI;
	private JPanel panMenu;
	private JPanel panMain;
	private JButton btnFriendList;
	private JButton btnChatList;
	private CardLayout cl;

	public GtTalkUI() {
		initialize();
		setVisible(true);
	}

	private void initialize() {
		setTitle("GtTalk");
		getContentPane().setBackground(Color.WHITE);
		setBounds(100, 100, 547, 542);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		panMenu = new JPanel();
		getContentPane().add(panMenu, BorderLayout.WEST);
		panMenu.setLayout(new BoxLayout(panMenu, BoxLayout.Y_AXIS));
		
		btnFriendList = new JButton("친구");
		panMenu.add(btnFriendList);
		btnFriendList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(panMain, "fUI");
			}
		});
		
		
		btnChatList = new JButton("채팅");
		panMenu.add(btnChatList);
		btnChatList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(panMain, "cUI");
			}
		});
		
		// 친구 목록UI
		fUI = new FriendUI();
		cUI = new ChatListUI();
		
		panMain = new JPanel();
		getContentPane().add(panMain, BorderLayout.CENTER);
		cl = new CardLayout();
		panMain.setLayout(cl);
		
		panMain.add(fUI, "fUI");
		panMain.add(cUI, "cUI");
		
	}
}
