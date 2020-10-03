package _07_ConnectionPool;

import java.sql.*;

// Client��� �����ϰ� ConnectionPool�� �̿��ؼ� ����Ŭ�� ����
public class ConnectionPoolEx {
	public static void main(String[] args) {
		ConnectionPool cp = null;
		Connection con = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "bitcamp";
		String password = "bitcamp";
		int initialCons = -1;	// �⺻������ �����ϰڴ�
		int maxCons = -1;	// �⺻������ �����ϰڴ�
		
		try {
			// ConnectionPool ��ü�� ���´�
			cp = ConnectionPool.getInstance(url, user, password, initialCons, maxCons);
			// ConnectionPool�� Connection ��ü�� ��û�Ѵ�
			con = cp.getConnection();
			pStmt = con.prepareStatement("SELECT * FROM dept");
			rs = pStmt.executeQuery();
			while(rs.next()) {
				System.out.print(rs.getString(1) + ", ");
				System.out.print(rs.getString(2) + ", ");
				System.out.print(rs.getString(3) + ", ");
				System.out.println(rs.getString(4));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pStmt!=null) pStmt.close();
				// ���⼭�� close()�ϸ� �ȵǰ�, cp�� �ݳ��ؾ� �Ѵ�
				if(con!=null) cp.releaseConnection(con);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// ��� ���񽺰� �����ٸ�, ��� �����Ѵ�
		cp.closeAll();
	}
}




