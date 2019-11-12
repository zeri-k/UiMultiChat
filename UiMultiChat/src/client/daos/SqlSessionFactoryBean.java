package client.daos;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryBean {
	private static SqlSessionFactory sessionFactoryOracle = null;
	private static SqlSessionFactory sessionFactorySqlite = null;
	private final static String res="orm/mybatis/config/mybatis-config.xml";
	static {
		try {
			if(sessionFactoryOracle == null) {
				// mybatis-config.xml 파일로 부터 설정 정보를 얻어오기 위한 입력스트림 생성
				Reader reader = Resources.getResourceAsReader(res);
		        // SqlSessionFactory 객체 생성
				sessionFactoryOracle = new SqlSessionFactoryBuilder().build(reader, "oracle");
				System.out.println("oracle 빌드");
			}
			if(sessionFactorySqlite == null) {
				// mybatis-config.xml 파일로 부터 설정 정보를 얻어오기 위한 입력스트림 생성
				Reader reader = Resources.getResourceAsReader(res);
				sessionFactorySqlite = new SqlSessionFactoryBuilder().build(reader, "sqlite");
				System.out.println("sqlite 빌드");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SqlSession getSqlsessionInstanceOracle() {
		System.out.println("oracle SqlSession 생성");
		// SqlSessionFactory 객체로부터 SqlSession 객체를 얻어내어 리턴
		return sessionFactoryOracle.openSession();
	}
	
	public static SqlSession getSqlsessionInstanceSqlite() {
		System.out.println("sqlite SqlSession 생성");
		// SqlSessionFactory 객체로부터 SqlSession 객체를 얻어내어 리턴
		return sessionFactorySqlite.openSession();
	}
}
