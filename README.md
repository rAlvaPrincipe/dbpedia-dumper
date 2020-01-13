## Build
```
$ mvn package
```

## Run
```
$ java -cp target/DBPediaDumper-0.0.1-SNAPSHOT.jar com.app.Dumper root_url langs_file format output_dir 
```
**root_url** is the page of the dbpedia version where all the languages are listed.
example: http://downloads.dbpedia.org/2014/
**langs_file** is the  path to the file where the the desired languages of the chosen dbpedia version are listed.
Follows an example of such file. Note that lines which start with '#' will be ignored. You can create it just by coping and pasting from  languages page.
```
am/                                                08-Sep-2014 08:48                   -
an/                                                08-Sep-2014 08:48                   -
ar/
arz/
ast/                                               08-Sep-2014 08:48                   -
#az/                                                08-Sep-2014 08:48                   -
#ba/                                                08-Sep-2014 08:48                   -
#bat_smg/                                           08-Sep-2014 08:48                   -
```
example: /home/renzo/dbpedia-downloads/langs.txt 
**format** is the format to be considered for the download in target pages like http://downloads.dbpedia.org/2014/en/. 
example: nt.bz2
**output_dir** is the the path to the output directory 
example: /home/renzo/dbpedia-downloads


