#!/usr/bin/env bash

killall -9 apache2

# log cpu usage
top -d 60 -b | grep "load average" -A 10 > /logs/cpu_usage.log &