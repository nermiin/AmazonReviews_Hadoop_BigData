package sau.mahoutRecommendation;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

// This is used to recommend the products based on rating to the user
// UserID, ProductID and star_rating consists of data for recommendation

public class RecommendationMain {

    final static Logger logger = Logger.getLogger(RecommendationMain.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        boolean mahoutGetDataJobSuccesful = false;
        Path inputPath = new Path(args[0]);
        Path dataOutputPath = new Path(args[1]);
        Path recommedationOutputPath = new Path(args[2]);
        long startTime = System.currentTimeMillis();

        try {

            Job getDataJob = Job.getInstance(conf, "Get recommendation data");
            getDataJob.setJarByClass(sau.mahoutRecommendation.RecommendationMain.class);

            getDataJob.setMapperClass(RecommendationDataMapper.class);
            getDataJob.setMapOutputKeyClass(NullWritable.class);
            getDataJob.setMapOutputValueClass(Text.class);
            getDataJob.setOutputKeyClass(NullWritable.class);
            getDataJob.setOutputValueClass(Text.class);
            getDataJob.setNumReduceTasks(1);

            FileInputFormat.setInputPaths(getDataJob, inputPath);
            FileOutputFormat.setOutputPath(getDataJob, dataOutputPath);

            if (fs.exists(dataOutputPath)) {
                fs.delete(dataOutputPath, true);
            }
            mahoutGetDataJobSuccesful = getDataJob.waitForCompletion(true);

            if(mahoutGetDataJobSuccesful) {

                Job recommendationJob = Job.getInstance(conf, "Recommendation");
                String path = dataOutputPath.toString();
                recommendationJob.getConfiguration().set("DataPath", path);

                recommendationJob.setJarByClass(sau.mahoutRecommendation.RecommendationMain.class);
                FileInputFormat.setInputPaths(recommendationJob, dataOutputPath);
                FileOutputFormat.setOutputPath(recommendationJob, recommedationOutputPath);

                recommendationJob.setMapperClass(UserDataMapper.class);
                recommendationJob.setReducerClass(RecommendationReducer.class);
                recommendationJob.setNumReduceTasks(1);

                recommendationJob.setMapOutputKeyClass(Text.class);
                recommendationJob.setMapOutputValueClass(NullWritable.class);

                recommendationJob.setOutputKeyClass(NullWritable.class);
                recommendationJob.setOutputValueClass(Text.class);
                if (fs.exists(recommedationOutputPath)) {
                    fs.delete(recommedationOutputPath, true);
                }

                recommendationJob.waitForCompletion(true);
                long endTime = System.currentTimeMillis();
                logger.info("Time taken in milliseconds : " + (endTime - startTime));
                logger.info("Time taken in seconds : " + (endTime - startTime)/1000);

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Something went wrong in main recommendation: ");
            e.printStackTrace();
        }

    }
}

