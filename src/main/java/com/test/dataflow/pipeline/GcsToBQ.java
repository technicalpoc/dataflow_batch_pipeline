package com.test.dataflow.pipeline;

// import List and ArrayList from java.util
import java.util.List;
import java.util.ArrayList;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.Write.CreateDisposition;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.Write.WriteDisposition;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.dataflow.tableoptions.Options;
import com.test.dataflow.transformations.TableRowBasedOnSource;

// import bigquery model classes from com.google.api.services.bigquery.model
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.api.services.bigquery.model.TableFieldSchema;


public class GcsToBQ{

// create ToupleTag object to hold the table row
    static final TupleTag<TableRow> tableRowTag = new TupleTag<TableRow>(){
    private static final long serialVersionUID = 1L;
    };

// create ToupleTag object with a name deadLetterTag of String type
    static final TupleTag<String> deadLetterTag = new TupleTag<String>(){
    private static final long serialVersionUID = 1L;
    };

// create a getSchema method to define the schema with attributes e.g. ID,Name,LastName,Marks,Percentage for BQ table
    public static TableSchema getSchema() {
    List<TableFieldSchema> fields = new ArrayList<>();
    fields.add(new TableFieldSchema().setName("ID").setType("STRING"));
    fields.add(new TableFieldSchema().setName("Name").setType("STRING"));
    fields.add(new TableFieldSchema().setName("LastName").setType("STRING"));
    fields.add(new TableFieldSchema().setName("Marks").setType("STRING"));
    fields.add(new TableFieldSchema().setName("Percentage").setType("STRING"));
    return new TableSchema().setFields(fields);
    }

 
public static void main(String[] args) {

// create a pipeline options object from the options interface
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
       
// create a pipeline object from the options object
    Pipeline p = Pipeline.create(options);

//  use pipeline object to read data from GCS location
    PCollection<String> lines = p.apply("ReadLines", TextIO.read().from(options.getInput()));

// apply the transformation on lines object to insert data into BQ table
    lines.apply("InsertDataIntoBQTable", new TableRowBasedOnSource(tableRowTag, deadLetterTag, options.getHeader()))
    .get(tableRowTag)
    .apply("WriteToBQTable", BigQueryIO.writeTableRows()
    .to(options.getProject() + ":" + options.getDatasource() + "." + options.getTableName())
    .withSchema(getSchema())
    .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
    .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED));

// run the pipeline
    p.run().waitUntilFinish();
    }

}
