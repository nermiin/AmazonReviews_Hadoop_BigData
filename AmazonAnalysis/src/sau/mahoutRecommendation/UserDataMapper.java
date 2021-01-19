package sau.mahoutRecommendation;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class UserDataMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        try {
            String[] line = value.toString().split(",");
            String userId = line[0].trim();
            context.write(new Text(userId), NullWritable.get());

        } catch (Exception e) {
            System.out.println("Something went wrong in User Mapper Task: ");
            e.printStackTrace();
        }
    }
}
