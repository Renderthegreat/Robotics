set -e

gradle build

java -cp build/libs/Robotics.jar cloud.renderlabs.robotlib.Test.Main