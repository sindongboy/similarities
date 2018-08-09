#!/bin/bash


echo "genreate raw matrix"
./generateMatrix.sh TFIDF ./

echo "generate top keyword"
./topKeywords.sh -m ./tfidf.mat -v ./tfidf.voc -n 10 -o ./keyword.top

echo "generate sim table"
./similarity.sh -m ./tfidf.mat -v ./tfidf.voc -s COSINE -o ./sim.table

echo "generate readable sim table"
./printSimTable.sh ./sim.table ./hoppin-meta.uni > readableSim.table

