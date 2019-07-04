package client.frame;

import client.user.UserClient;

public class Restart {
	public static void restartApplication() {
		UserClient.toServer.close();
	}
}
