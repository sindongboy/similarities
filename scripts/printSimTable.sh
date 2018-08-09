#!/bin/bash

function usage() {
	echo "Usage: $0 [sim table] [hoppin meta]"
	exit 1
}

simTable=$1
hoppinMeta=$2

if [[ $# -ne 2 ]]; then
	usage
fi

while read line
do
	src=`echo -e "${line}" | sed 's/ ==> /	/g' | cut -f 1 | sed 's/\.out//g'`
	results=`echo -e "${line}" | sed 's/ ==> /	/g' | cut -f 2`
	result1=`echo "${results}" | awk -F " " '{print $2}'`
	result2=`echo "${results}" | awk -F " " '{print $3}'`
	result3=`echo "${results}" | awk -F " " '{print $4}'`
	result4=`echo "${results}" | awk -F " " '{print $5}'`
	result5=`echo "${results}" | awk -F " " '{print $6}'`

	srcTitle=`cat ${hoppinMeta} | grep "${src}" | cut -f 2`

	result1ID=`echo "${result1}" | grep -o "U[^:]*" | sed 's/\.out//g'`
	result2ID=`echo "${result2}" | grep -o "U[^:]*" | sed 's/\.out//g'`
	result3ID=`echo "${result3}" | grep -o "U[^:]*" | sed 's/\.out//g'`
	result4ID=`echo "${result4}" | grep -o "U[^:]*" | sed 's/\.out//g'`
	result5ID=`echo "${result5}" | grep -o "U[^:]*" | sed 's/\.out//g'`

	res1Title=`cat ${hoppinMeta} | grep "${result1ID}" | cut -f 2`
	res2Title=`cat ${hoppinMeta} | grep "${result2ID}" | cut -f 2`
	res3Title=`cat ${hoppinMeta} | grep "${result3ID}" | cut -f 2`
	res4Title=`cat ${hoppinMeta} | grep "${result4ID}" | cut -f 2`
	res5Title=`cat ${hoppinMeta} | grep "${result5ID}" | cut -f 2`

	echo "${src} : ${srcTitle}"
	echo "	-> ${result1ID} : ${res1Title}"
	echo "	-> ${result2ID} : ${res2Title}"
	echo "	-> ${result3ID} : ${res3Title}"
	echo "	-> ${result4ID} : ${res4Title}"
	echo "	-> ${result5ID} : ${res5Title}"
done < ${simTable}
