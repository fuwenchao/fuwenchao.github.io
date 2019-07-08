package com.ultrapower.uop.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.script.ScriptScoreFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Sort;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.net.InetAddresses;

// ElasticSearch 对查询时间过滤，如每周的某天的某个时段

public class EsQueryTimeScriptTest {
	// 是"{\"data\":[{\"host\":\"AAPS4PPD\",\"hostclass\":\"disk\",\"instance\":\"dm\",\"parameter\":\"dskread\"},{\"host\":\"AAPS4PPD\",\"hostclass\":\"disk\",\"instance\":\"dm\",\"parameter\":\"dskreadwrite\"},{\"host\":\"AAPS4PPD\",\"hostclass\":\"disk\",\"instance\":\"dm\",\"parameter\":\"dskwrite\"},{\"host\":\"AAPS4PPD\",\"hostclass\":\"disk\",\"instance\":\"0\",\"parameter\":\"dskbps\"},{\"host\":\"BASPPDA\",\"hostclass\":\"disk\",\"instance\":\"hdisk0\",\"parameter\":\"dskreadwrite\"},{\"host\":\"BASPPDA\",\"hostclass\":\"disk\",\"instance\":\"hdisk0\",\"parameter\":\"dsktps\"},{\"host\":\"BASPPDA\",\"hostclass\":\"disk\",\"instance\":\"hdisk0\",\"parameter\":\"dskwrite\"},{\"host\":\"BASPPDA\",\"hostclass\":\"disk\",\"instance\":\"hdisk10\",\"parameter\":\"dskreadwrite\"}],\"starttime\":1488490695825,\"endtime\":1489389341324,\"interval\":\"8784s\"}:"
	private static final Logger log = LoggerFactory.getLogger(EsQueryTimeScriptTest.class);
	String str = "[{\"ip\":\"197.3.155.136\",\"port\":9300},{\"ip\":\"197.3.155.137\",\"port\":9300},{\"ip\":\"197.3.155.138\",\"port\":9300},{\"ip\":\"197.3.155.139\",\"port\":9300}]";
	private JSONArray ips = JSONArray.parseArray(str);

