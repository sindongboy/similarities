#!/bin/bash


src=$1
dst=$2

cat ./keyword.top | grep "${src}\|${dst}"
