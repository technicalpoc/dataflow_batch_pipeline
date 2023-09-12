package com.test.dataflow.test;

import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.ParDo;
import org.junit.Test;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import com.google.api.services.bigquery.model.TableRow;
import com.test.dataflow.transformations.TableRowBasedOnSource;

//create import for Create from org.apache.beam.sdk.transforms
import org.apache.beam.sdk.transforms.Create;


public class Test_GCSToBQ {

// create test class to test the GcsToBQ pipeline
@Test
public void testGcsToBQ() {

// create a pipeline object
    Pipeline p = TestPipeline.create();

//  create a PCollection object to hold the input data
     PCollection<String> input = p.apply(Create.of("1,John,Smith,400,80.0"));

//  create a PCollection object to hold the actual output data
     PCollection<TableRow> actualValue = input.apply(ParDo.of(new TableRowBasedOnSource()));


//  create a PCollection object to hold the expected output data
    PCollection<TableRow> expectedValue = p.apply(Create.of(
    new TableRow().set("ID", 1).set("Name", "John").set("LastName", "Smith").set("Marks", 400).set("Percentage", 80.0)));


//  assert that the expected output and actual output are equal
     PAssert.that(actualValue).containsInAnyOrder(expectedValue);

// run the pipeline
    p.run();
}

    
}
