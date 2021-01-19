package sau.totalProducts;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import org.apache.log4j.Logger;

// Count total number of products

public class ProductCountMain {

    private final static Logger logger = Logger.getLogger(sau.totalProducts.ProductCountMain.class);
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Configuration conf = new Configuration();
        try {
            long startTime = System.currentTimeMillis();

            Job job = new Job(conf, "ProductCount");
            job.setJarByClass(ProductCountMain.class);

            job.setMapperClass(ProductCountMapper.class);
            job.setReducerClass(ProductCountReducer.class);

            job.setInputFormatClass(TextInputFormat.class);
            job.setOutputFormatClass(TextOutputFormat.class);

            FileInputFormat.addInputPath(job, new Path(args[1]));
            FileOutputFormat.setOutputPath(job, new Path(args[2]));

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            job.setNumReduceTasks(1);
            long endTime = System.currentTimeMillis();
            logger.info("Time taken in milliseconds : " + (endTime - startTime));
            logger.info("Time taken in seconds : " + (endTime - startTime)/1000);
            System.exit(job.waitForCompletion(true) ? 0 : 1);

        } catch (Exception e) {
            System.out.println("Something went wrong in main class: ");
            e.printStackTrace();
        }

    }
}
