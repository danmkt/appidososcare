#!/usr/bin/env sh

##############################################################################
##
##  Gradle wrapper script for UNIX
##
##############################################################################

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME\n\nPlease set the JAVA_HOME variable in your environment to match the location of your Java installation."
    fi
else
    JAVACMD="java"
fi

APP_HOME=`dirname "$0"`
APP_HOME=`cd "$APP_HOME" ; pwd`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

# Use the maximum available, or preset max if defined
if [ -z "$JVM_MAX_MEM" ]; then
    JVM_MAX_MEM="1024m"
fi

# Determine the script path
SCRIPT_PATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Execute Gradle
exec "$JAVACMD" \
    -Xmx"$JVM_MAX_MEM" \
    $DEFAULT_JVM_OPTS \
    $JAVA_OPTS \
    -classpath "$SCRIPT_PATH" \
    org.gradle.wrapper.GradleWrapperMain "$@"
