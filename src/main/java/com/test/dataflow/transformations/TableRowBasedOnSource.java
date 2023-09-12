package com.test.dataflow.transformations;

// import the required libraries
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import com.google.api.services.bigquery.model.TableRow;


// create PTransform class which extends PTransform of type PCollection<String> and PCollectionTuple
public class TableRowBasedOnSource extends PTransform<PCollection<String>, PCollectionTuple> {

    // create a private static final long serialVersionUID variable
    private static final long serialVersionUID = 1L;

    // create a private final TupleTag<TableRow> tableRowTag variable
    private final TupleTag<TableRow> tableRowTag;

    // create a private String header variable
    private String headers;

    // create a private final TupleTag<String> deadLetterTag variable
    private final TupleTag<String> deadLetterTag;

     // create a constructore TableRowBasedOnSource which takes TupleTag<TableRow> tableRowTag, TupleTag<String> deadLetterTag and header as arguments
    public TableRowBasedOnSource(TupleTag<TableRow> tableRowTag, TupleTag<String> deadLetterTag, String headers) {
        this.tableRowTag = tableRowTag;
        this.deadLetterTag = deadLetterTag;
        this.headers = headers;
    }

    

    // override a method expand which takes PCollection<String> lines as argument and return PCollectionTuple
    @Override
    public PCollectionTuple expand(PCollection<String> lines) {

        // create a PCollectionTuple object
        PCollectionTuple output = lines.apply("ConvertToTableRow", ParDo.of(new DoFn<PCollection<String>, TableRow>() {

            // override a method processElement which takes ProcessContext c as argument and return void
            @Override
            public void processElement(ProcessContext c) {

                // create a String line variable which holds the value of c.element()
                String line = c.element();

                // create a String[] parts variable which holds the value of line.split(",")
                String[] parts = line.split(",");

                // create a TableRow row variable
                TableRow row = new TableRow();

                // create a String[] header variable which holds the value of headers.split(",")
                String[] header = headers.split(",");
                


                if(!line.contains(headers)){
                    //iterate over parts array and add each element to row object
                    for (int i = 0; i < parts.length; i++) {
                        row.set(header[i], parts[i]);
                    }
                

                // create a if statement which checks if row id is null
                if (row.get("id") == null) {

                    // create a String deadLetter variable which holds the value of line
                    String deadLetter = line;

                    // create a ProcessContext c object
                    c.output(deadLetterTag, deadLetter);

                } else {

                    // create a ProcessContext c object
                    c.output(tableRowTag, row);
                }
              }
            }
        }));
    }
}
