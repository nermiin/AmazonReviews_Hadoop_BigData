package sau.invertedIndexPattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

//This pattern is used to find each user who has reviewed the product

public class InvertedIndexMain {

    final static Logger logger = Logger.getLogger(InvertedIndexMain.class);
    public static void main(String[] args) throws IOException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        try {
            long startTime = System.currentTimeMillis();
            Job invertedIndexJob = Job.getInstance(conf, "Inverted Index");
            invertedIndexJob.setJarByClass(sau.invertedIndexPattern.InvertedIndexMain.class);

            invertedIndexJob.setMapperClass(InvertedIndexMapper.class);
            invertedIndexJob.setReducerClass(InvertedIndexReducer.class);
            invertedIndexJob.setInputFormatClass(TextInputFormat.class);
            invertedIndexJob.setOutputFormatClass(TextOutputFormat.class);

            invertedIndexJob.setMapOutputKeyClass(Text.class);
            invertedIndexJob.setMapOutputValueClass(Text.class);
            invertedIndexJob.setOutputKeyClass(Text.class);
            invertedIndexJob.setOutputValueClass(Text.class);

            FileInputFormat.addInputPath(invertedIndexJob, new Path(args[0]));
            FileOutputFormat.setOutputPath(invertedIndexJob, new Path(args[1]));
            if (fs.exists(new Path(args[1]))) {
                fs.delete(new Path(args[1]), true);
            }

            invertedIndexJob.waitForCompletion(true);
            long endTime = System.currentTimeMillis();
            logger.info("Time taken in milliseconds : " + (endTime - startTime));
            logger.info("Time taken in seconds : " + (endTime - startTime)/1000);

        } catch (Exception e) {
            System.out.println("Something went wrong in main class: ");
            e.printStackTrace();
        }
    }
}
