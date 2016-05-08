#!/bin/bash

if [ ! $1 ]; then
  echo "Please specify a user database file\n"
fi

if [ -e $1 ]; then
  file=`cat $1`
  for line in $file; do
    user=`echo $line | cut -d \; -f 1`
    password=`echo $line | cut -d \; -f 2`
    kamctl add $user $password
    echo $user $password
  done
else
  echo "That file does not exist"
fi
  
  
