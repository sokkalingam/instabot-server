#!/usr/bin/env bash

while true
    do
        echo "Building DNS for $1:$2"
        lt --subdomain $1 --port $2
        sleep 10
    done
