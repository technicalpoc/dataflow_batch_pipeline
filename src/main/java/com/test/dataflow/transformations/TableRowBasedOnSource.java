package src.main.java.com.test.dataflow.transformations;

// create PTransform class which extends PTransform of type PCollection<String> and PCollectionTuple
public class TableRowBasedOnSource extends PTransform<PCollection<String>, PCollectionTuple> {

    // create a private static final long serialVersionUID variable
    private static final long serialVersionUID = 1L;

    // create a private final TupleTag<TableRow> tableRowTag variable
    private final TupleTag<TableRow> tableRowTag;

    // create a private final TupleTag<String> stringTag variable
    private final TupleTag<String> stringTag;

    // create a private final TupleTag<String> deadLetterTag variable
    private final TupleTag<String> deadLetterTag = new TupleTag<String>();

    // create a constructor TableRowBasedOnSource which takes TupleTag<TableRow> tableRowTag and TupleTag<String> stringTag as arguments
    public TableRowBasedOnSource(TupleTag<TableRow> tableRowTag, TupleTag<String> stringTag) {
        this.tableRowTag = tableRowTag;
        this.stringTag = stringTag;
    }

    // create a method expand which takes PCollection<String> input as argument and returns PCollectionTuple
    @Override
    
    


    

 
}
