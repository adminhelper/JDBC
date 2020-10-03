package drugstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;

public class DS_CustomerDB {
	DS_Method dsmethod = new DS_Method();

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = null;

	PreparedStatement pstmt1 = null;
	PreparedStatement pstmt2 = null;
	String sql1 = null;
	String sql2 = null;
	String loginId = null; // 회원 로그인 ID
	int st_id = 0; // 구매할 상품번호
	int selNum = 0; // 몇개 구매할건지

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 회원가입
	public synchronized void CO_insert(String idKey, BufferedReader br, HashMap<String, PrintWriter> map)
			throws IOException {
		PreparedStatement defaultpstmt = null;
		int id = 1; // 회원번호

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
			sql = "INSERT INTO customer(co_id, co_loginid, co_password, co_name, co_birth, co_phonenum, co_point) VALUES (?, ?, ?, ?, ?, ?, '20')";
			sql1 = "SELECT co_loginid FROM customer WHERE co_loginid = ?";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); // 저장용
			pstmt1 = con.prepareStatement(sql1, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); // 중복찾는용

			String defaultSql = "SELECT * FROM customer";
			defaultpstmt = con.prepareStatement(defaultSql, ResultSet.TYPE_SCROLL_SENSITIVE, /* 커서 양방향 이동 */
					ResultSet.CONCUR_UPDATABLE);

			con.setAutoCommit(false); // 수동커밋으로 변경

			String loginId = null;
			String password = null;
			String name = null;
			String birth = null;
			String phoneNum = null;

			rs = defaultpstmt.executeQuery();
			rs.last();
			id = rs.getRow() + 1;

			while (true) {
				dsmethod.broadCast(idKey, map, "아이디를 입력하세요");
				loginId = dsmethod.getScanner(br);
				dsmethod.broadCast(idKey, map, "비밀번호를 입력하세요");
				password = dsmethod.getScanner(br);
				dsmethod.broadCast(idKey, map, "이름을 입력하세요.");
				name = dsmethod.getScanner(br);
				dsmethod.broadCast(idKey, map, "생년월일을 입력하세요.");
				birth = dsmethod.checkDate(idKey, br, map);
				dsmethod.broadCast(idKey, map, "핸드폰번호를 입력하세요.");
				phoneNum = dsmethod.getScanner(br);

				pstmt1.setString(1, loginId);
				rs = pstmt1.executeQuery();
				rs.last();

				if (rs.getRow() < 1) {
					break;
				}
				dsmethod.broadCast(idKey, map, "아이디가 중복되었습니다.");
				dsmethod.broadCast(idKey, map, "다시 입력해주세요.");
			} // while문 끝

			pstmt.setInt(1, id);
			pstmt.setString(2, loginId);
			pstmt.setString(3, password);
			pstmt.setString(4, name);
			pstmt.setString(5, birth);
			pstmt.setString(6, phoneNum);

			rs.last();
			pstmt.executeQuery();

			dsmethod.broadCast(idKey, map, "회원가입 성공했습니다.");
			con.commit();
			con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, pstmt1, con);
		}
	}

	// 회원로그인
	public boolean CO_logIn(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		boolean isTrue = false;

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
			sql = " SELECT co_loginid, co_password FROM customer ";
			pstmt = con.prepareCall(sql, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
					ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			dsmethod.broadCast(idKey, map, "아이디를 입력하세요.");
			loginId = dsmethod.getScanner(br);
			;
			dsmethod.broadCast(idKey, map, "비밀번호를 입력하세요.");
			String password = dsmethod.getScanner(br);
			;

			while (rs.next()) {
				if (rs.getString(1).equals(loginId)) {
					isTrue = true;
					break;
				}
			}

			if (isTrue) {
				if (!rs.getString(2).equals(password)) {
					dsmethod.broadCast(idKey, map, "비밀번호가 틀렸습니다.");
				} else if (rs.getString(2).equals(password)) {
					dsmethod.broadCast(idKey, map, "로그인 성공했습니다.");
					return true;
				}
			}

			if (!isTrue) {
				dsmethod.broadCast(idKey, map, "존재하지 않는 아이디입니다.");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, pstmt1, con);
		}
		return false;
	}

	// 판매목록
	public synchronized void salesList(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
			sql = "SELECT * FROM stock ORDER BY ST_ID ASC";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
					ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dsmethod.broadCast(idKey, map,
						"상품 번호 : " + rs.getInt("st_id") + ", 판매 상품 이름 : " + rs.getString("st_name") + ", 가격 : "
								+ rs.getInt("st_price") + "원, 수량 : " + rs.getInt("st_num") + "개");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, pstmt, pstmt1, con);
		}
	}

