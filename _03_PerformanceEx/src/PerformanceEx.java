
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PerformanceEx{ 
	// 1) OracleDriver 클래스 객체가 메모리로 Loading됨
	//    Why? DriverManager가 필요로 하니까
	static{ 
		try{ 
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
		}catch(ClassNotFoundException cnfe){ 
			cnfe.printStackTrace(); 
		} 
	} 
	
	public static void main(String[] args){
		// 시간을 재려고 
		long start; 
		long end; 
		
		// 오라클 접속 객체 변수
		Connection con = null; 
		// 사전에 Query문을 최적화 해놓고, 전송시 인자만 받아서 바로 전달
		// (속도가 상대적으로 더 빠르다)
		// (인자를 변수로 전달할 수 있어서 편리하다)
		PreparedStatement pstmt = null; 
		
		// Query문을 받아서 최적화 과정을 거쳐서 전송한다
		// (속도가 느리다 상대적으로 더 느리다)
		// (인자를 문자열 + 연산으로 전달해야 해서 불편하다)
		Statement stmt = null; 

		try{ 
			// 2) 오라클 서버와 연결
			con = DriverManager.getConnection( 
					// localhost == 127.0.0.1
					"jdbc:oracle:thin:@127.0.0.1:1521:xe", 
					"bitcamp", 
					"bitcamp"); 
			stmt = con.createStatement();
			try {
				stmt.executeUpdate("DROP TABLE performance");
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			String sql ="CREATE TABLE performance(id varchar(10)," + 
					"password varchar(10))"; 
			// 파싱->최적화->오라클에 전송
			stmt.executeUpdate(sql); 

			start=System.currentTimeMillis(); 
			for(int i=0;i<10000;i++){ 
				// 파싱->최적화->오라클에 전송
				stmt.executeUpdate("INSERT INTO performance values" 
				+"('"+i+"','"+i+"')"); 
				}

			end = System.currentTimeMillis(); 
			System.out.println("Statement process time = " 
					+(end - start)); 

			sql ="INSERT INTO performance values(?,?)"; 
			// pstmt객체가 만들어질 때 파싱->최적화
			pstmt = con.prepareStatement(sql); 

			start = System.currentTimeMillis(); 
			for(int j=0;j<10000;j++){ 
				// 인자 전달
				pstmt.setString(1,""+j); 
				pstmt.setString(2,""+j); 
				// 전송
				pstmt.executeUpdate(); 
			} 
			end = System.currentTimeMillis();

			System.out.println("PreparedStatement process time = " 
					+(end - start)); 
			 
		}catch(SQLException ee){ 
			ee.printStackTrace(); 
		}finally{ 
			try { 
				if(pstmt != null) pstmt.close(); 
				if(stmt != null) stmt.close(); 
				if(con != null) con.close(); 
			}catch(SQLException se){} 
		} 
	} 
}
