package Client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class server_test {
	@Test
	public void givenSFTP_client_whenServerResponsdsWhenStarted_thenCorrect() {
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			String response = client.sendMessage("hello server");
			assertEquals("hello client", response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
