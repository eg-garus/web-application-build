java -jar backend\target\app-0.0.1-SNAPSHOT.jar
mvn test -pl backend -Dtest=EventControllerTest
mvn clean package
mvn test -pl backend -Dtest=EventServiceTest
mvn clean test jacoco:report -pl backend
mvn clean package -DskipTests