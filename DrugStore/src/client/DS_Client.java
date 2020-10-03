package client;

import java.net.*;
import java.io.*;

public class DS_Client {

	public static void main(String[] args) {

		Socket sock = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		boolean endflag = false;
		try {
			sock = new Socket("localhost", 10001);
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
			// 입장문 전송
			System.out.print("'입장'이라고 입력해주세요 >> ");
			String id = keyboard.readLine();
			pw.println(id);
			pw.flush();
			InputThread it = new InputThread(sock, br);
			it.start();
			String line = null;
			while ((line = keyboard.readLine()) != null) {
				pw.println(line);
				pw.flush();
			}
		} catch (Exception ex) {
			if (!endflag)
				System.out.println(ex);
		} finally {
			try {
				if (pw != null)
					pw.close();
			} catch (Exception ex) {
			}
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
			}
			try {
				if (sock != null)
					sock.close();
			} catch (Exception ex) {
			}
		} // finally
	} // main
} // class

class InputThread extends Thread {
	private Socket sock = null;
	private BufferedReader br = null;

	public InputThread(Socket sock, BufferedReader br) {
		this.sock = sock;
		this.br = br;
	}

	public void run() {
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.equals("꾱!")) {
					System.exit(0);
				}
				System.out.println(line);
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
			}
			try {
				if (sock != null)
					sock.close();
			} catch (Exception ex) {
			}
		}
	} // InputThread
}
