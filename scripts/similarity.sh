#!/bin/bash


function usage() {
	echo "Usage : $0 [options]"
	echo "--- optioins ---"
	echo "-m : matrix file"
	echo "-v : vocabulary file"
	echo "-s : similarity measure [COSINE|JACCARD|BM25]" 
	echo "-o : output file"
	echo "-h : help"
	exit 1
}

matrix=""
vocabulary=""
measure=""
output=""

# parse option
while test $# -gt 0; do
	case "$1" in
		-h) # help
			usage
			;;
		-m) # matrix
			shift
			if test $# -gt 0; then
				matrix=$1
			else
				usage
			fi
			shift
			;;
		-v) # vocabulary map
			shift
			if test $# -gt 0; then
				vocabulary=$1
			else
				usage
			fi
			shift
			;;
		-s) # similarity measure
			shift
			if test $# -gt 0; then
				measure=$1
			else
				usage
			fi
			shift
			;;
		-o) # output file
			shift
			if test $# -gt 0; then
				output=$1
			else
				usage
			fi
			shift
			;;
		*) # default
			break
			;;
	esac
done


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

java -Xms2G -Xmx8G -cp ${CP} ${PACKAGE}.driver.GenerateSimilarityTable -m $matrix -v $vocabulary -s $measure -o $output
