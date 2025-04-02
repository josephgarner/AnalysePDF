# Analyse PDF


### Tech Stack
* Java + Springboot
* Apache PDFBox

### MVP Solution
1. Ingest a single Invoice or Bank Statement
2. Read info and scrape text
3. Search text for key words assigning scores to each grouped key words
4. Identify most probable match
5. Identify any branding
6. Return basic analysis report in JSON format

#### key word matching logic
1. Find key words associated with a given document type
2. If the same key word is found multiple times devalue its overall score
3. Search for specific characteristics related to each document type
    1. Bank Statements
        1. Multiple dates
    2. Invoices
        1. Minimal dates

#### Observed Issues
This solution does not work for image based pdfs i.e. Macquarie Bank statements
#### Solution Diagram
// ToDo
### Day 2 Extension
1. Store relevant key words in a DB that can be updated overtime
2. Store Analysis results a DB to be used for data optimization and reporting
3. Analyse document layouts to determine different financial institutions
4. Analyse font distribution patterns across the document (i.e. Invoices would use less font types compared to statements)
5. Allow the ingestion of multiple PDF's and splitting the analysis with concurrent workers
6. Link relevant Institute with common patterns in their PDF documents i.e. layouts, font ratios, key words.

#### Solution Diagram
// ToDo

### Setting up codebase
// ToDo