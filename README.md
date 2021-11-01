switch to project director

To build project please use:
mvn clean install

To run program:
1. Please delete {project-dir}/database folder to clean up previous result
2. mvn compile exec:java -Dexec.mainClass="LogFileProcessorApp" -Dexec.args="path_to_the_log_folder"

eg. if my log file is in test/resources, I would run: 
mvn compile exec:java -Dexec.mainClass="LogFileProcessorApp" -Dexec.args="./src/test/resources/input_log.txt"

