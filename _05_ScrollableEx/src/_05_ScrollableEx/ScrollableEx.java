package _05_ScrollableEx;

import java.sql.*;

public class ScrollableEx {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"bitcamp",
					"bitcamp");
			
			// 이번에는 미리 테이블을 만들어놓자
			// SQLDeveloper에서
			
			sql = "SELECT * FROM scrolltest";
			pstmt = con.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_SENSITIVE,/*커서 양방향 이동*/
					ResultSet.CONCUR_UPDATABLE);     /*데이터 동적 갱신 가능*/
			rs = pstmt.executeQuery();
			
			System.out.println("next()================================");
			while(rs.next()) {
				System.out.println("name: " + rs.getString(1) +
								", gender: " + rs.getString(2));
			}
			
			System.out.println("previous()================================");
			while(rs.previous()) {
				System.out.println("name: " + rs.getString(1) +
								", gender: " + rs.getString(2));
			}
			
			System.out.println("first()================================");
			rs.first();
			System.out.println("name: " + rs.getString(1) +
							", gender: " + rs.getString(2));
			
			System.out.println("last()================================");
			rs.last();
			System.out.println("name: " + rs.getString(1) +
							", gender: " + rs.getString(2));
			
			System.out.println("absolute(2)================================");
			rs.absolute(2);
			System.out.println("name: " + rs.getString(1) +
							", gender: " + rs.getString(2));
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}













