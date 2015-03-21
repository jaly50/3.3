package cc.cmu.edu.minisite;


import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Step2 {
	static final byte[] tableName = Bytes.toBytes("t1"),
			 qualifier=Bytes.toBytes("friends"),
			family = Bytes.toBytes("f");
		//Go to find hbase-site.xml in class path
	static	Configuration config = HBaseConfiguration.create();
	static {
		config.set("hbase.zookeeper.quorum", "ip-172-31-60-135.ec2.internal:2181");
	}
	static	 HTableFactory factory = new HTableFactory();
   static final HTableInterface table = factory.createHTableInterface(config, tableName);
	static JSONObject getStep2(HttpServerExchange exchange) {

		Map<String, Deque<String>> paras = exchange.getQueryParameters();
		try {
			String id = paras.get("id").getFirst();

			JSONObject response = new JSONObject();
			String idsStr = queryHbase(id);
			String[] ids = idsStr.split(",");
			JSONArray f = new JSONArray();
			JSONArray friends = new JSONArray();
			for (String friendId:ids) {
			JSONObject friend = new JSONObject();
				friend.put("userid", friendId);
			friends.add(friend);
			}
			response.put("friends",friends);

            return response;
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return null;

	}	 
	
  

	private static String queryHbase(String id) {
		try {
			String rowKeyStr= id;
			final byte[] rowKey = Bytes.toBytes(rowKeyStr);
			Get get = new Get(rowKey);
			  Result result;
				result = table.get(get);
		        byte[] value = result.getValue(family, qualifier);
			return Bytes.toString(value);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}






}
