package sau;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

// Find all the records partitioned by the year in which the product was reviewed

public class YearPartitioner {

        public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            Job job = Job.getInstance(conf,"Partitioning");

            job.setJarByClass(sau.YearPartitioner.class);

            job.setMapperClass(YearPartitionMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            //Custom Partitioner:
            job.setPartitionerClass(YearPartitionPartitioner.class);

            job.setReducerClass(YearPartitionReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setNumReduceTasks(14);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            if (fs.exists(new Path(args[1]))) {
                fs.delete(new Path(args[1]), true);
            }

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }

        public static class YearPartitionMapper extends Mapper<LongWritable, Text, Text, Text> {

            private Text inputRec = new Text();
            private Text year = new Text();

            protected void map(LongWritable key, Text value, Mapper.Context context) throws IOException, InterruptedException{

                if(key.get()==0){
                    return;
                }

                String[] line = value.toString().split("\\t");
                String[] yearPart = line[14].split("-");
                String yearVal = yearPart[2].trim();

                year.set(yearVal);
                inputRec.set(value);

                context.write(year, inputRec);
            }
        }

        public static class YearPartitionPartitioner extends Partitioner<Text, Text> {
            @Override
            public int getPartition(Text key, Text value, int numPartitions){
                int n=1;
                if(numPartitions==0){
                    return 0;
                }
                else if(key.equals(("99"))){
                    return n % numPartitions;
                }
                else if(key.equals(new Text("00"))){
                    return 2 % numPartitions;
                }
                else if(key.equals(new Text("01"))){
                    return 3 % numPartitions ;
                }
                else if(key.equals(new Text("02"))){
                    return 4 % numPartitions;
                }
                else if(key.equals(new Text("03"))){
                    return 5 % numPartitions;
                }
                else if(key.equals(new Text("04"))){
                    return 6 % numPartitions;
                }
                else if(key.equals(new Text("05"))){
                    return 7 % numPartitions;
                }
                else if(key.equals(new Text("06"))){
                    return 8 % numPartitions;
                }
                else if(key.equals(new Text("07"))){
                    return 9 % numPartitions;
                }
                else if(key.equals(new Text("08"))){
                    return 10 % numPartitions;
                }
                else if (key.equals(new Text("09"))){
                    return 11 % numPartitions;
                }
                else if (key.equals(new Text("10"))){
                    return 12 % numPartitions;
                }
                else if (key.equals(new Text("11"))){
                    return 13 % numPartitions;
                }
                else
                {
                    return 14 % numPartitions;
                }
            }
        }

        public static class YearPartitionReducer extends Reducer<Text, Text, Text, NullWritable> {

            protected void reduce(Text key, Iterable<Text> values, Reducer.Context context) throws IOException, InterruptedException{
                for(Text t: values){

                    context.write(t, NullWritable.get());
                }
            }
        }
    }
