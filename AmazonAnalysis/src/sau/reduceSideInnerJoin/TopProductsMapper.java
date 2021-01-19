package sau.reduceSideInnerJoin;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TopProductsMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        Text productId = new Text();
        Text count = new Text();

        try {
            String[] input = value.toString().split("\\t");
            productId.set(input[1].trim());
            count.set("#"+ input[1] + " "+ input[0].trim());

            context.write(productId, count);

        } catch (Exception e) {
            System.out.println("Something went wrong in Mapper 1 Task: ");
            e.printStackTrace();
        }
    }
}
