package idv.jiaming.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

//import idv.jiaming.mapreduce.extensions.CustomFileInputFormat;
//import idv.jiaming.mapreduce.extensions.WholeFileInputFormat;
//import idv.jiaming.mapreduce.extensions.WholeFileInputFormat2;
import idv.jiaming.mapreduce.extensions.WholeFileInputFormat2;

import org.apache.hadoop.mapreduce.Job;

public class PracticeOne {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Mapper Practice");
		job.setJarByClass(PracticeOne.class);
	    job.setMapperClass(PracticeOneMapper.class);
	    
        job.setMapOutputKeyClass(LongWritable.class);  
        job.setMapOutputValueClass(Text.class);
	    
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    job.setNumReduceTasks(0);
//	    job.setInputFormatClass(CustomFileInputFormat.class);
	    job.setInputFormatClass(WholeFileInputFormat2.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
