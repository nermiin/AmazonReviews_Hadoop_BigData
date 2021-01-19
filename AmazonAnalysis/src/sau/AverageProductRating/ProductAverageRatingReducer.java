package sau.AverageProductRating;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ProductAverageRatingReducer extends Reducer<Text, CountAverageTuple, Text, CountAverageTuple> {

    private CountAverageTuple result = new CountAverageTuple();

    public void reduce(Text key, Iterable<CountAverageTuple> value, Context context)
        throws IOException, InterruptedException {

        try {
            long count = 0;
            float sum = 0;

            for (CountAverageTuple val: value) {
                count += val.getCount();
                sum += val.getCount() * val.getAverage();
            }

            result.setCount(count);
            result.setAverage(sum/count);
            context.write(key, result);

        } catch (Exception e) {
            System.out.println("Something went wrong in Reducer Task: ");
            e.printStackTrace();
        }
    }

}
