package com.test.dataflow.tableoptions;

// import the required libraries
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.PipelineOptions;

//import ValueProvider from org.apache.beam.sdk.options
import org.apache.beam.sdk.options.ValueProvider;


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
    String getDatasource();
    void setDatasource(String value);    

    // create a method to set gcs location
    @Description("GCS location of the file(s) to read from")
    @Validation.Required
    String getInput();
    void setInput(String value);

    // create a method that set BQ table name of type ValueProvider<String>
    @Description("BigQuery table name")
    @Validation.Required
    ValueProvider<String> getTableName();
    void setTableName(ValueProvider<String> value);

    
    

}
