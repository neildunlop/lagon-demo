You'll need the IntelliJ Lombok plugin installed.

You'll also need to do: 
https://github.com/mplushnikov/lombok-intellij-plugin

REQUIRES MAVEN 3.3.9 - NOT KIDDING!

run with:
 
 `mvn lagom:runAll`
 
 Then hit the following url to get data:
 
 http://127.0.0.1:51563/api/blog/1
 
 
  mvn archetype:generate -DarchetypeGroupId=com.lightbend.lagom -DarchetypeArtifactId=maven-archetype-lagom-java -DarchetypeVersion=1.1.0