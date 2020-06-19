# Energy Australia Technical Test

## First Step - Set up mocking server
* Issue `npm install -g json-server`
* Next, issue `json-server --watch db.json`
* Once your mocking server is up, go to http://localhost:3000/festivals page. It will show you the predefined mocking data in Json. 
* (Optional) Install JSON viewer on your browser for better json data presentation.  


## Second Step - Build & Run
* To build, issue `mvn clean install`
* To run, issue `java -jar target/energyaustralia-0.0.1-SNAPSHOT.jar`
* Once the SpringBoot server is up, go to http://localhost:8080/api/v1/records page. It will show you the music festival data in alphabetical order in Json.
* (Optional) Install JSON viewer on your browser for better json data presentation.
