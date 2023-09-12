## Github Copilot Prompts

1. create a public class
2. create empty main method
3. create a pipeline options interface (10 attribes - )
   - create a method to set the project id
   - create a method to set csv header
   - create a method to set datasource name
   - create a method to set gcs location
4. create a pipeline options object from the options interface
5. use pipeline object to read data from GCS location


## Deployment Configuration

Enable API:

Dataflow API
Data pipelines API
Create Artifact repository as mentioned below

europe-west2-docker.pkg.dev/<GCP_Project>/rktest-artifactory
Create two GCS Buckets

gs://<GCP_Project>_cloudbuild (to store dataflow template file i.e. gcstobigquery.json)
gs://<GCP_Project>-logs-bucket (to store dataflow job logs)
Create BigQuery dataset and two table inside dataset

dataflow_pipeline (dataset)
gcs_bq_test - use resources/gcs_bq_test.json table schema to to create this table
gcs_bq_error_table - use resources/gcs_bq_error_table.json table schema to to create this table
Ensure your VPC's subnet has PGA (Private Google Access) is ON

Build Dataflow Flex template using below command

gcloud dataflow flex-template build gs://<GCP_Project>_cloudbuild/gcstobigquery.json --image-gcr-path "europe-west2-docker.pkg.dev/<GCP_Project>/rktest-artifactory/gcstobigquery:latest" --sdk-language "JAVA" --flex-template-base-image "JAVA11" --metadata-file "metadata.json" --jar "target/pipeline-0.0.1.jar" --env FLEX_TEMPLATE_JAVA_MAIN_CLASS="com.example.dataflow.pipeline.GcsToBigQuery" --gcs-log-dir="gs://<GCP_Project>-logs-bucket" --project <GCP_Project>

Deploy and run Dataflow job using below command gcloud dataflow flex-template run "gcs-to-bq" --template-file-gcs-location "gs://<GCP_Project>_cloudbuild/gcstobigquery.json" --parameters projectID=<GCP_Project> --parameters outputTable=<GCP_Project>:dataflow_pipeline.gcs_bq_test --parameters errorSpec=<GCP_Project>:dataflow_pipeline.gcs_bq_error_table --parameters sourceFilePath=gs://<GCP_Project>-config/gcs_bq_csv.csv --parameters csvHeaders="ID~Name~LastName~Marks~Percentage" --parameters sourceName="studentData" --region "europe-west2" --network "default" --service-account-email "<GCP_SA>" --subnetwork "https://www.googleapis.com/compute/v1/projects/<GCP_Project>/regions/europe-west2/subnetworks/default" --num-workers=1 --disable-public-ips --project <GCP_Project>