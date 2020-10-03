package drugstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;

public class DS_AdminDB {
	DS_Method dsmethod = new DS_Method();

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = null;

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//////////////////////////////////////////////// 여기부터 회원관리 메소드

	// 회원 전체 출력
	public synchronized void showAllCustomer(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

			sql = "SELECT * FROM CUSTOMER ORDER BY co_id ASC";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dsmethod.broadCast(idKey, map, "----------------------------");
				dsmethod.broadCast(idKey, map, "회원번호 : " + rs.getInt(1));
				dsmethod.broadCast(idKey, map, "ID : " + rs.getString(2));
				dsmethod.broadCast(idKey, map, "PASSWORD : " + rs.getString(3));
				dsmethod.broadCast(idKey, map, "이름 : " + rs.getString(4));
				dsmethod.broadCast(idKey, map, "생년월일 : " + rs.getString(5));
				dsmethod.broadCast(idKey, map, "전화번호: " + rs.getString(6));
				dsmethod.broadCast(idKey, map, "포인트 : " + rs.getInt(7));
				dsmethod.broadCast(idKey, map, "----------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 회원 검색
	public synchronized void searchCustomer(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
			dsmethod.broadCast(idKey, map, "검색할 ID를 입력해주세요");
			String selectScan = dsmethod.getScanner(br);
			sql = "SELECT * FROM CUSTOMER WHERE CO_LOGINID LIKE '%" + selectScan + "%'";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dsmethod.broadCast(idKey, map, "----------------------------");
				dsmethod.broadCast(idKey, map, "회원번호 : " + rs.getInt(1));
				dsmethod.broadCast(idKey, map, "ID : " + rs.getString(2));
				dsmethod.broadCast(idKey, map, "PASSWORD : " + rs.getString(3));
				dsmethod.broadCast(idKey, map, "이름 : " + rs.getString(4));
				dsmethod.broadCast(idKey, map, "생년월일 : " + rs.getString(5));
				dsmethod.broadCast(idKey, map, "전화번호: " + rs.getString(6));
				dsmethod.broadCast(idKey, map, "포인트 : " + rs.getInt(7));
				dsmethod.broadCast(idKey, map, "----------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 회원 삭제
	public synchronized void deleteCustomer(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		PreparedStatement updatepstmt = null;
		PreparedStatement defaultpstmt = null;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
			dsmethod.broadCast(idKey, map, "삭제할 아이디를 입력하세요");
			String selectScan = dsmethod.getScanner(br);
			sql = "SELECT * FROM CUSTOMER WHERE CO_LOGINID LIKE '%" + selectScan + "%'";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			String updateSql = "UPDATE CUSTOMER SET CO_ID = ? WHERE CO_LOGINID = ?";
			String defaultSql = "SELECT * FROM CUSTOMER";
			updatepstmt = con.prepareStatement(updateSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
					ResultSet.CONCUR_UPDATABLE);
			defaultpstmt = con.prepareStatement(defaultSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
					ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			rs = pstmt.executeQuery();

			while (rs.next()) {
				dsmethod.broadCast(idKey, map, "----------------------------");
				dsmethod.broadCast(idKey, map, "회원번호 : " + rs.getInt(1));
				dsmethod.broadCast(idKey, map, "ID : " + rs.getString(2));
				dsmethod.broadCast(idKey, map, "PASSWORD : " + rs.getString(3));
				dsmethod.broadCast(idKey, map, "이름 : " + rs.getString(4));
				dsmethod.broadCast(idKey, map, "생년월일 : " + rs.getString(5));
				dsmethod.broadCast(idKey, map, "전화번호: " + rs.getString(6));
				dsmethod.broadCast(idKey, map, "포인트 : " + rs.getInt(7));
				dsmethod.broadCast(idKey, map, "----------------------------");
			}

			while (true) {
				dsmethod.broadCast(idKey, map, "회원을 삭제하시겠습니까? (Y / N)");
				String yesOrNo = dsmethod.getScanner(br);
				if (yesOrNo.equalsIgnoreCase("Y")) {
					break;
				} else if (yesOrNo.equalsIgnoreCase("N")) {
					dsmethod.broadCast(idKey, map, "삭제가 취소되었습니다.");
					return;
				} else {
					dsmethod.broadCast(idKey, map, "잘못 입력하셨습니다.");
				}
			}

			sql = "DELETE FROM CUSTOMER WHERE CO_LOGINID LIKE '%" + selectScan + "%' ";

			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.execute();

			con.commit();

			dsmethod.broadCast(idKey, map, "삭제되었습니다.");

			rs = defaultpstmt.executeQuery();

			dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
			int rowNum = 0;
			while (rs.next()) {
				rowNum = rs.getRow();
				String checkname = rs.getString(2);
				updatepstmt.setInt(1, rowNum);
				updatepstmt.setString(2, checkname);
				updatepstmt.executeUpdate();
			}

			con.commit();
			con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	//////////////////////////////////////////////// 여기부터 재고 관리 메소드

	// 재고 테이블 모든 상태 출력(품번, 재고명, 수량, 단가)
	public synchronized void showAllStuff(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

			sql = "SELECT * FROM stock ORDER BY ST_ID ASC";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
			while (rs.next()) {
				dsmethod.broadCast(idKey, map, "품번 : " + rs.getInt(1) + ", 재고명: " + rs.getString(2) + ", 수량: "
						+ rs.getString(3) + ", 단가: " + rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 재고 입력
	public synchronized void insertStuff(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		PreparedStatement defaultpstmt = null;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

			sql = "INSERT INTO stock (ST_ID, ST_NAME, ST_NUM, ST_PRICE) VALUES (?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			String defaultSql = "SELECT * FROM stock";
			defaultpstmt = con.prepareStatement(defaultSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
					ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			rs = defaultpstmt.executeQuery();
			rs.last();
			int id = rs.getRow() + 1;
			dsmethod.broadCast(idKey, map, "새로 입력하실 제품의 품번은 -" + id + "-입니다.");
			dsmethod.broadCast(idKey, map, "");
			dsmethod.broadCast(idKey, map, "새로 입력하실 제품의 이름을 입력하세요.");
			String name = dsmethod.getScanner(br);
			dsmethod.broadCast(idKey, map, "새로 입력하실 제품의 수량을 입력하세요.");
			int num = dsmethod.checkException(idKey, br, map);
			;
			dsmethod.broadCast(idKey, map, "새로 입력하실 제품의 단가를 입력하세요.");
			int price = dsmethod.checkException(idKey, br, map);

			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			pstmt.setInt(3, num);
			pstmt.setInt(4, price);
			rs.last();
			pstmt.execute();

			con.commit();
//			isCommit = true;
			con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

			dsmethod.broadCast(idKey, map, "품번 : " + id + ", 재고명: " + name + ", 수량: " + num + ", 단가: " + price);
			dsmethod.broadCast(idKey, map, "재고 입력이 완료되었습니다.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 재고 검색
	   public synchronized void searchStuff(String idKey, BufferedReader br, HashMap<String, PrintWriter> map)
	         throws IOException {

	      try {
	         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

	         sql = "SELECT * FROM stock WHERE ST_ID = ?";
	         pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
	               ResultSet.CONCUR_UPDATABLE); /* 데이터 동적 갱신 가능 */

	         dsmethod.broadCast(idKey, map, "검색하실 제품의 품번을 입력하세요.");
	         int id = dsmethod.checkException(idKey, br, map);

	         pstmt.setInt(1, id);
	         rs = pstmt.executeQuery();
	         while (rs.next()) {
	            dsmethod.broadCast(idKey, map, "품번: " + rs.getInt(1) + ", 재고명: " + rs.getString(2) + ", 수량: "
	                  + rs.getInt(3) + ", 단가: " + rs.getInt(4));
	         }

	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         closeAll(rs, pstmt, con);
	      }
	   }

	   // 재고 수정
	   public synchronized void updateStuff(String idKey, BufferedReader br, HashMap<String, PrintWriter> map)
	         throws IOException {
	      PreparedStatement updatepstmt = null;

	      try {
	         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

	         sql = "SELECT * FROM stock WHERE ST_ID = ?";
	         String updateSql = "UPDATE stock SET ST_NAME = ?, ST_NUM = ?, ST_PRICE = ? WHERE ST_ID = ?";
	         pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
	               ResultSet.CONCUR_UPDATABLE); /* 데이터 동적 갱신 가능 */
	         updatepstmt = con.prepareStatement(updateSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
	               ResultSet.CONCUR_UPDATABLE);

	         con.setAutoCommit(false); // 수동커밋으로 변경

	         dsmethod.broadCast(idKey, map, "수정하실 제품의 품번을 입력하세요.");
	         int id = dsmethod.checkException(idKey, br, map);

	         pstmt.setInt(1, id);
	         rs = pstmt.executeQuery();
	         while (rs.next()) {
	            dsmethod.broadCast(idKey, map, "품번: " + rs.getInt(1) + ", 재고명: " + rs.getString(2) + ", 수량: "
	                  + rs.getInt(3) + ", 단가: " + rs.getInt(4));
	         }

	         rs.first();
	         String name = null;
	         while (true) {
	            dsmethod.broadCast(idKey, map, rs.getInt(1) + "번 제품의 이름을 수정하시겠습니까? (Y / N)");
	            String yesOrNo = dsmethod.getScanner(br);
	            
	            if (yesOrNo.equalsIgnoreCase("Y")) {
	               dsmethod.broadCast(idKey, map, "수정하실 이름을 입력해주세요.");
	               name = dsmethod.getScanner(br);
	               ;
	               break;
	            } else if (yesOrNo.equalsIgnoreCase("N")) {
	               name = rs.getString(2);
	               dsmethod.broadCast(idKey, map, "제품의 이름은 -" + name + "-입니다.");
	               break;
	            } else {
	               dsmethod.broadCast(idKey, map, "잘못 입력하셨습니다.");
	            }
	         }
	         dsmethod.broadCast(idKey, map, "수정하실 제품의 수량을 입력하세요.");
	         int num = dsmethod.checkException(idKey, br, map);
	         dsmethod.broadCast(idKey, map, "수정하실 제품의 단가를 입력하세요.");
	         int price = dsmethod.checkException(idKey, br, map);

	         updatepstmt.setString(1, name);
	         updatepstmt.setInt(2, num);
	         updatepstmt.setInt(3, price);
	         updatepstmt.setInt(4, id);
	         int temp = updatepstmt.executeUpdate();

	         con.commit();

	         con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

	         dsmethod.broadCast(idKey, map, "품번 : " + id + ", 재고명: " + name + ", 수량: " + num + ", 단가: " + price);
	         dsmethod.broadCast(idKey, map, "재고 수정이 완료되었습니다.");

	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (updatepstmt != null)
	               updatepstmt.close();
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         closeAll(rs, pstmt, con);
	      }
	   }

	   // 재고 삭제
	   public synchronized void deleteStuff(String idKey, BufferedReader br, HashMap<String, PrintWriter> map)
	         throws IOException {
	      PreparedStatement deletepstmt = null;
	      PreparedStatement updatepstmt = null;
	      PreparedStatement defaultpstmt = null;

	      try {
	         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

	         sql = "SELECT * FROM stock WHERE ST_ID = ?";
	         String deleteSql = "DELETE stock WHERE ST_ID = ?";
	         String updateSql = "UPDATE stock SET ST_ID = ? WHERE ST_NAME = ?";
	         String defaultSql = "SELECT * FROM stock";
	         pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
	               ResultSet.CONCUR_UPDATABLE); /* 데이터 동적 갱신 가능 */
	         deletepstmt = con.prepareStatement(deleteSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
	               ResultSet.CONCUR_UPDATABLE);
	         updatepstmt = con.prepareStatement(updateSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
	               ResultSet.CONCUR_UPDATABLE);
	         defaultpstmt = con.prepareStatement(defaultSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
	               ResultSet.CONCUR_UPDATABLE);

	         con.setAutoCommit(false); // 수동커밋으로 변경

	         dsmethod.broadCast(idKey, map, "삭제하실 제품의 품번을 입력하세요.");
	         int id = dsmethod.checkException(idKey, br, map);

	         pstmt.setInt(1, id);
	         rs = pstmt.executeQuery();
	         while (rs.next()) {
	            dsmethod.broadCast(idKey, map, "품번: " + rs.getInt(1) + ", 재고명: " + rs.getString(2) + ", 수량: "
	                  + rs.getInt(3) + ", 단가: " + rs.getInt(4));
	         }

	         while (true) {
	            dsmethod.broadCast(idKey, map, "제품을 삭제하시겠습니까? (Y / N)");
	            String yesOrNo = dsmethod.getScanner(br);
	            ;
	            if (yesOrNo.equalsIgnoreCase("Y")) {
	               break;
	            } else if (yesOrNo.equalsIgnoreCase("N")) {
	               dsmethod.broadCast(idKey, map, "삭제가 취소되었습니다.");
	               return;
	            } else {
	               dsmethod.broadCast(idKey, map, "잘못 입력하셨습니다.");
	            }
	         }

	         rs = defaultpstmt.executeQuery();

	         while (rs.next()) {
	            deletepstmt.setInt(1, id);
	            deletepstmt.executeQuery();
	         }

	         con.commit();

	         dsmethod.broadCast(idKey, map, "삭제가 완료되었습니다.");

	         rs = defaultpstmt.executeQuery();

	         dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
	         int rowNum = 0;
	         while (rs.next()) {
	            rowNum = rs.getRow();
	            String checkname = rs.getString(2);
	            updatepstmt.setInt(1, rowNum);
	            updatepstmt.setString(2, checkname);
	            updatepstmt.executeUpdate();
	         }

	         con.commit();

	         con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (updatepstmt != null)
	               updatepstmt.close();
	            if (deletepstmt != null)
	               deletepstmt.close();
	            if (defaultpstmt != null)
	               defaultpstmt.close();
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         closeAll(rs, pstmt, con);
	      }
	   }
	   
	////////////////////////////////////////// 여기부터 수익관련 메소드

	// 전체회원 포인트조회 // 로그인ID 순서로 출력
	public synchronized void pointAllSearch(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

			sql = "SELECT * FROM customer ORDER BY co_loginid ASC";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dsmethod.broadCast(idKey, map, "회원 ID : " + rs.getString("co_loginid") + ", 회원 이름 : "
						+ rs.getString("co_name") + ", 포인트 : " + rs.getString("co_point"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 수익 조회
	public synchronized void incomeSearch(String idKey, HashMap<String, PrintWriter> map) {

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

			sql = "SELECT in_id, sum(in_money) FROM income GROUP BY in_id ORDER BY in_id ASC";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
			while (rs.next()) {
				dsmethod.broadCast(idKey, map, "고객번호 : " + rs.getInt(1) + ", 총 구매금액: " + rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	public void closeAll(ResultSet rs, PreparedStatement pstmt, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}