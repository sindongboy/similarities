#!/bin/bash

while :
do
	clear
	cat $1 | grep -v "	" | wc -l
	sleep 1
done
