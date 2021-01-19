package sau.invertedIndexPattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class InvertedIndexReducer extends Reducer<Text,Text,Text,Text> {

    private Text result = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        try {
            StringBuilder sb = new StringBuilder();
            boolean first = true;

            for(Text id: values){
                if(first){
                    first = false;
                }
                else{
                    sb.append(" ");
                }
                sb.append(id.toString());
            }

            result.set(sb.toString());
            context.write(key, result);

        } catch (Exception e) {
            System.out.println("Something went wrong in Reducer Task: ");
            e.printStackTrace();
        }
    }
}
