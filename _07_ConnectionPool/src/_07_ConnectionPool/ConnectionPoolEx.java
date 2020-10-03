package _07_ConnectionPool;

import java.sql.*;

// Client라고 가정하고 ConnectionPool을 이용해서 오라클과 연동
public class ConnectionPoolEx {
	public static void main(String[] args) {
		ConnectionPool cp = null;
		Connection con = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "bitcamp";
		String password = "bitcamp";
		int initialCons = -1;	// 기본값으로 설정하겠다
		int maxCons = -1;	// 기본값으로 설정하겠다
		
		try {
			// ConnectionPool 객체를 얻어온다
			cp = ConnectionPool.getInstance(url, user, password, initialCons, maxCons);
			// ConnectionPool에 Connection 객체를 요청한다
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
				// 여기서는 close()하면 안되고, cp에 반납해야 한다
				if(con!=null) cp.releaseConnection(con);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// 모든 서비스가 끝났다면, 모두 해제한다
		cp.closeAll();
	}
}





