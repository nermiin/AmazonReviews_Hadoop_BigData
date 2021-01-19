package sau.mahoutRecommendation;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RecommendationDataMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

        try {
            if(key.get()==0){
                return;
            }

            else{
                String[] line = value.toString().split("\\t");
                Text output = new Text();
                output.set(line[1] + "," + line[4] + "," + line[7]); // 1: userID, 4:productID, 7:Rating

                context.write(NullWritable.get(), output);
            }

        } catch(Exception  e){
            System.out.println("Something went wrong in Recommendation Data Mapper Task: ");
            e.printStackTrace();
        }
    }
}
