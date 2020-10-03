package _06_SavePointEx;

import java.sql.*;

/*
Rollback을 하게 되면 Commit을 하지 않은 기존의 모든 Query를
복원하게 된다.
하지만 이중에는 성공이 된 것도 있기 때문에 
여러 번의 Query중에 중간중간 SavePoint를 저장해 놓으면
전부 복원하지 않고 해당 지점인 SavePoint 상태까지만 Rollback
할 수 있다.
*/
/*
현재 테이블 상태를 저장
Savepoint save1 = con.setSavepoint();
Savepoint save2 = con.setSavepoint();
해당 savePoint로 복원하려면
con.rollback(save2);
*/
public class SavePointEx {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement selectPstmt = null;
		PreparedStatement updatePstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"bitcamp",
					"bitcamp");
			
			// SavePoint를 사용하기 위해 자동커밋을 false한다
			con.setAutoCommit(false);
			
			// savepoint테이블을 미리 생성하자
			
			String selectQuery = "SELECT id, total FROM savepoint"
						+ "\r\n" + "WHERE total > ?";
			String updateQuery = "UPDATE savepoint SET total = ?"
						+ "\r\n" + "WHERE id = ?";
			
			selectPstmt = con.prepareStatement(selectQuery,
								ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_UPDATABLE);
			updatePstmt = con.prepareStatement(updateQuery,
								ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_UPDATABLE);
			
			selectPstmt.setInt(1, 100);
			rs = selectPstmt.executeQuery();
			
			// 현재 상태를 확인
			while(rs.next()) {
				System.out.println("id: " + rs.getString("id")
							+ ", total: " + rs.getString("total"));
			}
			System.out.println("save1==========================");
			// 최초의 (트랜잭션)복원데이터 상태를 저장해 놓는다
			Savepoint save1 = con.setSavepoint();	
			
			// 아직 데이터를 읽기 전 상태로 커서를 위치시킨다
			// 커서 : 행의 위치
			rs.beforeFirst();
			while(rs.next()) {
				String id = rs.getString("id");
				int oldTotal = rs.getInt("total");
				int updateTotal = oldTotal * 2;				

				updatePstmt.setInt(1, updateTotal);
				updatePstmt.setString(2, id);
				updatePstmt.executeUpdate();
				
				System.out.println("New Total of " + 
						oldTotal + " is " + updateTotal);
				
				if(updateTotal >= 5000) {
					System.out.println("save1으로 Rollback...");
					con.rollback(save1);
				}
			}
			System.out.println("=============================");

			selectPstmt.setInt(1, 100);
			rs = selectPstmt.executeQuery();
			// 현재 상태를 확인
			while(rs.next()) {
				System.out.println("id: " + rs.getString("id")
							+ ", total: " + rs.getString("total"));
			}
			System.out.println("save2==========================");
			// 최초의 (트랜잭션)복원데이터 상태를 저장해 놓는다
			Savepoint save2 = con.setSavepoint();	
			
			rs.beforeFirst();
			while(rs.next()) {
				String id = rs.getString("id");
				int oldTotal = rs.getInt("total");
				int updateTotal = oldTotal * 2;				

				updatePstmt.setInt(1, updateTotal);
				updatePstmt.setString(2, id);
				updatePstmt.executeUpdate();
				
				System.out.println("New Total of " + 
						oldTotal + " is " + updateTotal);
				
				if(updateTotal >= 5000) {
					System.out.println("save2으로 Rollback...");
					con.rollback(save2);
				}
			}
			System.out.println("=============================");
			
			con.commit();
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM savepoint");
			while(rs.next()) {
				String id = rs.getString("id");
				int total = rs.getInt("total");
				System.out.println("id: " + id + 
						", total: " + total);
			}
			
			con.setAutoCommit(true);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(selectPstmt!=null) selectPstmt.close();
				if(updatePstmt!=null) updatePstmt.close();
				if(con!=null) con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}
	}
}



















