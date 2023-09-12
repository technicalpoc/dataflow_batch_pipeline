package src.main.java.com.test.dataflow.pipeline;

// import the required libraries
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;

public class GcsToBQ{

// create ToupleTag object to hold the table row
    static final TupleTag<TableRow> tableRowTag = new TupleTag<TableRow>(){
    private static final long serialVersionUID = 1L;
    };


// create empty main method
public static void main(String[] args) {

// create a pipeline options object from the options interface
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
       
// create a pipeline object from the options object
    Pipeline p = Pipeline.create(options);

//  use pipeline object to read data from GCS location
    PCollection<String> lines = p.apply("ReadLines", TextIO.read().from(options.getInput()));

// use pipeline object to transform the data into Bi
}
}