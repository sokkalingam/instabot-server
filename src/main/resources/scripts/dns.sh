#!/usr/bin/env bash

while true
    do
        echo "Building Localtunnel DNS for $1"
        lt --subdomain $1 --port $2
        sleep 2
    done
