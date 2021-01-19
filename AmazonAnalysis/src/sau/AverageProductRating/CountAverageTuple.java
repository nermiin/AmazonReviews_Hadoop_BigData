package sau.AverageProductRating;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CountAverageTuple implements Writable {

    private Long count;
    private Float average;

    public CountAverageTuple(){

    }

    public CountAverageTuple(Long count, Float average) {
        this.count = count;
        this.average = average;
    }

    public void write(DataOutput d) throws IOException {
        d.writeLong(count);
        d.writeFloat(average);
    }

    public void readFields(DataInput di) throws IOException {
        count = di.readLong();
        average = di.readFloat();
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return (new StringBuilder().append(count).append("\t").append(average).toString());
    }
}
