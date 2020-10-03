package comicbook;

import java.sql.*;

public class DB {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = null;
	View view;

	public DB(View view) {
		this.view = view;
	}

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 회원 목록 확인
	public void showAllMemberList() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");

			sql = "SELECT * FROM member ORDER BY 1";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				view.dataTxt.append("ID : " + rs.getString(1));
				view.dataTxt.append(", PW : " + rs.getString(2));
				view.dataTxt.append(", NAME : " + rs.getString(3));
				view.dataTxt.append(", PHONE : " + rs.getString(4));
				view.dataTxt.append(", ADDRESS : " + rs.getString(5) + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 대여목록 확인
	public void showAllRentalList() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");

			sql = "SELECT r.RENTAL_NUM, ID, BOOK_NUM, RENTAL_DATE, RETURN_DATE  FROM rental r, rentaldetails rd "
					+ "WHERE r.rental_num = rd.rental_num ORDER BY 1";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				view.dataTxt.append("대여 번호 : " + rs.getString(1));
				view.dataTxt.append(", 회원 ID : " + rs.getString(2));
				view.dataTxt.append(", 책 번호 : " + rs.getString(3));
				view.dataTxt.append(", 대여 일자 : " + rs.getString(4));
				view.dataTxt.append(", 반납 일자 : " + rs.getString(5) + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 책 입고
	public void bookInput(String num, String title, String page, String fee) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			String quantity = "1";
			sql = "INSERT INTO book(BOOK_NUM,TITLE,PAGE,RENTAL_FEE,QUANTITY) VALUES(?,?,?,?,?)";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			pstmt.setString(1, num);
			pstmt.setString(2, title);
			pstmt.setString(3, page);
			pstmt.setString(4, fee);
			pstmt.setString(5, quantity);
			rs = pstmt.executeQuery();

			con.commit();
			con.setAutoCommit(true); // 자동커밋으로 변경

			view.dataTxt.append("책 번호 : " + num + "책 제목 : " + title + "페이지 수 : " + page + "대여료 : " + fee + "수량 : "
					+ quantity + "\n");
			view.sysTxt.append("책 입고가 완료 되었습니다\n");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 전체 책 목록 조회
	public void showAllBook() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			sql = "SELECT * FROM book ORDER BY 1";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				view.dataTxt.append("책번호: " + rs.getInt(1));
				view.dataTxt.append(", 제목: " + rs.getString(2));
				view.dataTxt.append(", 페이지: " + rs.getString(3));
				view.dataTxt.append(", 대여료: " + rs.getString(4));
				view.dataTxt.append(", 수량: " + rs.getString(5) + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 회원가입
	public void memberInput(String id, String pw, String name, String num, String adr) {
		String id_sql;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");

			id_sql = "SELECT id FROM member";
			pstmt = con.prepareStatement(id_sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (id.equals(rs.getString(1))) {
					view.sysTxt.append("사용할 수 없는 아이디입니다.\n");
					break;
				} else {
					sql = "INSERT INTO member(ID,PW,NAME,PHONE,ADDRESS) VALUES(?,?,?,?,?)";
					pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

					con.setAutoCommit(false); // 수동커밋으로 변경

					pstmt.setString(1, id);
					pstmt.setString(2, pw);
					pstmt.setString(3, name);
					pstmt.setString(4, num);
					pstmt.setString(5, adr);
					rs = pstmt.executeQuery();

					con.commit();
					con.setAutoCommit(true); // 자동커밋으로 변경
					view.sysTxt.append("회원 가입이 완료되었습니다\n");
					break;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 로그인
	public boolean login(String id, String pw) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			sql = "SELECT ID,PW FROM member";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (id.equals(rs.getString(1))) {
					if (pw.equals(rs.getString(2)))
						return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
		return false;
	}

	// 대여가능한 책 목록 조회
	public void showEnableBook() {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			sql = "SELECT * FROM book WHERE quantity!=0 ORDER BY 1";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				view.dataTxt.append("책번호: " + rs.getString(1));
				view.dataTxt.append(", 제목: " + rs.getString(2));
				view.dataTxt.append(", 페이지: " + rs.getString(3));
				view.dataTxt.append(", 대여료: " + rs.getString(4));
				view.dataTxt.append(", 대여가능 수량: " + rs.getString(5) + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 대여
	public void rental(String bNum) {
		PreparedStatement rentalpstmt = null;
		int count = 0;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			String rentalSql = "UPDATE book SET quantity = 0 WHERE book_num = ?";
			rentalpstmt = con.prepareStatement(rentalSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			rentalpstmt.setInt(1, Integer.parseInt(bNum));
			count = rentalpstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true); // 자동커밋으로 변경

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rentalpstmt != null)
				try {
					rentalpstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			closeAll(rs, pstmt, con);
		}
	}

	// 책 대여 디테일
	public void rentalDetail(String bNum, String memId) {
		PreparedStatement renpstmt = null;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");

			String rSql = "INSERT INTO rental (rental_num, id) VALUES (seq.nextval, ?)"; // 렌탈(렌탈번호,아이디)
			sql = "INSERT INTO rentaldetails (rental_num, book_num, rental_date, return_date) VALUES (seq.currval, ?, sysdate, sysdate+7)";
			// 렌탈디테일(보여주는용도, 북넘버받음)

			renpstmt = con.prepareStatement(rSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			renpstmt.setString(1, memId);
			rs = renpstmt.executeQuery();

			pstmt.setInt(1, Integer.parseInt(bNum));
			rs = pstmt.executeQuery();

			con.commit();
			con.setAutoCommit(true); // 자동커밋으로 변경

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (renpstmt != null)
				try {
					renpstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			closeAll(rs, pstmt, con);
		}
	}

	// 책 대여 후 금액 보여주기
	public void showFee(String bNum) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			sql = "SELECT rental_num, b.book_num, rental_fee, rental_date, return_date\r\n"
					+ "FROM book b, rentaldetails r \r\n" + "WHERE b.book_num = r.book_num AND b.book_num = " + bNum
					+ " ORDER BY 1";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				view.sysTxt.append("대여 번호: " + rs.getString(1));
				view.sysTxt.append(", 책 번호: " + rs.getInt(2));
				view.sysTxt.append(", 대여료: " + rs.getString(3));
				view.sysTxt.append(", 대여 일자: " + rs.getString(4));
				view.sysTxt.append(", 반납 일자: " + rs.getString(5) + "\n");
			}
			view.sysTxt.append("대여 완료 되었습니다\n");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, con);
		}
	}

	// 책 반납
	public void memberRentalList(String loginId) {
		Connection con = null; // 오라클 서버 연결
		Statement stmt = null; // Query문 전송
		ResultSet rs = null; // SELECT문 결과

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT d.rental_num,book_num,rental_date,return_date "
					+ "FROM rentaldetails d,rental r WHERE r.rental_num=d.rental_num AND id='" + loginId + "'"
					+ " ORDER BY 1");
			while (rs.next()) {
				rs.getString(1);
				view.dataTxt.append("책번호: " + rs.getInt(2) + "  /  ");
				view.dataTxt.append("대여일: " + rs.getString(3) + "  /  ");
				view.dataTxt.append("반납예정일: " + rs.getString(4) + "\n");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void returnBook(String bNum) {
		PreparedStatement returnpstmt = null;
		int count = 0;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			sql = "SELECT * FROM rentaldetails WHERE book_num = ? ORDER BY 1";
			String returnSql = "UPDATE book SET quantity = 1 WHERE book_num = ?";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); // 데이터 동적 갱신
			returnpstmt = con.prepareStatement(returnSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			pstmt.setInt(1, Integer.parseInt(bNum));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				view.dataTxt.append("대여번호: " + rs.getString(1) + ", 책번호: " + rs.getInt(2) + ", 대여일: " + rs.getString(3)
						+ ", 반납일: " + rs.getString(4) + "\n");
			}
			;
			view.sysTxt.append("반납되었습니다\n");

			returnpstmt.setInt(1, Integer.parseInt(bNum));
			count = returnpstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true); // 자동커밋으로 변경

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (returnpstmt != null)
				try {
					returnpstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			closeAll(rs, pstmt, con);
		}
	}

	// 책 반납 디테일
	public void returnDetail(String bNum) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "comicBook", "comicBook");
			sql = "DELETE FROM rentaldetails WHERE book_num=?"; // 해당 book_num 관련 데이터가 삭제됨
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			pstmt.setInt(1, Integer.parseInt(bNum));
			rs = pstmt.executeQuery();

			con.commit();
			con.setAutoCommit(true); // 자동커밋으로 변경

		} catch (Exception e) {
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
