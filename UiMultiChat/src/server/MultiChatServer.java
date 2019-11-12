package server;

import java.net.ServerSocket;
import java.net.Socket;

import client.daos.DAO;

public class MultiChatServer {
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		DAO dao = new DAO();
		
		// 오라클db에서 누가 누구한테 보냈는지 dao로 가져온다
		
		try {
			serverSocket = new ServerSocket(9002);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread thread = new PerClinetThread(socket);
				thread.start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}