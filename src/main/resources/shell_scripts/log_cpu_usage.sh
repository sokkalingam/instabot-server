#!/usr/bin/env bash

top -d 60 -b | grep "load average" -A 10 > /logs/cpu_usage.log &