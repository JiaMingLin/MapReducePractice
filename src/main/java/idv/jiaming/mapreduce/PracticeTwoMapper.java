package idv.jiaming.mapreduce;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PracticeTwoMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>{

    private byte[] family = null;
    private byte[] qualifier = null;
    
    protected void setup(Context context) throws IOException, InterruptedException {
      String column = context.getConfiguration().get("conf.column");
      byte[][] colkey = KeyValue.parseColumn(Bytes.toBytes(column));
      family = colkey[0];
      if (colkey.length > 1) {
        qualifier = colkey[1];
      }
    }
    
    public void map(LongWritable offset, Text line, Context context) 
    throws IOException {
       try {
        String lineString = line.toString();
        byte[] rowkey = DigestUtils.md5(lineString); 
        Put put = new Put(rowkey);
        put.add(family, qualifier, Bytes.toBytes(lineString)); 
        context.write(new ImmutableBytesWritable(rowkey), put);
        context.getCounter(Counters.LINES).increment(1);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
