#!/bin/bash


hoppinMeta="/Users/sindongboy/Documents/resource/userModelling/20140803/hoppin-meta.uni"
cluster="./movieCluster"

while read line
do
	class=`echo -e "${line}" | cut -f 1`
	uid=`echo -e "${line}" | cut -f 2`
	title=`cat ${hoppinMeta} | grep "${uid}" | cut -f 3`
	echo "${class}	${uid}	${title}"
done < ${cluster}
