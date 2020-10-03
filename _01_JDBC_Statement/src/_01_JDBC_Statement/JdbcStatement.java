package _01_JDBC_Statement;

import java.sql.*;

/*
1. ojdbcX.jar 을 포함한다(Add Externals Libraries)
2. import java.sql.*;
3. 오라클 드라이버 객체를 로딩한다
4. Connection 객체를 생성한다(오라클 접속용도)
5. Statement 객체를 생성한다(오라클에 Query문 전달)
6. Query에 결과가 있다면 (SELECT문 사용시)
   ResultSet 객체를 생성한다(데이터 목록 저장객체)
7. ResultSet -> Statement -> Connection 순서로
   (즉, Open한 역순으로 ) Close()한다.
*/
public class JdbcStatement {
//	static 초기화 영역
//	가장 먼저 호출되는 영역
//	static 필드를 초기화하는 곳
	static {
//		1) 오라클 드라이버 객체를 로딩한다
//		DriverManager내부에서 사용할 Oracle 드라이버 객체를
//		생성해서 메모리에 로딩함
//		프로그램에서 직접 접근은 하지 않으므로 클래스 변수에
//		대입하지 않았다.
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection con = null;	// 오라클 접속용도
		Statement stmt = null;	// SQL문 실행용도
		ResultSet rs = null;	// SELECT문 결과 받는 용도
		

		try {
//			2) Connection 객체를 생성한다
			// 오라클에 접속한다.
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"bitcamp",
					"bitcamp");
			
//			3) Statement 객체를 생성한다
			stmt = con.createStatement();
			StringBuffer sb = new StringBuffer();
			int updateCount = 0;
			
//			4) 프로그램을 여러번 실행하기 위해 test1테이블이 있으면
//			삭제하라는 명령
			try {
				sb.setLength(0);	// 새로운 문자열 담을 때
				sb.append("DROP TABLE test1");
				stmt.executeUpdate(sb.toString());
				System.out.println(sb.toString());
			}catch(SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
//			5) test1 테이블 생성
			sb.setLength(0);
			sb.append("CREATE TABLE test1(" +
					 "id VARCHAR2(10), "
					 + "age NUMBER)");
			//executeUpdate의 리턴값
//			DML(INSERT, UPDATE, DELETE) : 실행된 행의 개수
//			DDL(CREATE, DROP) : 0, 실행된 행의 개수가 없다
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println(sb.toString());
			System.out.println("createCount: " + updateCount);
			
//			6) 데이터 입력
			sb.setLength(0);
			sb.append("INSERT INTO test1 ");
			sb.append("VALUES ('aaa000'");
			sb.append(", 10)");
			System.out.println(sb.toString());
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("INSERT COUNT : " + updateCount);
			
//			7) 데이터 검색
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			System.out.println(sb.toString());
			rs = stmt.executeQuery(sb.toString());
			while(rs.next()) {
				System.out.print("id: " + rs.getString(1) + ", ");
				System.out.println("age: " + rs.getString(2));
			}
			
//			8) Update문 하기
			sb.setLength(0);
			sb.append("UPDATE test1 SET id='bbb000', ");
			sb.append("age=20 ");
			sb.append("WHERE id='aaa000'");
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println(sb.toString());
			System.out.println("UPDATE COUNT : " + updateCount);

//			9) 데이터 검색
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			System.out.println(sb.toString());
			rs = stmt.executeQuery(sb.toString());
			while(rs.next()) {
				System.out.print("id: " + rs.getString(1) + ", ");
				System.out.println("age: " + rs.getString(2));
			}			
			
//			10) 삭제하기
			sb.setLength(0);
			sb.append("DELETE FROM test1");
			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println(sb.toString());
			System.out.println("DELETE COUNT : " + updateCount);
			
//			9) 데이터 검색
			sb.setLength(0);
			sb.append("SELECT * FROM test1");
			System.out.println(sb.toString());
			rs = stmt.executeQuery(sb.toString());
			while(rs.next()) {
				System.out.print("id: " + rs.getString(1) + ", ");
				System.out.println("age: " + rs.getString(2));
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
				if(stmt!=null) stmt.close();
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




