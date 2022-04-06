#!/bin/bash

BIN="/home/simbleau/git/simbleau/thesis-master/demos/bin/warp-sandbox"
ROOT_FOLDER="/home/simbleau/git/simbleau/thesis-master/demos/"

cd $ROOT_FOLDER
exec env CARGO_MANIFEST_DIR=$ROOT_FOLDER $BIN
