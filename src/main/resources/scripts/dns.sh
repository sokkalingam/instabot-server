#!/bin/bash

while true
	do
		echo "Building Local Tunnel"
		lt --subdomain $1 --port $2
	done