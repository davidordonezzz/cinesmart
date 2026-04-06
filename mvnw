#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.3.2
#
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_SKIP_RC" ] ; then
  if [ -f /usr/local/etc/mavenrc ] ; then
    . /usr/local/etc/mavenrc
  fi
  if [ -f /etc/mavenrc ] ; then
    . /etc/mavenrc
  fi
  if [ -f "$HOME/.mavenrc" ] ; then
    . "$HOME/.mavenrc"
  fi
fi

cygwin=false;
darwin=false;
mingw=false
case "$(uname)" in
  CYGWIN*) cygwin=true ;;
  MINGW*) mingw=true;;
  Darwin*) darwin=true
    if [ -z "$JAVA_HOME" ]; then
      if [ -x "/usr/libexec/java_home" ]; then
        JAVA_HOME="$(/usr/libexec/java_home)"; export JAVA_HOME
      else
        JAVA_HOME="/Library/Java/Home"; export JAVA_HOME
      fi
    fi
    ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=$(java-config --jre-home)
  fi
fi

if $mingw ; then
  [ -n "$JAVA_HOME" ] && [ -d "$JAVA_HOME" ] &&
    JAVA_HOME="$(cd "$JAVA_HOME" || (echo "cannot cd into $JAVA_HOME."; exit 1); pwd)"
fi

if [ -z "$JAVA_HOME" ]; then
  javaExecutable="$(which javac)"
  if [ -n "$javaExecutable" ] && ! [ "$(expr "\"$javaExecutable\"" : '\([^ ]*\)')" = "no" ]; then
    readLink=$(which readlink)
    if [ ! "$(expr "$readLink" : '\([^ ]*\)')" = "no" ]; then
      if $darwin ; then
        javaHome="$(dirname "\"$javaExecutable\"")"
        javaExecutable="$(cd "\"$javaHome\"" && pwd -P)/javac"
      else
        javaExecutable="$(readlink -f "\"$javaExecutable\"")"
      fi
      javaHome="$(dirname "\"$javaExecutable\"")"
      javaHome=$(expr "$javaHome" : '\(.*\)/bin')
      JAVA_HOME="$javaHome"
      export JAVA_HOME
    fi
  fi
fi

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD="$(\unset -f command 2>/dev/null; \command -v java)"
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." >&2
  echo "  We cannot execute $JAVACMD" >&2
  exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "Warning: JAVA_HOME environment variable is not set."
fi

# traverses directory structure from process work directory to filesystem root
# first directory with .mvn subdirectory is considered project base directory
find_maven_basedir() {
  if [ -z "$1" ]
  then
    echo "Path not specified to find_maven_basedir"
    return 1
  fi

  basedir="$1"
  wdir="$1"
  while [ "$wdir" != '/' ] ; do
    if [ -d "$wdir"/.mvn ] ; then
      basedir=$wdir
      break
    fi
    if [ -d "${wdir}" ]; then
      wdir=$(cd "$wdir/.." || exit 1; pwd)
    fi
  done
  printf '%s' "$(cd "$basedir" || exit 1; pwd)"
}

export MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-"$(find_maven_basedir "$(dirname "$0")")"}
if [ -z "$MAVEN_PROJECTBASEDIR" ]; then exit 1; fi

if [ -r "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config" ]; then
  MAVEN_OPTS="$(concat_lines "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config") $MAVEN_OPTS"
fi

wrapperUrl="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
while IFS="=" read -r key value; do
  case "$key" in (wrapperUrl) wrapperUrl="$value"; break ;;
  esac
done < "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

if [ -r "$WRAPPER_JAR" ]; then
    if [ "$MVNW_VERBOSE" = true ]; then
      echo "Found $WRAPPER_JAR"
    fi
else
    if [ -n "$MVNW_REPOURL" ]; then
      wrapperUrl="$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
    fi
    if [ "$MVNW_VERBOSE" = true ]; then
      echo "Couldn't find $WRAPPER_JAR, downloading it ..."
      echo "Downloading from: $wrapperUrl"
    fi
    if command -v wget > /dev/null; then
        QUIET="--quiet"
        if [ "$MVNW_VERBOSE" = true ]; then QUIET=""; fi
        if [ -n "$MVNW_USERNAME" ] && [ -n "$MVNW_PASSWORD" ]; then
            wget $QUIET --http-user="$MVNW_USERNAME" --http-password="$MVNW_PASSWORD" "$wrapperUrl" -O "$WRAPPER_JAR" || rm -f "$WRAPPER_JAR"
        else
            wget $QUIET "$wrapperUrl" -O "$WRAPPER_JAR" || rm -f "$WRAPPER_JAR"
        fi
    elif command -v curl > /dev/null; then
        QUIET="--silent"
        if [ "$MVNW_VERBOSE" = true ]; then QUIET=""; fi
        if [ -n "$MVNW_USERNAME" ] && [ -n "$MVNW_PASSWORD" ]; then
            curl $QUIET --user "$MVNW_USERNAME:$MVNW_PASSWORD" -o "$WRAPPER_JAR" "$wrapperUrl" -f -L || rm -f "$WRAPPER_JAR"
        else
            curl $QUIET -o "$WRAPPER_JAR" "$wrapperUrl" -f -L || rm -f "$WRAPPER_JAR"
        fi
    fi
fi

if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=$(cygpath --path --windows "$JAVA_HOME")
  [ -n "$MAVEN_PROJECTBASEDIR" ] && MAVEN_PROJECTBASEDIR=$(cygpath --path --windows "$MAVEN_PROJECTBASEDIR")
  [ -n "$WRAPPER_JAR" ] && WRAPPER_JAR=$(cygpath --path --windows "$WRAPPER_JAR")
fi

MAVEN_OPTS="$(echo "$MAVEN_OPTS" | tr -d '\n')"

exec "$JAVACMD" \
  $MAVEN_OPTS \
  $MAVEN_DEBUG_OPTS \
  -classpath "$WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}" \
  $WRAPPER_LAUNCHER "$@"
