# Utavi client for Hyperledger Fabric

## Compile project
Building of code can be one via maven in docker

```bash
$ docker run -it --rm --name utavi -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-8-alpine mvn clean compile
```

## Run unit test
Building of code can be one via maven in docker

```bash
$ docker run -it --rm --name utavi -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-8-alpine mvn test
```

## Package artifacts
Building of code can be one via maven in docker

```bash
$ docker run -it --rm --name utavi -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-8-alpine mvn package
```

## Run code analysis

### SonarQube

- run sonarqube in docker
```bash
$ docker run --rm -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
```
default credentials: admin/admin

Check https://hub.docker.com/_/sonarqube/ for more information

- update `pom.xml` with [sonar plugin](https://mvnrepository.com/artifact/org.codehaus.mojo/sonar-maven-plugin) details
add next lines:
```xml
<project>
  <build>
    ...
    <plugins>
      ...
      <plugin>
        <groupId>org.sonarsource.scanner.maven</groupId>
        <artifactId>sonar-maven-plugin</artifactId>
        <version>3.4.1.1168</version>
      </plugin>
    </plugins>
  </build>
</project>
```

- run sonarqube scan
```bash
$ docker run -it --rm --name utavi -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-8-alpine mvn clean compile  sonar:sonar -Dsonar.host.url=http://172.17.0.1:9000 -Dsonar.jdbc.url="jdbc:h2:tcp://172.17.0.1/sonar"
```

- Check report and statistic via http://localhost:9000 and selecting project

- Jenkins plugin  
https://wiki.jenkins.io/display/JENKINS/SonarQube+plugin

### [StyleCheck](http://checkstyle.sourceforge.net/)
- update `pom.xml` with [checkstyle plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html) details
add next lines:
```xml
<project>
  ...
  <reporting>
    <plugins>
      ...
			<plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>3.0.0</version>
        <reportSets>
          <reportSet>
            <reports>
              <report>checkstyle</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
    </plugins>
  </reporting>
</project>
```

- run scan  
```bash
$ docker run -it --rm --name utavi -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-8-alpine mvn clean compile checkstyle:checkstyle  checkstyle:checkstyle-aggregate
```

- Check report by opening file `target/site/checkstyle.html` in your browser

- Jenkins plugin  
https://wiki.jenkins.io/display/JENKINS/Checkstyle+Plugin


## Docker build

```bash
$ docker build -t utavi-user-service .
```

## Deployments

More information regarding deployments to certain environments and prerequisites for it can be found in [Deployment information](deployments/README.md) document.


# Problems

### libcrypt.so.1 cannot open shared object file no such file or directory

Installing `libxcrypt-compat` helped.

