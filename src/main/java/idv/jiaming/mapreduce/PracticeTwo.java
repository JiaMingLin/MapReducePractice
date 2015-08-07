package idv.jiaming.mapreduce;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class PracticeTwo {
	public static final String NAME = "ImportFromFile";
	private static CommandLine parseArgs(String[] args) throws ParseException { 
	       Options options = new Options();
	       Option o = new Option("t", "table", true,
	         "table to import into (must exist)");
	       o.setArgName("table-name");
	       o.setRequired(true);
	       options.addOption(o);
	       o = new Option("c", "column", true,
	         "column to store row data into (must exist)");
	       o.setArgName("family:qualifier");
	       o.setRequired(true);
	       options.addOption(o);
	       o = new Option("i", "input", true,
	         "the directory or file to read from");
	       o.setArgName("path-in-HDFS");
	       o.setRequired(true);
	       options.addOption(o);
	       options.addOption("d", "debug", false, "switch on DEBUG log level");
	       CommandLineParser parser = new PosixParser();
	       CommandLine cmd = null;
	       try {
	         cmd = parser.parse(options, args);
	       } catch (Exception e) {
	         System.err.println("ERROR: " + e.getMessage() + "\n");
	         HelpFormatter formatter = new HelpFormatter();
	         formatter.printHelp(NAME + " ", options, true);
	         System.exit(-1);
	       }
	       return cmd;
	     }

	     public static void main(String[] args) throws Exception {
	       Configuration conf = HBaseConfiguration.create();
	       String[] otherArgs =
	         new GenericOptionsParser(conf, args).getRemainingArgs(); 
	       CommandLine cmd = parseArgs(otherArgs);
	       String table = cmd.getOptionValue("t");
	       String input = cmd.getOptionValue("i");
	       String column = cmd.getOptionValue("c");
	       conf.set("conf.column", column);
	       Job job = Job.getInstance(conf, "Import from file " + input + " into table " + table); 

	       job.setJarByClass(PracticeTwo.class);
	       job.setMapperClass(PracticeTwoMapper.class);
	       job.setOutputFormatClass(TableOutputFormat.class);
	       job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, table);
	       job.setOutputKeyClass(ImmutableBytesWritable.class);
	       job.setOutputValueClass(Put.class);
	       job.setNumReduceTasks(0); 
	       FileInputFormat.addInputPath(job, new Path(input));
	       System.exit(job.waitForCompletion(true) ? 0 : 1);
	     }
}
