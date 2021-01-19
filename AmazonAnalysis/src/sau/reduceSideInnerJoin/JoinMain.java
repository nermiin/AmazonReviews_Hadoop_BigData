package sau.reduceSideInnerJoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

// Find average product rating for all those products which are present in the topN reviewed products
// An inner join is used to Product id, review count and average rating
// Multiple input is passed as input for join to be performed

public class JoinMain {

    final static Logger logger = Logger.getLogger(JoinMain.class);
    public static void main(String[] args) throws IOException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        try {
            long startTime = System.currentTimeMillis();
            Job joinsJob = Job.getInstance(conf, "Join");
            joinsJob.setJarByClass(sau.reduceSideInnerJoin.JoinMain.class);

            Path topNProductsOutputPath = new Path(args[0]);
            Path summarizationOutputPath = new Path(args[1]);
            MultipleInputs.addInputPath(joinsJob, topNProductsOutputPath, TextInputFormat.class, TopProductsMapper.class);
            MultipleInputs.addInputPath(joinsJob, summarizationOutputPath, TextInputFormat.class, RatingsMapper.class);
            FileOutputFormat.setOutputPath(joinsJob, new Path(args[2]));

            joinsJob.setReducerClass(JoinReducer.class);

            joinsJob.setMapOutputKeyClass(Text.class);
            joinsJob.setMapOutputValueClass(Text.class);
            joinsJob.setOutputKeyClass(Text.class);
            joinsJob.setOutputValueClass(Text.class);

            if (fs.exists(new Path(args[2]))) {
                fs.delete(new Path(args[2]), true);
            }

            joinsJob.waitForCompletion(true);
            long endTime = System.currentTimeMillis();
            logger.info("Time taken in milliseconds : " + (endTime - startTime));
            logger.info("Time taken in seconds : " + (endTime - startTime)/1000);

        } catch (Exception e) {
            System.out.println("Something went wrong in main class: ");
            e.printStackTrace();
        }

    }
}
