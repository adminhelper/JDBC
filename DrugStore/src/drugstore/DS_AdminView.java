package drugstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class DS_AdminView {
	DS_Method dsmethod = new DS_Method();
	DS_AdminDB adminDB = new DS_AdminDB();

	// 관리자 메뉴
	public void subLoop_admin(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		while (true) {
			admin_showIntro(idKey, map); // 관리자 메뉴 출력
			String choice = dsmethod.getSelect(idKey, br, map); // 관리자 메뉴 선택
			switch (choice) {
			case "1":
				checkCustomer(idKey, br, map);// 회원관리
				break;
			case "2":
				checkStock(idKey, br, map); // 재고확인
				break;
			case "3":
				checkIncome(idKey, br, map); // 수익확인
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

	// 관리자 메뉴 출력
	private void admin_showIntro(String idKey, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 관리자 전용 페이지입니다 +++++++++++");
		dsmethod.broadCast(idKey, map, "1. 회원 관리");
		dsmethod.broadCast(idKey, map, "2. 재고 관리");
		dsmethod.broadCast(idKey, map, "3. 수익 관리");
		dsmethod.broadCast(idKey, map, "4. 이전메뉴로");
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 회원관리 메뉴
	private void checkCustomer(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		while (true) {
			checkCustomer_showIntro(idKey, map); // 회원 관리 출력
			String choice = dsmethod.getSelect(idKey, br, map); // 회원 관리 선택
			switch (choice) {
			case "1":
				showAllCo(idKey, br, map); // 회원 모두 출력
				break;
			case "2":
				searchCo(idKey, br, map); // 회원 입력
				break;
			case "3":
				deleteCo(idKey, br, map); // 회원 삭제
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

	// 회원 관리 메뉴 출력
	private void checkCustomer_showIntro(String idKey, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 회원 관리 페이지입니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "1. 회원 전체 출력");
		dsmethod.broadCast(idKey, map, "2. 회원 검색");
		dsmethod.broadCast(idKey, map, "3. 회원삭제");
		dsmethod.broadCast(idKey, map, "4. 이전 메뉴로");
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 회원 모두 출력
	private void showAllCo(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 모든 회원을 출력합니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.showAllCustomer(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 회원 검색
	private void searchCo(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 회원을 검색합니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.searchCustomer(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 회원 삭제
	private void deleteCo(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 회원을 삭제합니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.deleteCustomer(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 재고 관리
	public void checkStock(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		while (true) {
			checkStock_showIntro(idKey, map); // 재고 관리 출력
			String choice = dsmethod.getSelect(idKey, br, map); // 재고 관리 선택
			switch (choice) {
			case "1":
				showAllStock(idKey, br, map); // 재고 모두 출력
				break;
			case "2":
				insertStock(idKey, br, map); // 재고 입력
				break;
			case "3":
				searchStock(idKey, br, map); // 재고 검색
				break;
			case "4":
				updateStock(idKey, br, map); // 재고 수정
				break;
			case "5":
				deleteStock(idKey, br, map); // 재고 삭제
				break;
			case "6":
				dsmethod.broadCast(idKey, map, "==========이전메뉴로 돌아갑니다★==========");
				return;
			default:
				dsmethod.broadCast(idKey, map, "");
				dsmethod.broadCast(idKey, map, "잘못된 입력입니다.");
				break;
			}
		}
	}

	// 재고 메뉴 출력
	private void checkStock_showIntro(String idKey, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 재고 관리 페이지입니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "1. 전체재고확인");
		dsmethod.broadCast(idKey, map, "2. 재고 입력");
		dsmethod.broadCast(idKey, map, "3. 재고 검색");
		dsmethod.broadCast(idKey, map, "4. 재고 수정");
		dsmethod.broadCast(idKey, map, "5. 재고 삭제");
		dsmethod.broadCast(idKey, map, "6. 이전 메뉴로");
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 재고 모두 출력
	private void showAllStock(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 모든 재고를 출력합니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.showAllStuff(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 재고 입력
	private void insertStock(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 새로운 재고를 입력합니다 +++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.insertStuff(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 재고 검색
	private void searchStock(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++++ 재고를 검색합니다 ++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.searchStuff(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 재고 수정
	private void updateStock(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++++ 재고를 수정합니다 ++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.updateStuff(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 재고 삭제
	private void deleteStock(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++++ 재고를 삭제합니다 ++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
		adminDB.deleteStuff(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 수익확인
	private void checkIncome(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		while (true) {
			checkIncome_showIntro(idKey, map); // 수익확인 출력
			String choice = dsmethod.getSelect(idKey, br, map); // 수익관리 선택
			switch (choice) {
			case "1":
				showpoint(idKey, br, map); // 포인트 조회
				break;
			case "2":
				showIncome(idKey, map); // 수익 조회
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

	// 수익 관리 출력
	private void checkIncome_showIntro(String idKey, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 수익 관리 페이지입니다 ++++++++++++");
		dsmethod.broadCast(idKey, map, "1. 회원별 적립금 조회");
		dsmethod.broadCast(idKey, map, "2. 수익 조회");
		dsmethod.broadCast(idKey, map, "3. 이전 메뉴로");
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 포인트 조회
	private void showpoint(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "+++++++++++ 포인트 조회 페이지입니다 +++++++++++");
		adminDB.pointAllSearch(idKey, br, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}

	// 수익 조회
	private void showIncome(String idKey, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(idKey, map, "");
		dsmethod.broadCast(idKey, map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(idKey, map, "++++++++++++ 수익조회 페이지입니다 ++++++++++++");
		adminDB.incomeSearch(idKey, map);
		dsmethod.broadCast(idKey, map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(idKey, map, "");
	}
}
