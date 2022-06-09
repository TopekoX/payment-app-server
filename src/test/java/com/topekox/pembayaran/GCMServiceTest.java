package com.topekox.pembayaran;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.topekox.pembayaran.service.GCMService;

public class GCMServiceTest {
	
	@Test
	public void testGCMSendMessage() {
		GCMService service = new GCMService();
		Map<String, Object> data = new HashMap<>();
		data.put("body", "Halo From GCM");
		data.put("title", "Demo GCM");
		service.kirimMessage("d7PDKnfFS327_0mjH7U9zg:APA91bHcMEoFD3EGFkRsGf2dSq6omuDIUNL71"
				+ "9yOcxW1sobTYwts0yaZSi7Fdk9zwsYJdEE28FdmcFG5tCINF1p0xLnOEHDC_QwQD12I2xTS-qb8m"
				+ "-CbPPWuqflNLy1Wg9pA9xYhb1sI", data);
	}

}
