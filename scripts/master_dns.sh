#!/usr/bin/env bash

# How to use
# ./master_dns.sh

DOMAIN_NAME="instabot"
PORT=8080

while true
do
    ./dns.sh ${DOMAIN_NAME}  $PORT
    ./dns.sh ${DOMAIN_NAME}1 $PORT
    ./dns.sh ${DOMAIN_NAME}2 $PORT
done