#!/bin/bash
cd "$(dirname "$0")" || exit 1
if /usr/libexec/java_home -v 21 >/dev/null 2>&1; then
  export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
  export PATH="$JAVA_HOME/bin:$PATH"
fi
if ! java -version 2>&1 | head -1 | grep -q '"21[.]'; then
  echo "Java 21 is required. Install JDK 21 and try again."; read -r; exit 1
fi
if [ ! -f app/timecard.jar ]; then
  bash ./gradlew bootJar || { read -r; exit 1; }
  mkdir -p app
  cp build/libs/*-SNAPSHOT.jar app/timecard.jar || exit 1
fi
(sleep 4; open http://localhost:8080/timecard) &
java -jar app/timecard.jar
read -r -p "Press Enter to close..."
