package idv.jiaming.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import idv.jiaming.mapreduce.extensions.WholeFileInputFormat2;

public class PracticeThree {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		Configuration conf = HBaseConfiguration.create();
		Job job = Job.getInstance(conf, "Mapper Practice Three");
		job.setJarByClass(PracticeThree.class);
	    job.setMapperClass(PracticeThreeMapper.class);
	    job.setOutputFormatClass(NullOutputFormat.class);

	    job.setNumReduceTasks(0);
	    job.setInputFormatClass(WholeFileInputFormat2.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
