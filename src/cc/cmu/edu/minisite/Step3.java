package cc.cmu.edu.minisite;

import java.util.Deque;
import java.util.Map;

import io.undertow.server.HttpServerExchange;

import org.json.simple.JSONObject;

public class Step3 {

	public static JSONObject getStep3(HttpServerExchange exchange) {

		Map<String, Deque<String>> paras = exchange.getQueryParameters();
		try {
			String id = paras.get("id").getFirst();
			
			JSONObject response = new JSONObject();
		//	response.put("time", createdDate);
		//	response.put("url", imageUrl);
            return response;
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		return null;
	}

}
