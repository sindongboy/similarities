#!/bin/bash

# run
VERSION=`cat ../pom.xml | grep "version" | head -1 | grep -o ">.*<" | sed 's/[><]//g'`
TARGET="../target/similarities-${VERSION}.jar"
if [[ ! -f ${TARGET} ]]; then
	echo "[warning] no library installed for version : ${VERSION}"
	echo -n "want to compile and install? (y|N) [y by default]: "
	read count
	if [[ -z ${count} ]] || [[ ${count} == "y" ]]; then  # yes
		cd ../
		mvn install
	elif [[ ${count} == "N" ]]; then  # no
		exit 1
	fi
fi

# env.
CONFIG="/Users/sindongboy/Documents/workspace/similarties/config"

# dependencies
OMPCONFIG="/Users/sindongboy/.m2/repository/com/skplanet/nlp/omp-config/1.0.6-SNAPSHOT/omp-config-1.0.6-SNAPSHOT.jar"
JBLAS="/Users/sindongboy/.m2/repository/org/jblas/jblas/1.2.3/jblas-1.2.3.jar"
LOG4J="/Users/sindongboy/.m2/repository/log4j/log4j/1.2.7/log4j-1.2.7.jar"
COLLECTION="/Users/sindongboy/.m2/repository/net/sourceforge/collections/collections-generic/4.01/collections-generic-4.01.jar"
OMPCLI="/Users/sindongboy/.m2/repository/com/skplanet/nlp/cli/1.0.0/cli-1.0.0.jar"
COMCLI="/Users/sindongboy/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar"
MALLET="/Users/sindongboy/.m2/repository/cc/mallet/mallet/2.0.7/mallet-2.0.7.jar"
TROVE="/Users/sindongboy/Documents/workspace/mallet-test/lib/trove-2.0.2.jar"

CP="${CONFIG}:${OMPCONFIG}:${JBLAS}:${LOG4J}:${COLLECTION}:${OMPCLI}:${COMCLI}:${MALLET}:${TROVE}:${TARGET}"

PACKAGE="com.skplanet.nlp.similarities"

java -Xms2G -Xmx8G -Djava.util.logging.config.file=/tmp/sim.log -cp ${CP} ${PACKAGE}.driver.LDALoaderTester $1
