package sau.AverageProductRating;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class ProductAverageRatingMapper extends Mapper<LongWritable, Text, Text, CountAverageTuple> {

    private CountAverageTuple outCountAverage = new CountAverageTuple();
    private Text id = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException  {

        try {

            String input[] = value.toString().split("\\t");
            String productId = input[3].trim();

            if (!productId.isEmpty()) {
                id.set((productId));
                outCountAverage.setCount(Long.valueOf(1));
                outCountAverage.setAverage(Float.valueOf(input[7].trim()));
                context.write(id, outCountAverage);
            }

        } catch (Exception e) {
            System.out.println("Something went wrong in Mapper Task: ");
            e.printStackTrace();
        }

    }
}
