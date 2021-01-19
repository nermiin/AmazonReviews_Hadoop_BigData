package sau.totalProducts;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class ProductCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text word = new Text();
    private IntWritable one = new IntWritable(1);

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        try {
            String input[] = value.toString().split("\\t");
            String productId = input[3].trim();

            word.set(productId);
            context.write(word, one);
        } catch (Exception e) {
            System.out.println("Something went wrong in Mapper Task: ");
            e.printStackTrace();
        }

    }
}
