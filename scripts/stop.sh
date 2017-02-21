#!/bin/bash
pid=`ps aux | grep smartmirror | awk '{print $2}'`
kill -9 $pid