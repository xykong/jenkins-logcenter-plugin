build:
	mvn package


debug:
	@echo "Add 'Remote JVM Debug' in your IntelliJ IDEA debug setting with '-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'"
	MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=5005,suspend=n" mvn hpi:run

