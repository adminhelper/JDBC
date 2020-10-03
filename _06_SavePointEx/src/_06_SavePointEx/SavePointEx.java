package _06_SavePointEx;

import java.sql.*;

/*
Rollback�� �ϰ� �Ǹ� Commit�� ���� ���� ������ ��� Query��
�����ϰ� �ȴ�.
������ ���߿��� ������ �� �͵� �ֱ� ������ 
���� ���� Query�߿� �߰��߰� SavePoint�� ������ ������
���� �������� �ʰ� �ش� ������ SavePoint ���±����� Rollback
�� �� �ִ�.
*/
/*
���� ���̺� ���¸� ����
Savepoint save1 = con.setSavepoint();
Savepoint save2 = con.setSavepoint();
�ش� savePoint�� �����Ϸ���
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
			
			// SavePoint�� ����ϱ� ���� �ڵ�Ŀ���� false�Ѵ�
			con.setAutoCommit(false);
			
			// savepoint���̺��� �̸� ��������
			
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
			
			// ���� ���¸� Ȯ��
			while(rs.next()) {
				System.out.println("id: " + rs.getString("id")
							+ ", total: " + rs.getString("total"));
			}
			System.out.println("save1==========================");
			// ������ (Ʈ�����)���������� ���¸� ������ ���´�
			Savepoint save1 = con.setSavepoint();	
			
			// ���� �����͸� �б� �� ���·� Ŀ���� ��ġ��Ų��
			// Ŀ�� : ���� ��ġ
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
					System.out.println("save1���� Rollback...");
					con.rollback(save1);
				}
			}
			System.out.println("=============================");

			selectPstmt.setInt(1, 100);
			rs = selectPstmt.executeQuery();
			// ���� ���¸� Ȯ��
			while(rs.next()) {
				System.out.println("id: " + rs.getString("id")
							+ ", total: " + rs.getString("total"));
			}
			System.out.println("save2==========================");
			// ������ (Ʈ�����)���������� ���¸� ������ ���´�
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
					System.out.println("save2���� Rollback...");
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


















