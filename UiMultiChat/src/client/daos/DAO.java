package client.daos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import client.dtos.ChatRoomDTO;
import client.dtos.IdDTO;
import client.dtos.IdSearchDTO;
import client.dtos.JoinDTO;
import client.dtos.LoginDTO;
import client.dtos.ProfileDTO;
import client.dtos.PwSearchDTO;

public class DAO {
	private SqlSession oracle;
	private SqlSession sqlite;
	
	public DAO() {
		oracle = SqlSessionFactoryBean.getSqlsessionInstanceOracle();
		sqlite = SqlSessionFactoryBean.getSqlsessionInstanceSqlite();
		
	}
	
	//=====================================================================
	
	public void insertMember(JoinDTO vo) {
		oracle.insert("member.insertMember", vo);
		oracle.insert("member.insertProfile", vo);
		oracle.commit();
		System.out.println("가입 성공");
	}
	
	public LoginDTO selectLogin(LoginDTO vo) {
		return oracle.selectOne("member.selectLogin", vo);
	}
	
	public ProfileDTO selectProfile(String id) {
		System.out.println("selectProfile\t" + id);
		String value = id;
		return oracle.selectOne("member.selectProfile", value);
	}
	
	public String selectIdSearch(IdSearchDTO isDTO) {
		System.out.println("selectIdSearch\t");
		return oracle.selectOne("member.selectIdSearch", isDTO);
	}
	
	public String selectPwSearch(PwSearchDTO pwDTO) {
		System.out.println("selectPwSearch\t");
		return oracle.selectOne("member.selectPwSearch", pwDTO);
	}
	
	public void updatePw(PwSearchDTO pwDTO) {
		System.out.println("updatePw\t");
		oracle.update("member.updatePw", pwDTO);
		oracle.commit();
	}

	//=====================================================================
	
	
	
	
	public void insertFriend(IdDTO vo) {
		System.out.println("insertFriend");
		oracle.insert("Friend.insertFriend", vo);
		oracle.commit();
	}
	
	public List<ProfileDTO> selectFriendList(String id) {
		System.out.println("selectFriendList");
		String value = id;
		return oracle.selectList("Friend.selectFriendList", value);
	}
	
	
	//=====================================================================
	
	
	public void insertChat(ChatRoomDTO crDTO) {
		System.out.println(crDTO.getC_name() + "과의 채팅방 생성!");
		sqlite.insert("Chat.insertChat", crDTO);
		sqlite.commit();
	}
	
	public List<ChatRoomDTO> selectChatList() {
		System.out.println("selectChatList");
		return sqlite.selectList("Chat.selectChatList");
	}
}

