package idv.jiaming.mapreduce;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PracticeOneMapper extends Mapper<Object, Text, LongWritable, Text>{
	private LongWritable textKey = new LongWritable();
	private Text textContent = new Text();
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String content = value.toString();		
		
		Date date = new Date();
		
		textKey.set(date.getTime());
		textContent.set(content);
		context.write(textKey, textContent);
	}
}
