package client.ui.chatting;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import client.MainClass;
import client.dtos.ChatRoomDTO;
import client.dtos.MessageDTO;

@SuppressWarnings("serial")
public class ChatTextInputArea extends JTextArea {
	private MessageDTO msgDTO;
	private ChatRoomDTO cDTO;
	private ObjectOutputStream oos;
	private BufferedOutputStream bos;
	

	public void bindSocket(ChatRoomDTO cDTO) throws IOException {
		this.cDTO = cDTO;
		msgDTO = new MessageDTO();
		msgDTO.setFromId(MainClass.myId);
		bos = new BufferedOutputStream(MainClass.socket.getOutputStream());
		oos = new ObjectOutputStream(bos);
		
		setLineWrap(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(e.isControlDown()) {
						append("\n");
					}else {
						submit();
						e.consume();
					}
				}
				
			}
		});

	}
	
	public void submit() {
		msgDTO.setFromId(MainClass.myId);
		msgDTO.setToId(cDTO.getId());
		msgDTO.setC_id(cDTO.getC_id());
		
		long daytime = System.currentTimeMillis(); 
		SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat Time = new SimpleDateFormat("hh:mm");
		String strDay = day.format(new Date(daytime));
		String strTime = Time.format(new Date(daytime));
		
		msgDTO.setDay(strDay);
		msgDTO.setTime(strTime);
		
		msgDTO.setContent(getText());
		
		try {
			if(!(msgDTO.getContent().equals("") || msgDTO.getContent().equals("\n"))) {
				bos.write(3);
				bos.flush();
				oos.writeObject(msgDTO);
	            oos.flush();
	            oos.reset();
			}
		}catch (IOException e1) {
			e1.printStackTrace();
		}
		
		setText(null);
		requestFocus();
	}

}
