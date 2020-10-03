package drugstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class DS_CustomerView {
	DS_Method dsmethod = new DS_Method();
	DS_CustomerDB coDB = new DS_CustomerDB();

	// 고객 메뉴
	public void subLoop_customer(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		while (true) {
			customer_showIntro(idKey, map); // 고객 메뉴 출력
			String choice = dsmethod.getSelect(idKey, br, map); // 고객메뉴 선택
			switch (choice) {
			case "1":
				costomerJoin(idKey, br, map); // 고객 회원가입
				break;
			case "2":
				costomerLogin(idKey, br, map); // 고객 로그인
				break;
			case "3":
				dsmethod.broadCast(idKey, map, "==========이전메뉴로 돌아갑니다★==========");
				return;
			default:
				dsmethod.broadCast(idKey, map, "");
				dsmethod.broadCast(idKey, map, "잘못된 입력입니다.");
				break;
			}
		}
	}

	// 고객 메뉴 출력
	private void customer_showIntro(String idKey, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 고객 전용 페이지입니다 +++++++++++");
		dsmethod.broadCast(idKey, map, "1. 회원가입");
		dsmethod.broadCast(idKey, map, "2. 로그인");
		dsmethod.broadCast(idKey, map, "3. 이전메뉴로");
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");

	}

	// 회원 가입
	public void costomerJoin(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 회원가입 페이지입니다 ++++++++++++");
		coDB.CO_insert(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 로그인
	public void costomerLogin(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		boolean isTrue = false;

		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 로그인 페이지입니다 ++++++++++++");
		isTrue = coDB.CO_logIn(idKey, br, map);
		if (isTrue)
			customer_menu(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 로그인 후 고객메뉴
	private void customer_menu(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		while (true) {
			customerPage(idKey, map);// 메뉴 출력
			String choice = dsmethod.getSelect(idKey, br, map); // 메인메뉴 선택
			switch (choice) {
			case "1":
				salesListView(idKey, br, map); // 판매 상품 보기
				break;
			case "2":
				salesView(idKey, br, map); // 상품 구매
				break;
			case "3":
				pointSearchView(idKey, br, map); // 포인트 조회
				break;
			case "4":
				dsmethod.broadCast(idKey, map, "==========이전메뉴로 돌아갑니다★==========");
				return;
			default:
				dsmethod.broadCast(idKey, map, "");
				dsmethod.broadCast(idKey, map, "잘못된 입력입니다.");
				break;
			}
		}
	}

	// 로그인 후 고객 메뉴 출력
	private void customerPage(String idKey, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 고객 전용 페이지입니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "1. 판매 상품 보기");
		dsmethod.broadCast(idKey, map, "2. 상품 구매");
		dsmethod.broadCast(idKey, map, "3. 포인트 조회");
		dsmethod.broadCast(idKey, map, "4. 이전 메뉴로");
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 판매 상품 보기
	private void salesListView(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 판매상품을 검색합니다 +++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		coDB.salesList(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 상품 구매
	private void salesView(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++++ 상품을 구매합니다 ++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		coDB.sales(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 포인트 조회
	private void pointSearchView(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 포인트를 조회합니다 ++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		coDB.pointSearch(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}
}
