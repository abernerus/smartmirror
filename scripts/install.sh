#!/bin/bash
ssh pi@192.168.1.2 "mkdir -p ~/apps/smartmirror"
/bin/bash upload.sh
scp start.sh pi@192.168.1.2:~/apps/smartmirror
scp stop.sh pi@192.168.1.2:~/apps/smartmirror
scp smartmirror pi@192.168.1.2:/etc/init.d/
