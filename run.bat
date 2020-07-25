chcp 1253
java -cp bin;lib\commons-io-2.5.jar;lib\java-json.jar;lib\jxl-2.6.12.jar auebunischedule.Main -Dfile.encoding=UTF-8

:: Copy the file "xslt\schedules.xsl" to the folder "schedules\xml".
if not exist schedules mkdir schedules
if not exist schedules\xml mkdir schedules\xml
copy xslt\schedules.xsl schedules\xml

pause
