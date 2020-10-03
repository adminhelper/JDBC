package _02_JDBC_PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
SQL문을 사용하는 3가지 종류의 클래스
Statement               : 기본
PreparedStatement extends Statement				: 속도향상, 변수전달 편리
CallableStatement extends PreparedStatement		: 트리거, 프로시저
*/

//statement는 실행순간에 SQL문을 파싱, 최적화, 전송을 한다.
//
//preparedStatement는 사전에 미리 SQL문을 파싱, 최적화하고
//실행순간에 전달만 한다
//그래서 preparedStatement가 속도가 더 빠르다.
//인자만 나중에 전달하므로 매우 편리하다

public class JdbcPreparedStatement {
	// 1) OracleDriver 클래스를 객체화한다
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// 2) 클래스 변수 준비
		Connection con = null;			// 오라클 서버 연결
		PreparedStatement pstmt = null; // SQL문 전달
		ResultSet rs = null;			// SELECT문의 결과
		
		try {
			// 3) 오라클 서버에 접속
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"bitcamp",
					"bitcamp");
			StringBuffer sb = new StringBuffer();
			int uCount = 0;
			
			// 4) aaa 테이블이 있다면 제거해라
			try {
				sb.setLength(0);
				sb.append("DROP TABLE aaa");
				//stmt.executeUpdate(sb.toString());
				pstmt = con.prepareStatement(sb.toString());
				uCount = pstmt.executeUpdate();
				System.out.println(sb.toString());
				System.out.println("DROP COUNT : " + uCount);
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			
			// 5) aaa 테이블을 생성해라
			sb.setLength(0);
			sb.append("CREATE TABLE aaa(id VARCHAR2(10), ");
			sb.append("password VARCHAR2(10))");
			pstmt = con.prepareStatement(sb.toString());
			uCount = pstmt.executeUpdate();
			System.out.println(sb.toString());
			System.out.println("CREATE COUNT : " + uCount);
			
			// 6) 데이터 입력
			sb.setLength(0);
			sb.append("INSERT INTO aaa VALUES(?, ?)");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, "chicken");
			pstmt.setString(2, "baseball");
			uCount = pstmt.executeUpdate();
			System.out.println(sb.toString());
			System.out.println("INSERT COUNT : " + uCount);
			
			// 7) 데이터 검색
			sb.setLength(0);
			sb.append("SELECT * FROM aaa");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			System.out.println(sb.toString());
			while(rs.next()) {
				System.out.print("id : " + rs.getString(1));
				System.out.println(", password : " + rs.getString(2));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				if(pstmt!=null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				if(con!=null) con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}









