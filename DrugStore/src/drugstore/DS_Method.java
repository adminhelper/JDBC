package drugstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class DS_Method {
	public HashMap<String, PrintWriter> map = new HashMap<String, PrintWriter>();

	// 제일 중요한 브로드캐스트
	public void broadCast(String idKey, HashMap<String, PrintWriter> map, String msg) { // 서버에서 클라이언트로 메세지 전송해주는 메서드
		synchronized (map) { // HashMap인 map에 여러 쓰레드가 접근하므로 synchronized 해줌.
			try {
				map.get(idKey).println(msg); // 각 값에 메세지를 입력
				map.get(idKey).flush(); // 전송해줌.
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}

	// 선택 메소드
	public String getSelect(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		broadCast(idKey, map, "");
		broadCast(idKey, map, "원하시는 메뉴의 숫자를 입력해주세요 >>>>>>>>>> ");
		String choice = br.readLine();
		broadCast(idKey, map, "");
		return choice;
	}

	public String getScanner(BufferedReader br) throws IOException {
		String str = br.readLine();
		return str;
	}

	public int getScannerInt(BufferedReader br) throws IOException {
		int num = Integer.parseInt(br.readLine());
		return num;
	}

	// 예외처리 메소드
	public int checkException(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		int num = 0;
		boolean bl = true;
		while (bl) {
			try {
				num = getScannerInt(br); // 입금금액 입력
				if (num < 0) { // 만약 0보다 작을 경우 출력
					broadCast(idKey, map, "-단위는 입력이 불가합니다.");
					broadCast(idKey, map, "다시 입력하세요.");
					continue; // 다시 while문 처음으로
				}
				bl = false; // 정확한 금액 입력시 rd가 false가 되면서 while문 탈출
			} catch (Exception e) {
				br.readLine(); // 만약 String 입력할 경우 출력
				broadCast(idKey, map, "");
				broadCast(idKey, map, "정상적인 숫자를 입력하세요.");
				broadCast(idKey, map, "다시 입력하세요.");
				continue; // 다시 처음으로
			}
		}
		return num; // while문 나와서 숫자값 리턴.
	}

	// 예외처리 메소드
	public String checkDate(String idKey, BufferedReader br, HashMap<String, PrintWriter> map) throws IOException {
		String date = null;
		boolean bl = true;
		while (bl) {
			date = br.readLine(); // 입금금액 입력
			if (date.indexOf("-") != 4 || date.indexOf("-") + 3 != 7) { // 만약 0보다 작을 경우 출력
				broadCast(idKey, map, "YYYY-MM-DD 형태로 입력해주세요.");
				broadCast(idKey, map, "다시 입력하세요.");
				continue; // 다시 while문 처음으로
			}
			bl = false; // 정확한 금액 입력시 rd가 false가 되면서 while문 탈출
		}
		return date; // while문 나와서 숫자값 리턴.
	}
}
