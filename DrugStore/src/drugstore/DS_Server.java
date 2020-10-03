package drugstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class DS_Server {
	final static int PORT = 10001;

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		DS_Method dsmethod = new DS_Method();
		// 해쉬맵을 선언해준다.
		try {
			ServerSocket server = new ServerSocket(PORT);
			while (true) {
				// 1. 클라이언트 접속 대기
				System.out.println("클라이언트 접속 대기...");
				Socket sock = server.accept();
				// 2. 클라이언트 (송수신) 담당 스레드 생성하여 전담
				EchoThread echoThread = new EchoThread(sock, dsmethod.map);
				// 매개변수로 sock과 map을 전달
				echoThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}

class EchoThread extends Thread {
	DS_Method dsmethod = new DS_Method();
	DS_AdminView adminView = new DS_AdminView();
	DS_CustomerView coView = new DS_CustomerView();
	private Socket sock; // 클라이언트와 연결된 소켓 객체

	public EchoThread(Socket sock, HashMap<String, PrintWriter> map) {
		this.sock = sock;
		dsmethod.map = map;
	}

	// 메인 메뉴 출력
	public void showIntro(String inIdkey, BufferedReader br, HashMap<String, PrintWriter> map) {
		dsmethod.broadCast(inIdkey, dsmethod.map, "");
		dsmethod.broadCast(inIdkey, dsmethod.map, "+++++++++++ 코로나19 DRUGSTORE +++++++++++");
		dsmethod.broadCast(inIdkey, dsmethod.map, "1. 관리자 전용");
		dsmethod.broadCast(inIdkey, dsmethod.map, "2. 고객 전용");
		dsmethod.broadCast(inIdkey, dsmethod.map, "3. 프로그램 종료");
		dsmethod.broadCast(inIdkey, dsmethod.map, "+++++++++++++++++++++++++++++++++++++++++");
		dsmethod.broadCast(inIdkey, dsmethod.map, "");
//		dsmethod.broadCast(id,dsmethod.map,dsmethod.map,"꾱");
	}

	public void run() {

		try {
			// 1. 클라이언트 주소 얻기
			InetAddress inetAddr = sock.getInetAddress();
			System.out.println(inetAddr.getHostAddress() + "로부터 접속했습니다.");
			// 2. 송신 stream 얻기
			OutputStream out = sock.getOutputStream();
			OutputStreamWriter outW = new OutputStreamWriter(out);
			PrintWriter pw = new PrintWriter(outW);
			// 3. 수신 stream 얻기
			InputStream in = sock.getInputStream();
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inR);
			// 4. 수신 후 송신 (echo)

			boolean exit = true;

			String ipip = br.readLine();
			String inIdkey = inetAddr.getHostAddress();
			System.out.println("[System] ip -" + inIdkey + "- 고객이 입장하셨습니다.");
			dsmethod.map.put(inIdkey, pw);
			dsmethod.broadCast(inIdkey, dsmethod.map, "");
			dsmethod.broadCast(inIdkey, dsmethod.map, "**********코로나 19 DRUGSTORE에 오신 것을 환영합니다!**********");

			while (exit) {
				showIntro(inIdkey, br, dsmethod.map); // 메인메뉴 출력
				String choice = dsmethod.getSelect(inIdkey, br, dsmethod.map);// 메인메뉴 선택
				switch (choice) {
				case "1":
					adminView.subLoop_admin(inIdkey, br, dsmethod.map); // 관리자 메뉴
					break;
				case "2":
					coView.subLoop_customer(inIdkey, br, dsmethod.map); // 고객 메뉴
					break;
				case "3":
					dsmethod.broadCast(inIdkey, dsmethod.map, "==========프로그램을 종료합니다★==========");
					dsmethod.broadCast(inIdkey, dsmethod.map, "꾱!");
					exit = false;
					break;
				default:
					dsmethod.broadCast(inIdkey, dsmethod.map, "");
					dsmethod.broadCast(inIdkey, dsmethod.map, "잘못된 입력입니다.");
					break;
				}
			}
			System.out.println(inetAddr.getHostAddress() + "가 접속을 끊었습니다.");
			pw.close();
			outW.close();
			out.close();
			br.close();
			inR.close();
			in.close();
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}