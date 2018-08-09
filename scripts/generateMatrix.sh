#!/bin/bash

function usage() {
	echo "Usage : $0 [weighting method] [output]"
	echo " - weighting method : TFIDF, LSI or RAW"
	exit 1
}

if [[ $# -ne 2 ]]; then 
	usage
fi

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
CONFIG="/Users/sindongboy/Dropbox/Documents/workspace/similarities/config"

# dependencies
DEF=`find ../lib -type f -name "*" | awk '{printf("%s:", $0);}' | sed 's/:$//g'`
TARGET="../target/similarities-1.0.0-SNAPSHOT.jar"

CP="${CONFIG}:${DEF}:${TARGET}"

PACKAGE="com.skplanet.nlp.similarities"
java -Xms2G -Xmx16G -cp ${CP} ${PACKAGE}.driver.GenerateWeightedMatrix -m $1 -o $2
