package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import client.MainClass;
import client.daos.DAO;
import client.dtos.IdSearchDTO;
import client.dtos.JoinDTO;
import client.dtos.LoginDTO;
import client.dtos.MessageDTO;
import client.dtos.PwSearchDTO;
import client.ui.FriendUI;

class PerClinetThread extends Thread {
	public static Map<String, BufferedOutputStream> session1 = new ConcurrentHashMap<String, BufferedOutputStream>();
	public static Map<String, ObjectOutputStream> session2 = new ConcurrentHashMap<String, ObjectOutputStream>();
	private Socket socket;
	private ObjectOutputStream oos;
	private String id;
	private MessageDTO msgDTO;
	private int type;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	private ObjectInputStream ois;
	private DAO dao;
	private DataOutputStream dos;
	private FileInputStream fin;

	public PerClinetThread(Socket socket) {
		this.socket = socket;
		try {
			bis = new BufferedInputStream(socket.getInputStream());
			ois = new ObjectInputStream(bis);
			bos = new BufferedOutputStream(socket.getOutputStream());
			oos = new ObjectOutputStream(bos);
			dao = new DAO();
		} catch (Exception e) {
			System.out.println("서버 : " + e.getMessage() + "\t생성자 부분");
		}
	}

	public void run() {
		try {
			while (true) {
				type = bis.read();
				// 1=로그인, 2=가입, 3=메시지, 4=id찾기, 6=pw찾기, 7=패스워드 수정
				if (type == 1) {
					LoginDTO lDTO = (LoginDTO) ois.readObject();
					if (lDTO == null) {
						break;
					}
					login(lDTO);
				} else if (type == 2) {
					JoinDTO jDTO = (JoinDTO) ois.readObject();
					if (jDTO == null) {
						break;
					}
					join(jDTO);
				} else if (type == 3) {
					msgDTO = (MessageDTO) ois.readObject();
					if (msgDTO == null) {
						break;
					}
					send(msgDTO);
				}else if (type == 4) {
					IdSearchDTO isDTO = (IdSearchDTO) ois.readObject();
					if (isDTO == null) {
						break;
					}
					idSearch(isDTO);
				}else if (type == 6) {
					PwSearchDTO pwDTO = (PwSearchDTO) ois.readObject();
					if (pwDTO == null) {
						break;
					}
					pwSearch(pwDTO);
				}else if (type == 7) {
					PwSearchDTO pwDTO = (PwSearchDTO) ois.readObject();
					if (pwDTO == null) {
						break;
					}
					dao.updatePw(pwDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("서버 : " + e.getMessage() + "\t스레드 run 부분");
		} finally {
			System.out.println("서버 : " + id + "님이 로그아웃 하셨습니다.");
			if(id != null) {
				session1.remove(id);
				session2.remove(id);
			}
			try {
				socket.close();
			} catch (Exception ignored) {
			}
		}
	}

	// 메시지 전송
	private void send(MessageDTO msgDTO) {
		for (String key : session1.keySet()) {
			try {
				if (msgDTO.getToId().equals(key) || msgDTO.getFromId().equals(key)) {
					System.out.println("서버 : " + "보낸사람>>" + msgDTO.getFromId() + "\t받는사람>>" + msgDTO.getToId());
					session1.get(key).write(3);
					session1.get(key).flush();
					session2.get(key).writeObject(msgDTO);
					session2.get(key).flush();
					session2.get(key).reset();
				}
			} catch (IOException e) {
				System.out.println("서버 : " + e.getMessage() + "\tsend 부분");
			}
		}
		if (!session1.containsKey(msgDTO.getToId())) {
			File file = new File("src\\server\\chat\\" + msgDTO.getToId() + ";" + msgDTO.getFromId() + ";"
					+ msgDTO.getC_id() + ".txt");
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
	}

	private String msgString(MessageDTO msgDTO) {
		String name = dao.selectProfile(msgDTO.getFromId()).getP_name();
		
		String all = "";
		try {
			// 파일 객체 생성
			File file = new File("src\\server\\chat\\" + msgDTO.getToId() + ";" + msgDTO.getFromId() + ";"
					+ msgDTO.getC_id() + ".txt");
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

	// 로그인
	private void login(LoginDTO lDTO) {
		if (session1.isEmpty()) {
			lDTO = dao.selectLogin(lDTO);
			try {
				bos.write(1);
				bos.flush();
				oos.writeObject(lDTO);
				oos.flush();
				oos.reset();
				if (lDTO != null)
					file(lDTO.getId());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (lDTO != null) {
				id = lDTO.getId();
				session1.put(id, bos);
				session2.put(id, oos);
				System.out.println("서버 : " + id + "님이 로그인 하셨습니다.");
			}
		} else {
			for (String key : session1.keySet()) {
				if (key.equals(lDTO.getId())) {
					try {
						lDTO.setId("로그인 됨");
						bos.write(1);
						bos.flush();
						oos.writeObject(lDTO);
						oos.flush();
						oos.reset();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					lDTO = dao.selectLogin(lDTO);
					try {
						bos.write(1);
						bos.flush();
						oos.writeObject(lDTO);
						oos.flush();
						oos.reset();
						file(lDTO.getId());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (lDTO != null) {
						id = lDTO.getId();
						session1.put(id, bos);
						session2.put(id, oos);
						System.out.println("서버 : " + id + "님이 로그인 하셨습니다.");
					}
				}
			}
		}
	}

	private void file(String id) {
		try {
			BufferedOutputStream bos2 = new BufferedOutputStream(socket.getOutputStream());
			dos = new DataOutputStream(bos2);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("서버 : 파일 전송 시작");
		File path = new File("src\\server\\chat\\");
		final String fatternName = id + ";";

		String fileList[] = path.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(fatternName);
			}

		});

		byte[] buf = new byte[1024];
		int read = 0;
		if (fileList.length > 0) {
			try {
				// 파일 있다고 전송
				dos.writeBoolean(true);
				dos.flush();

				// 파일 개수 전송
				dos.writeInt(fileList.length);
				dos.flush();

				for (int i = 0; i < fileList.length; i++) {
					// 파일 이름 전송
					dos.writeUTF(fileList[i].split(";")[2]);
					dos.flush();
				}

				for (int i = 0; i < fileList.length; i++) {
					// 보낸사람
					dos.writeUTF(fileList[i].split(";")[1]);
					dos.flush();
					File file = new File("src\\server\\chat\\" + fileList[i]);
					dos.writeLong(file.length());
					fin = new FileInputStream("src\\server\\chat\\" + fileList[i]);
					while ((read = fin.read(buf)) != -1) {
						dos.write(buf, 0, read);
						dos.flush();
					}
					System.out.println("서버 : " + fileList[i].split(";")[2] + " 파일 전송완료");
					fin.close();
					file.delete();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				dos.writeBoolean(false);
				dos.flush();
				System.out.println("서버 : 보낼 파일 없음");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 가입
	private void join(JoinDTO jDTO) {
		try {
			dao.insertMember(jDTO);
			bos.write(2);
			bos.flush();
			bos.write(21);
			bos.flush();
		} catch (Exception e) {
			try {
				bos.write(2);
				bos.flush();
				bos.write(22);
				bos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private void idSearch(IdSearchDTO isDTO) {
		try {
			BufferedOutputStream bos3 = new BufferedOutputStream(socket.getOutputStream());
			dos = new DataOutputStream(bos3);
			bos.write(4);
			bos.flush();
			String id = dao.selectIdSearch(isDTO);
			if(id != null)
				dos.writeUTF(id);
			else
				dos.writeUTF("");
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void pwSearch(PwSearchDTO pwDTO) {
		try {
			BufferedOutputStream bos4 = new BufferedOutputStream(socket.getOutputStream());
			dos = new DataOutputStream(bos4);
			bos.write(5);
			bos.flush();
			if(dao.selectPwSearch(pwDTO) == null) {
				dos.writeBoolean(false);
			}else {
				dos.writeBoolean(true);
				dos.flush();
				dos.writeUTF(pwDTO.getId());
			}
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
