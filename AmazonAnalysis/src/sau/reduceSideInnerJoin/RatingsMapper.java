package sau.reduceSideInnerJoin;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RatingsMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException {

        Text productId = new Text();
        Text rating = new Text();

        try {
            String[] input = value.toString().split("\\t");
            productId.set(input[0].trim());
            rating.set("*" + input[2].trim());

            context.write(productId, rating);

        } catch (Exception e) {
            System.out.println("Something went wrong in Mapper 2 Task: ");
            e.printStackTrace();
        }
    }
}
