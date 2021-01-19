package sau.topNProducts;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TopNProductsReducer extends Reducer<IntWritable, Text, IntWritable, Text> {

    int count = 0;
    private int N = 10;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // default = 10
        this.N = context.getConfiguration().getInt("N", 10);
    }

    @Override
    public void reduce(IntWritable key, Iterable<Text> value, Context context)
            throws IOException, InterruptedException{
        try {
            for(Text val: value){
                if(count<N)
                {
                    context.write(key,val);
                }
                count++;
            }

        } catch (Exception e) {
            System.out.println("Something went wrong in Reducer Task: ");
            e.printStackTrace();
        }
    }
}
