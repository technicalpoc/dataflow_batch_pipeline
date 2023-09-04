package com.test.dataflow.pipeline;

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

// create a pipeline options interface
public interface Options extends PipelineOptions {

    // create a method to set the project id
  @Description("Project ID to run the pipeline under")
    @Default.String("my-project-id")
    String getProject();
    void setProject(String value);

    // create a method to set csv header
    @Description("CSV header to ignore")
    @Default.String("id,name,age")
    String getHeader();
    void setHeader(String value);

    // create a method to set datasource name
    @Description("Data source name")
    @Default.String("datasource")
    String              getDatasource();
    void setDatasource(String value);    

    // create a method to set gcs location
    @Description("GCS location of the file(s) to read from")
    @Validation.Required
    String getInput();
    void setInput(String value);

}


// create empty main method
public static void main(String[] args) {

// create a pipeline options object from the options interface
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
       
// create a pipeline object from the options object
    Pipeline p = Pipeline.create(options);

//  use pipeline object to read data from GCS location
    PCollection<String> lines = p.apply("ReadLines", TextIO.read().from(options.getInput()));

}
}