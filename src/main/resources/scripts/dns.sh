#!/usr/bin/env bash

URL="$1.localtunnel.me"

while true
do
	echo "Building DNS for $URL:$2"
	
	# Run Local Tunnel for subdomain param1 and port param2,
	# write the output to ltOut.log
	# Run it as background process using & at the end
	lt --subdomain $1 --port $2 > ltOut.log &
	
	# Store the process id of lt background process in pid
	pid=$!
	sleep 10
	
	# Read ltOut.log and store it to variable output
	output=$(<ltOut.log)
	echo "$output"
	
	# if output contains the desired URL
	if [[ $output = *$URL ]]
	then
		# wait till the lt process completes
		echo "Running on $pid"
		wait $pid
	else
		# kill all node.exe processes if the URL does not match what we want
		taskkill -F -IM node.exe
	fi
	sleep 10

done
