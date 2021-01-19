package sau.topNProducts;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

// Find the topN reviewed products sorted by count
// Here, secondary sorting technique is used with implementation of comparator class

public class TopNProductsMain {

    final static Logger logger = Logger.getLogger(TopNProductsMain.class);
    public static void main(String[] args) throws IOException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        try {
            long startTime = System.currentTimeMillis();
            Job topNProductsJob = Job.getInstance(conf, "Top N Rated Products");
            topNProductsJob.setJarByClass(sau.topNProducts.TopNProductsMain.class);

            int N = 10;
            topNProductsJob.getConfiguration().setInt("N", N);
            topNProductsJob.setInputFormatClass(TextInputFormat.class);
            topNProductsJob.setOutputFormatClass(TextOutputFormat.class);

            topNProductsJob.setMapperClass(TopNProductsMapper.class);
            topNProductsJob.setSortComparatorClass(CountComparator.class);
            topNProductsJob.setReducerClass(TopNProductsReducer.class);
            topNProductsJob.setNumReduceTasks(1);

            topNProductsJob.setMapOutputKeyClass(IntWritable.class);
            topNProductsJob.setMapOutputValueClass(Text.class);
            topNProductsJob.setOutputKeyClass(IntWritable.class);
            topNProductsJob.setOutputValueClass(Text.class);

            FileInputFormat.setInputPaths(topNProductsJob, new Path(args[0]));  // Output file path of totalProducts - MR chaining
            FileOutputFormat.setOutputPath(topNProductsJob, new Path(args[1]));
            if (fs.exists(new Path(args[1]))) {
                fs.delete(new Path(args[1]), true);
            }

            topNProductsJob.waitForCompletion(true);
            long endTime = System.currentTimeMillis();
            logger.info("Time taken in milliseconds : " + (endTime - startTime));
            logger.info("Time taken in seconds : " + (endTime - startTime)/1000);

        } catch (Exception e) {
            System.out.println("Something went wrong in main class: ");
            e.printStackTrace();

        }
    }
}
