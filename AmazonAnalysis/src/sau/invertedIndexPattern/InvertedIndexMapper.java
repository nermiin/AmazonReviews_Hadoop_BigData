package sau.invertedIndexPattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text productId = new Text();
    private Text userId = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        if(key.get()==0){
            return;
        }
        try{
            String[] tokens = value.toString().split("\\t");
            userId.set(tokens[1]);
            productId.set(tokens[3]);
            context.write(productId, userId);

        } catch(Exception  e){
            System.out.println("Something went wrong in Mapper Task: ");
            e.printStackTrace();
        }
    }
}
