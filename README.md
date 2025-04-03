# Analyse PDF
Heuristic approach to the PDF Document Classification API

### Tech Stack
* Java + Springboot
* Apache PDFBox

## MVP Solution
1. Ingest a single Invoice or Bank Statement
2. Read info and scrape text
3. Search text for key-words assigning scores to each grouped key-words
4. Identify most probable match
5. Calculate uses of different fonts
6. Return basic analysis report in JSON format

#### key word matching logic
1. Find key-words associated with a given document type
2. If the same key word is found multiple times devalue its overall score
3. Search for specific characteristics related to each document type
    1. Bank Statements
        1. Multiple dates
    2. Invoices
        1. Minimal dates

#### Basic CI/CD
Simple workflow designed to mock a Lambda deployment. Although this code base was not originally designed for a lambda integration the controller should easily be swappable without affecting the remaining codebase
#### Observed Issues
This solution does not work for image based pdfs i.e. Macquarie Bank statements

## Day 2 Extension Ideas
1. Store relevant key-words in a DB that can be updated overtime
2. Store Analysis results a DB to be used for data optimization and reporting
3. Analyse font positions grouping sections of the page by font types
4. Allow the ingestion of multiple PDF's and splitting the analysis with concurrent workers
5. Link relevant Institute with common patterns in their PDF documents i.e. layouts, font ratios, key-words.


### Setting up codebase
_Requires [Maven](https://maven.apache.org/install.html) and [Java 17](https://www.openlogic.com/openjdk-downloads)_

Run the following command in your CLI
```bash
mvn spring-boot:run
```
Or run inside your fav ide

### Calling the API
##### API Endpoint
```
POST http://localhost:8080/api/pdf/analyse
```
Set the body to `Multipart Form` and add a file with the key `file` and select your PDF

The 200 response Body will have the following Structure
```JSON
{
  "documentType": String,
  "fontRatio": {
    "ratios": [
      {
        "name": String,
        "percentage": Double
      }
    ]
  }
}
```
Error responses will have the following structure
```JSON
{
  "error": String
}
```