	public static void main(String[] args) {
		EsQueryTimeScriptTest et = new EsQueryTimeScriptTest();
		Client client = et.getNewConnection();
		SearchRequestBuilder srb = client.prepareSearch();
		srb.setIndices("perform_data_2017*").setTypes("patrol_perform");
		BoolQueryBuilder bqb = new BoolQueryBuilder();
		
		
		DateRangeBuilder dr = new DateRangeBuilder("a");
		dr.field("time").format("HH:mm:ss").addRange("08:00:00", "09:00:00");
		//srb.addAggregation(dr);
		
		DateHistogramBuilder db = new DateHistogramBuilder("dbnew");
		db.field("time").interval(86400000).offset("+8h");
		//srb.addAggregation(db);
		
		//srb.addAggregation(AggregationBuilders.filter("f").subAggregation(dr));

		Map<String, Object> params = new HashMap<>();
		params.put("8", 8);
		params.put("11", 11);
		params.put("13", 13);
		params.put("17", 17);
		
		JSONArray rangearr = new JSONArray();
		rangearr.add("3-6");
		rangearr.add("7-8");
		rangearr.add("8-11");
		rangearr.add("12-14");
		
		Longs l = new Longs(null);
		l.getDate().toDateTime().withZone(DateTimeZone.forID("Asia/BeiJing")).toLocalDateTime().getDayOfMonth();
		
		String scripts = "";
		String method = "";
		switch ("hourOfDay") {
		case "hourOfDay":
			method = "getHourOfDay()";
		case "dayOfWeek":
			method = "getDayOfWeek()";
		case "dayOfYear":
			method = "getDayOfYear()";
		case "monthOfYear":
			method = "getMonthOfYear()";
		case "dayOfMonth":
			method = "getDayOfMonth()";
		default:
			method = "";
			for (int i = 0; 0 < rangearr.size(); i++) {
				String script ="";
				String[] range = rangearr.getString(i).split("-");
				if(Integer.valueOf(range[0])>Integer.valueOf(range[1])){
					script = "doc['time'].getDate().toDateTime().withZone(DateTimeZone.forID(\"Asia/Shanghai\")).toLocalDateTime()."+method+">="+range[0]
							+"&doc['time'].getDate().toDateTime().withZone(DateTimeZone.forID(\"Asia/Shanghai\")).toLocalDateTime()."+method+"()<="+range[1];
					scripts = script +" |"+scripts;
				}else{
					script = "doc['time'].getDate().toDateTime().withZone(DateTimeZone.forID(\"Asia/Shanghai\")).toLocalDateTime()."+method+"()>="+range[0]
							+"|doc['time'].getDate().toDateTime().withZone(DateTimeZone.forID(\"Asia/Shanghai\")).toLocalDateTime()."+method+"()<="+range[1];
					scripts = script +" |"+scripts;
				}
			}
			
			/*for (int i = 0; 0 < rangearr.size(); i++) {
				String script ="";
				String[] range = rangearr.getString(i).split("-");
				if(Integer.valueOf(range[0])<8&&Integer.valueOf(range[1])<8){
					int a = 24 + Integer.valueOf(range[0]) -8;
					int b = 24 + Integer.valueOf(range[1]) -8;
					script = "doc['time'].getDate().getHourOfDay()>="+a+"&doc['time'].getDate().getHourOfDay()<="+b;
				}else if(Integer.valueOf(range[0])<8&&Integer.valueOf(range[1])==8){
					int a = 24 + Integer.valueOf(range[0]) -8;
					script = "doc['time'].getDate().getHourOfDay()>="+a+"|doc['time'].getDate().getHourOfDay()==0";
				}
				else if(Integer.valueOf(range[0])<8&&Integer.valueOf(range[1])>8){
					int a = 24 + Integer.valueOf(range[0]) -8;
					int b = Integer.valueOf(range[1]) -8;
					script = "doc['time'].getDate().getHourOfDay()>="+a+"&doc['time'].getDate().getHourOfDay()<=23|doc['time'].getDate().getHourOfDay()<="+b;
				}else if(Integer.valueOf(range[0])==8&&Integer.valueOf(range[1])==8){
					script = "doc['time'].getDate().getHourOfDay()==0";
				}else if(Integer.valueOf(range[0])==8&&Integer.valueOf(range[1])>8){
					int b = Integer.valueOf(range[1]) -8;
					script = "doc['time'].getDate().getHourOfDay()>=0&doc['time'].getDate().getHourOfDay()<="+b;
				}else {
					int a = Integer.valueOf(range[0]) -8;
					int b = Integer.valueOf(range[1]) -8;
					script = "doc['time'].getDate().getHourOfDay()>="+a+"&doc['time'].getDate().getHourOfDay()<="+b;
				}
				scripts = script +" |"+scripts;
			}*/
		}
		
		String inlineScript = scripts.substring(0, scripts.length()-1);
		Script script = new Script(inlineScript, ScriptType.INLINE, "groovy", params);

		bqb.filter(QueryBuilders.scriptQuery(script));
		//bqb.must(QueryBuilders.scriptQuery(script));
		
		//ScoreFunctionBuilder sfb = ScoreFunctionBuilders.scriptFunction(script);
		
		String quaryinfo ="host:\"ATMA3PPD\" AND parameter: \"CPUUserTime\" AND instance:\"CPU\"";
		QueryStringQueryBuilder qs = QueryBuilders.queryStringQuery(quaryinfo);		
		bqb.must(qs);
		
		QueryBuilder date = QueryBuilders.rangeQuery("time").gte("now-1d").lte("now");
		bqb.must(date);
		
		srb.setQuery(bqb);
		
		//TermsBuilder ta = AggregationBuilders.terms("instance").field("instance");
		//srb.addAggregation(ta);
		
		srb.setSize(1000).addSort("time", SortOrder.ASC);
		System.out.println(srb);
		SearchResponse searchresponse = srb.execute().actionGet();
		JSONArray b = JSONObject.parseObject(searchresponse.toString()).getJSONObject("hits").getJSONArray("hits");
		System.out.println(b.size());
		for(int n = 0; n<=b.size();n++){
			JSONObject c = b.getJSONObject(n);
			System.out.println(c.getJSONObject("_source").getString("time"));
		}
		
	}

	public Client getNewConnection() {
		Settings settings = Settings.builder()
				.put("client.transport.ping_timeout", "50s")
				.put("client.transport.sniff", true)
				.put("cluster.name", "automaticservice").build();
		Client client = null;
		TransportClient transclient = TransportClient.builder()
				.settings(settings).build();
		for (int i = 0; i < ips.size(); i++) {
			JSONObject obj = ips.getJSONObject(i);
			try {
				transclient.addTransportAddress(new InetSocketTransportAddress(
						InetAddresses.forString(obj.getString("ip")), obj
								.getInteger("port")));
			} catch (Exception e) {
				log.error("getNewConnection Exception_" + e.getMessage(), e);
			}
		}
		client = transclient;
		return client;

	}
}
