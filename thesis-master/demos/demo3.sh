#!/bin/bash

BIN="/home/simbleau/git/simbleau/thesis-master/demos/bin/renderkit"
ASSET1="/home/simbleau/git/simbleau/thesis-master/demos/assets/bezigon-1.svg"
ASSET2="/home/simbleau/git/simbleau/thesis-master/demos/assets/bezigon-1000.svg"

exec $BIN "$ASSET1" &
exec $BIN "$ASSET2" &
