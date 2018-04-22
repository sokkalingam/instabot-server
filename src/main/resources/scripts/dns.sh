#!/usr/bin/env bash

while true
    do
        echo "Building Localtunnel DNS"
        lt --subdomain $1 --port $2
        sleep 5
    done
	