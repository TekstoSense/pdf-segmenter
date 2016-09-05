# PDF-Segmenter

Identifying Segment or Sections from a PDF file is not a new endeavour in text analytics area. There are few supervised approaches available to correctly identify segments from a pdf. These solutions are mostly apt for scientifc articles as it is trained against scientific corpus. There are few commercial applications also available to identify sections from pdf. 
The supervised approach suffers from specificity. The existing unsupervised approach suffers from inaccuracy.

**pdf-segmenter** is an unsupervised algorithm to identify segment from any type of documents with good accuracy. In our internal testing with scientific articles and journals it clocks more than 90% accuracy. It is able to identify Title and Author as first section and Abstract, Introduction etc. as subsequent sections. It also identifies Tables and Figures with good accuracy. pdf-segmenter provides output as json.

Note : It also works for other type of general pdf documents but accuracy is less than 90%. In our subsequent releases we will improvise the accuracy of other pdf document like books etc.

## Supported PDF Type

- All Journals and Published Papers
- White Papers and other general typ PDF

## Upcoming Releases

- Patent document support

## Dependency

- PDFBox 2.0
