package com.topekox.pembayaran;

import org.junit.jupiter.api.Test;
import com.topekox.pembayaran.fcm.NotificationRequest;
import com.topekox.pembayaran.service.FCMService;

public class FCMServiceTest {

	// INFO: Sebelum menjalankan test ini
	// lokasi source "service-account.json" harus 
	// di isi manual/hardcode di FCMService
	@Test
	public void testFCMSendMessage() {
		FCMService service = new FCMService();
		service.initialize();	
		
		NotificationRequest request = new NotificationRequest();
		request.setTitle("Test FCM Message");
		request.setMessage("FCM Say...Hello World!!!");
		request.setTopic("foo");
		request.setToken("d7PDKnfFS327_0mjH7U9zg:APA91bHcMEoFD3EGFkRsGf2dSq6omuDIUNL719"
				+ "yOcxW1sobTYwts0yaZSi7Fdk9zwsYJdEE28FdmcFG5tCINF1p0xLnOEHDC_QwQD12I2xTS"
				+ "-qb8m-CbPPWuqflNLy1Wg9pA9xYhb1sI");
		
		service.messageToToken(request);
	}
	
}
