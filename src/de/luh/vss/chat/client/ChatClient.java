package de.luh.vss.chat.client;

import java.io.IOException;

public class ChatClient {

	public static void main(String... args) {
		try {
			new ChatClient().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException {
		// implement your chat client logic here
	}

}