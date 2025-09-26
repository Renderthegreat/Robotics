set -e

gradle build --quiet --warning-mode all

CLASS_PATH=$(gradle --quiet printClassPath)
MOCKITO_JAR=$(gradle --quiet printMockitoAgentPath)
MAIN_JAR="build/libs/Robotics.jar"
TEST_JAR="build/libs/Robotics-tests.jar"

java \
	-cp "$TEST_JAR:$MAIN_PATH:$CLASS_PATH" \
	-XX:+EnableDynamicAgentLoading \
	-Xshare:off \
	cloud.renderlabs.robotics.RobotTest \