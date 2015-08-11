package idv.jiaming.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.types.ObjectId;
import org.apache.hadoop.hbase.client.HTable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.jiaming.models.CommentObject;
import idv.jiaming.models.POIObject;
import idv.jiaming.models.SourceEnum;

public class PracticeThreeMapper extends Mapper<Object, Text, ImmutableBytesWritable, Put>{

	private String poiTableName = "POITable";
	private String commentTableName = "CommentTable";
	private byte[] poiFamily = "poi_family".getBytes();
	private byte[] commentFamily = "comment_family".getBytes();
    private HTable poiTable = null;
    private HTable commentTable = null;
	
    @Override
    protected void setup(Context context)throws IOException, InterruptedException {
    	poiTable = new HTable(context.getConfiguration(),poiTableName);
    	commentTable = new HTable(context.getConfiguration(),commentTableName);    	
    }
    
    @Override
    protected void cleanup(Context context)throws IOException, InterruptedException {    
    	poiTable.flushCommits();
    	commentTable.flushCommits();
    }
    
	public void map(Object key, Text value, Context context){
		String html = value.toString();		
		POIObject poiObj = retrievePOIInfo(html);
		
		if(poiObj != null){
			// Store the POI article to table "POITable"	
			try{
				// Using the title md5 as the row key.
				byte[] poiRowkey = DigestUtils.md5(poiObj.getPoi_title());
				Put poiPut = new Put(poiRowkey);
				poiPut.add(poiFamily, POIObject.POI_VISTOR_RATING_FIELD.getBytes(), Bytes.toBytes(poiObj.getPoi_vistor_rating()));
				poiPut.add(poiFamily, POIObject.POI_RATING_SUMMARY_FIELD.getBytes(), Bytes.toBytes(poiObj.getPoi_rating_summary()));
				poiPut.add(poiFamily, POIObject.POI_TITLE_FIELD.getBytes(), Bytes.toBytes(poiObj.getPoi_title()));
				poiPut.add(poiFamily, POIObject.POI_TELEPHONE_FIELD.getBytes(), Bytes.toBytes(poiObj.getPoi_telephone()));
				poiPut.add(poiFamily, POIObject.POI_TYPE_FIELD.getBytes(), Bytes.toBytes(poiObj.getPoi_type()));
				poiPut.add(poiFamily, POIObject.POI_URL_FIELD.getBytes(), Bytes.toBytes(poiObj.getPoi_url()));
				poiPut.add(poiFamily, POIObject.POI_ADDRESS_FIELD.getBytes(), Bytes.toBytes(poiObj.getPoi_address()));
				poiPut.add(poiFamily, POIObject.POI_SOURCE_FIELD.getBytes(), Bytes.toBytes(SourceEnum.TRIP_ADVISOR.getValue()));
				poiTable.put(poiPut);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			// Store the comment to table "CommentTable"
			List<CommentObject> commentObjList = retrieveComments(html, poiObj.getPoi_title());
			for(CommentObject commentObj: commentObjList){
				try{
					// using the mongodb objectId as the row key, to avoid collide
					ObjectId id= new ObjectId();
					byte[] poiRowkey = DigestUtils.md5(id.toString());
					Put commentPut = new Put(poiRowkey);
					commentPut.add(commentFamily, CommentObject.POI_TITLE_FIELD.getBytes(), Bytes.toBytes(commentObj.getPoi_title()));
					commentPut.add(commentFamily, CommentObject.COMMENT_USER_NAME_FIELD.getBytes(), Bytes.toBytes(commentObj.getComment_user_name()));
					commentPut.add(commentFamily, CommentObject.COMMENT_URL_FIELD.getBytes(), Bytes.toBytes(commentObj.getComment_url()));
					commentPut.add(commentFamily, CommentObject.COMMENT_TITLE_FIELD.getBytes(), Bytes.toBytes(commentObj.getComment_title()));
					commentPut.add(commentFamily, CommentObject.COMMENT_DATE_FIELD.getBytes(), Bytes.toBytes(commentObj.getComment_date()));
					commentPut.add(commentFamily, CommentObject.COMMENT_CONTENT_FIELD.getBytes(), Bytes.toBytes(commentObj.getComment_content()));
					commentPut.add(commentFamily, CommentObject.COMMENT_RATING_FIELD.getBytes(), Bytes.toBytes(commentObj.getComment_rating()));
					
					commentTable.put(commentPut);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private POIObject retrievePOIInfo(String html){
		Document doc = Jsoup.parse(html);
		//TripAdvisor parsing... POI
//		String tripAdvisorURL = "http://www.tripadvisor.com.tw";
		String title = doc.getElementsByAttributeValue("class", "heading_name_wrapper").text();
		Elements arrType = doc.getElementsByAttributeValue("itemprop", "title");
		String poiURL = doc.getElementsByAttributeValue("hreflang", "zh-Hant").attr("href");
		String poiType = "";
		for(Element type : arrType) {
			poiType += type.text() + ">";
		}
		POIObject resultObj = null;
		if(!title.isEmpty() && !poiType.isEmpty()) {
			Elements poiStatus = doc.getElementsByAttributeValue("class", "label fl part clickable");
			ArrayList<String> arrStatus = new ArrayList<String>();
			Elements poiRating = doc.getElementsByAttributeValue("class", "valueCount fr part");
			Elements poiStar = doc.getElementsByAttributeValue("class", "ratingRow wrap");
			ArrayList<String> arrStar = new ArrayList<String>();
			String poiTel = doc.getElementsByAttributeValue("class", "fl phoneNumber").text();
			String poiAddress = doc.getElementsByAttributeValue("class", "format_address").get(0).text();
			for(int i = 0; i < poiStatus.size(); i++) {
				arrStatus.add(poiStatus.get(i).getElementsByAttributeValue("class", "label fl part clickable").text() + ":"
						+ poiRating.get(i).getElementsByAttributeValue("class", "valueCount fr part").text());
			}		
			for(Element li : poiStar) {
				arrStar.add(li.getElementsByAttributeValue("class", "text").text() + ":"
						 + Double.parseDouble(li.getElementsByAttributeValueContaining("class", "rating_s_fill").attr("alt").replaceAll("[\\u4E00-\\u9FA5]", "")));
			}
			
			// POI Object initialize
			resultObj = new POIObject(arrStatus.toString(), arrStar.toString(), title, poiTel, poiType, poiURL, poiAddress);
			
//			System.out.println("POI visitor rating: " + arrStatus);
//			System.out.println("POI rating summary: " + arrStar);
//			System.out.println("POI title: " + title);
//			System.out.println("POI telephone: " + poiTel);
//			System.out.println("POI type: " + poiType);
//			System.out.println("POI URL: " + poiURL);
//			System.out.println("POI address: " + poiAddress);
//			System.out.println();
		}
		return resultObj;
	}
	
	private List<CommentObject> retrieveComments(String html, String poi_title){
		Document doc = Jsoup.parse(html);
		Elements userReviewBlocks = doc.getElementsByAttributeValueContaining("class", "review basic_review inlineReviewUpdate");
		String tripAdvisorURL = "http://www.tripadvisor.com.tw";
		List<CommentObject> commentObjList = new ArrayList<CommentObject>();
		for(Element li : userReviewBlocks) {
			CommentObject commentObj = null;
			String userName = li.getElementsByAttributeValue("class", "username mo").text();
			String reviewURL = tripAdvisorURL + li.getElementsByAttributeValueContaining("href", "ShowUserReviews").attr("href");
			String reviewTitle = li.getElementsByAttributeValue("class", "noQuotes").text();
			String reviewDate= li.getElementsByAttributeValue("class", "ratingDate").text().replaceFirst("[\\u4E00-\\u9FA5]", "-").replaceFirst("[\\u4E00-\\u9FA5]", "-").replaceAll("[\\u4E00-\\u9FA5]", "");
			String review= li.getElementsByAttributeValue("class", "partial_entry").text().replaceAll(" ", "\\/n").replaceAll("\\w\\/n", " ");
			double rating= Double.parseDouble(li.getElementsByAttributeValueContaining("class", "rating_s_fill").attr("alt").replaceAll("[\\u4E00-\\u9FA5]", ""));
			if(!reviewTitle.isEmpty() && !reviewURL.isEmpty() && !review.isEmpty()) {
				commentObj = new CommentObject(poi_title, userName, reviewURL, reviewTitle, reviewDate, review, String.valueOf(rating));
				commentObjList.add(commentObj);
			}
		}
		return commentObjList;
	}	
}