//	// 구매
//	public synchronized void sales(String idKey, BufferedReader br, HashMap<String, PrintWriter> map)
//			throws IOException {
//		salesList(idKey, br, map);
//		try {
//			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
//			sql = "UPDATE stock SET st_num = ? WHERE st_id = ?";
//			sql1 = "SELECT * FROM stock";
//			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
//					ResultSet.CONCUR_UPDATABLE);
//			pstmt1 = con.prepareStatement(sql1, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
//					ResultSet.CONCUR_UPDATABLE);
//			rs = pstmt1.executeQuery();
//
////			con.setAutoCommit(false); // 수동커밋으로 변경
//
//			// 수익확인용 추가
//			// --------------------------------------------------------------------//
//			PreparedStatement incomePstmt = null; // 수익확인용 추가
//			PreparedStatement customerPstmt = null; // 수익확인용 추가
//
//			String customerSql = "SELECT * FROM customer WHERE co_loginid = ?";
//			customerPstmt = con.prepareStatement(customerSql);
//			String incomeSql = "INSERT INTO income(in_id, in_money) VALUES (?, ?)";
//			incomePstmt = con.prepareStatement(incomeSql);
//			// --------------------------------------------------------------------------------//
//
//			rs.last();
//			int checkNum = rs.getRow();
//			dsmethod.broadCast(idKey, map, "");
//			while (true) {
//				dsmethod.broadCast(idKey, map, "구매하실 상품 번호를 입력해주세요. (구매를 취소하시려면 0을 입력해주세요.)");
//				st_id = dsmethod.checkException(idKey, br, map);
//				if (st_id == 0) {
//					dsmethod.broadCast(idKey, map, "구매를 취소합니다");
//					return;
//				}
//
//				if (st_id <= checkNum) {
//					break;
//				}
//
//				dsmethod.broadCast(idKey, map, "해당하는 상품이 없습니다.");
//				continue;
//			}
//
//			rs.beforeFirst();
//			int oneNum = 0; // 수익확인용 상품가격 저장할 변수
//			while (rs.next()) {
//				if (st_id == rs.getInt(1)) {
//					break;
//				}
//			}
//			while(true) {
//			if (rs.getInt("st_num") == 0) {
//				dsmethod.broadCast(idKey, map, "구매가능한 상품이 없습니다.");
//				return;
//			} else {
//				dsmethod.broadCast(idKey, map, "");
//				dsmethod.broadCast(idKey, map,
//						"선택하신 상품은 " + rs.getString("st_name") + "\n" + rs.getInt("st_price") + "원 입니다");
//				break;
//			}
//			}
//			oneNum = rs.getInt("st_price");
//
//			while (true) {
//				dsmethod.broadCast(idKey, map, "구매하실 수량을 입력해주세요. (구매를 취소하시려면 0을 입력해주세요.)");
//				selNum = dsmethod.checkException(idKey, br, map);
//
//				if (selNum == 0) {
//					dsmethod.broadCast(idKey, map, "구매를 취소합니다");
//					return;
//				}
//
//				if (rs.getInt("st_num") < selNum) {
//					dsmethod.broadCast(idKey, map, "수량이 부족합니다 " + rs.getInt("st_num") + "개 이하로 구매 가능합니다.");
//					continue;
//
//				}
//				// 바로 밑에서 메서드를 타고 rs, pstmt에 변화가 생겨서
//				// 미리 업데이트 함
//				int num = rs.getInt("st_num");
//				num = rs.getInt("st_num") - selNum;
//				pstmt.setInt(1, num);
//				pstmt.setInt(2, st_id);
//				pstmt.executeUpdate();
//
//				dsmethod.broadCast(idKey, map, selNum + "개 " + (oneNum * selNum) + "원 입니다.");
//				dsmethod.broadCast(idKey, map, "포인트를 사용하시겠습니까? (Y / N)");
//				String yesNo = dsmethod.getScanner(br);
//
//				if (yesNo.equalsIgnoreCase("y")) {
//					pointSel(idKey, br, map);
//					dsmethod.broadCast(idKey, map, selNum + "개 구매완료했습니다.");
//					break;
//				} else if (yesNo.equalsIgnoreCase("n")) {
//					pointpoint(idKey, map);
//					dsmethod.broadCast(idKey, map, selNum + "개 구매완료했습니다.");
//					break;
//				} else {
//					dsmethod.broadCast(idKey, map, "잘못 입력하셨습니다.");
//					continue;
//				}
//			}
//			customerPstmt.setString(1, loginId);
//			rs = customerPstmt.executeQuery();
//			rs.next();
//
//			int loginidNum = rs.getInt(1); // 회원번호 찾아주는 변수
//			int money = oneNum * selNum; // 가격 * 개수 (총 금액)
//
//			incomePstmt.setInt(1, loginidNum);
//			incomePstmt.setInt(2, money);
//			incomePstmt.executeQuery();
//
////			con.commit();
////			con.setAutoCommit(true); // 원래 자동커밋으로 돌리자
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			closeAll(rs, pstmt, pstmt1, con);
//		}
//	}

	
	
	
	
	
	// 구매
		public synchronized void sales(String idKey, BufferedReader br, HashMap<String, PrintWriter> map)
				throws IOException {
			salesList(idKey, br, map);
			try {
				con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
				sql = "UPDATE stock SET st_num = ? WHERE st_id = ?";
				sql1 = "SELECT * FROM stock";
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
						ResultSet.CONCUR_UPDATABLE);
				pstmt1 = con.prepareStatement(sql1, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
						ResultSet.CONCUR_UPDATABLE);
				rs = pstmt1.executeQuery();

//				con.setAutoCommit(false); // 수동커밋으로 변경

				// 수익확인용 추가
				// --------------------------------------------------------------------//
				PreparedStatement incomePstmt = null; // 수익확인용 추가
				PreparedStatement customerPstmt = null; // 수익확인용 추가

				String customerSql = "SELECT * FROM customer WHERE co_loginid = ?";
				customerPstmt = con.prepareStatement(customerSql);
				String incomeSql = "INSERT INTO income(in_id, in_money) VALUES (?, ?)";
				incomePstmt = con.prepareStatement(incomeSql);
				// --------------------------------------------------------------------------------//

				rs.last();
				int checkNum = rs.getRow();
				dsmethod.broadCast(idKey, map, "");
				while (true) {
					dsmethod.broadCast(idKey, map, "구매하실 상품 번호를 입력해주세요.");
					st_id = dsmethod.checkException(idKey, br, map);
					if (st_id <= checkNum) {
						break;
					}
					dsmethod.broadCast(idKey, map, "해당하는 상품이 없습니다.");
				}

				rs.beforeFirst();
				int oneNum = 0; // 수익확인용 상품가격 저장할 변수
				while (rs.next()) {
					if (st_id == rs.getInt(1)) {
						dsmethod.broadCast(idKey, map, "");
						dsmethod.broadCast(idKey, map,
								"선택하신 상품은 " + rs.getString("st_name") + "\n" + rs.getInt("st_price") + "원 입니다");
						oneNum = rs.getInt("st_price");
						break;
					}
				}

				dsmethod.broadCast(idKey, map, "구매하실 수량을 입력해주세요. (구매를 취소하시려면 0을 입력해주세요.)");
				selNum = dsmethod.checkException(idKey, br, map);

				if (selNum == 0) {
					dsmethod.broadCast(idKey, map, "구매를 취소합니다");
					return;
				}
				dsmethod.broadCast(idKey, map, selNum + "개 " + (oneNum * selNum) + "원 입니다.");
				
				if(rs.getInt("st_num")==0) {
					dsmethod.broadCast(idKey, map, "구매가능한 상품이 없습니다.");
					return;
				}
				else if (rs.getInt("st_num") < selNum) {
					dsmethod.broadCast(idKey, map, "수량이 부족합니다 " + rs.getInt("st_num") + "개 이하로 구매 가능합니다.");
					return;
				}

				// 바로 밑에서 메서드를 타고 rs, pstmt에 변화가 생겨서
				// 미리 업데이트 함
				int num = rs.getInt("st_num");
				num = rs.getInt("st_num") - selNum;
				pstmt.setInt(1, num);
				pstmt.setInt(2, st_id);
				pstmt.executeUpdate();

				dsmethod.broadCast(idKey, map, "포인트를 사용하시겠습니까? (Y / N)");
				String yesNo = dsmethod.getScanner(br);

				if (yesNo.equalsIgnoreCase("y")) {
					pointSel(idKey, br, map);
					dsmethod.broadCast(idKey, map, selNum + "개 구매완료했습니다.");
				} else if (yesNo.equalsIgnoreCase("n")) {
					pointpoint(idKey, map);
					dsmethod.broadCast(idKey, map, selNum + "개 구매완료했습니다.");
				} else {
					dsmethod.broadCast(idKey, map, "잘못 입력하셨습니다.");
				}

				customerPstmt.setString(1, loginId);
				rs = customerPstmt.executeQuery();
				rs.next();

				int loginidNum = rs.getInt(1); // 회원번호 찾아주는 변수
				int money = oneNum * selNum; // 가격 * 개수 (총 금액)

				incomePstmt.setInt(1, loginidNum);
				incomePstmt.setInt(2, money);
				incomePstmt.executeQuery();

//				con.commit();
//				con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeAll(rs, pstmt, pstmt1, con);
			}
		}


	
	
	
	
	// 나
	public synchronized void pointSel(String idKey, BufferedReader br, HashMap<String, PrintWriter> map)
			throws IOException {
		int a = 0; // 상품 가격 받을 애
		int point = 0; // 내 포인트
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");

			sql = "SELECT * FROM customer JOIN stock ON co_id = st_id WHERE co_loginid = ?";

			sql1 = "UPDATE customer SET co_point = ? WHERE co_loginid = ?";
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
					ResultSet.CONCUR_UPDATABLE);
			pstmt1 = con.prepareStatement(sql1, ResultSet.TYPE_SCROLL_SENSITIVE, // 커서 양방향 이동
					ResultSet.CONCUR_UPDATABLE);

			while (true) {
				dsmethod.broadCast(idKey, map, "사용할 포인트 금액을 입력하세요.");
				int inputPoint = dsmethod.checkException(idKey, br, map);

				pstmt.setString(1, loginId);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					if (rs.getString("co_loginid").equals(loginId)) {
						a = rs.getInt("st_price");
						point = rs.getInt("co_point");
					}
				}

				// 음수 입력 못하게 예외처리하기
				if (inputPoint < 0) {
					dsmethod.broadCast(idKey, map, "잘못된 입력입니다.");
					continue;
				}

				if (inputPoint <= point) {
					if (inputPoint <= a * selNum) {
						point = point - inputPoint;
						pstmt1.setInt(1, point);
						pstmt1.setString(2, loginId);
						pstmt1.executeUpdate();
						rs = pstmt1.executeQuery();

						con.commit();
						con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

						dsmethod.broadCast(idKey, map, "잔여 포인트는 " + point + "원 입니다.");
						break;
					} else {
						dsmethod.broadCast(idKey, map, a * selNum + "원 이하로 입력해주세요.");
					}
				} else {
					dsmethod.broadCast(idKey, map, "포인트가 부족합니다" + point + "원 이하로 입력해주세요.");
				}
			} // outer loop

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 메서드 빠져나가서도 rs, pstmt, con을 쓰기 때문에 닫아주지 않았음
	}

	// 포인트 지급
	public synchronized void pointpoint(String idKey, HashMap<String, PrintWriter> map) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
			sql = "UPDATE customer SET co_point = ? WHERE co_loginid = ?";
			sql1 = "SELECT * FROM customer";
			sql2 = "SELECT * FROM stock";
			pstmt = con.prepareStatement(sql);
			pstmt1 = con.prepareStatement(sql1);
			pstmt2 = con.prepareStatement(sql2);

			rs = pstmt1.executeQuery();

			con.setAutoCommit(false); // 수동커밋으로 변경

			int nowPoint = 0; // 회원의 현재 포인트
			int getPoint = 0; // 회원이 구매해서 받을 포인트 (구매가격의 10퍼센트)
			int point = 0; // 회원의 구매 후 최종 포인트

			while (rs.next()) {
				if (rs.getString("co_loginid").equals(loginId))
					nowPoint = rs.getInt("co_point");
			}

			rs = pstmt2.executeQuery();

			while (rs.next()) {
				if (rs.getInt("st_id") == st_id)
					getPoint = rs.getInt("st_price") / 10;
			}

			getPoint *= selNum;
			point = nowPoint + getPoint;

			dsmethod.broadCast(idKey, map, getPoint + "포인트 적립되었습니다.");
			dsmethod.broadCast(idKey, map, "현재 포인트는 " + point + "원 입니다.");
			pstmt.setInt(1, point);
			pstmt.setString(2, loginId);
			pstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true); // 원래 자동커밋으로 돌리자

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 나
	// 고객 포인트 조회
	public synchronized void pointSearch(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "drugstore", "drugstore");
			sql = "SELECT * FROM customer";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getString("co_loginid").equals(loginId)) {
					dsmethod.broadCast(idKey, map, rs.getString("co_loginId") + "님의 포인트 : " + rs.getString("co_point"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void closeAll(ResultSet rs, PreparedStatement pstmt, PreparedStatement pstmt2, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (pstmt1 != null)
				pstmt1.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
