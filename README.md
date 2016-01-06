# CIDDIff
ClientItemDefinition Difference Generator

This program will generate a more readable difference output between two files, named ClientItemDefinitions.txt and ClientItemDefinitions_new.txt. Also uncorporates locale file en_us_data.dat for plain text item names.  Inteded for use in quickly reading update changes for Planetside 2. 

Instructions:
Complie or run the jar file with ClientItemDefinitions.txt, ClientItemDefinitions_new.txt, and en_us_data.dat in the same folder. Make sure to encode both in UTF-8 first. The results will be printed in the output terminal.

Note: Assumes that new entries are added to the bottom of the list, as they have been historically.