package sau.AverageProductRating;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.io.Text;
import java.io.IOException;
import org.apache.log4j.Logger;

//	Find the average product rating reviews for each product

public class ProductAverageRatingMain {

    final static Logger logger = Logger.getLogger(ProductAverageRatingMain.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
    {
        try {
            long startTime = System.currentTimeMillis();
            Job job = Job.getInstance();
            job.setJarByClass(sau.AverageProductRating.ProductAverageRatingMain.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            job.setMapperClass(ProductAverageRatingMapper.class);
            job.setReducerClass(ProductAverageRatingReducer.class);
            job.setCombinerClass(ProductAverageRatingReducer.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(CountAverageTuple.class);

            job.setNumReduceTasks(1);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(CountAverageTuple.class);

            job.waitForCompletion(true);
            long endTime = System.currentTimeMillis();
            logger.info("Time taken in milliseconds : " + (endTime - startTime));
            logger.info("Time taken in seconds : " + (endTime - startTime)/1000);

        } catch (Exception e) {
            System.out.println("Something went wrong in main class: ");
            e.printStackTrace();
        }
    }
}
