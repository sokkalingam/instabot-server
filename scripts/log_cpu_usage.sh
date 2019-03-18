#!/usr/bin/env bash

# Runs every 60 seconds
top -d 60 -b | grep "load average" -A 10 > /logs/cpu_usage.log