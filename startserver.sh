#!/bin/bash
SERVER_BIN=$PWD/src/sftp/server/bin
SERVER_CLASS_DIR=$SERVER_BIN/sftp/server
if [[ $# < 1 ]]; then
	echo "Please provide correct arguments."
	echo "Usage: ./startserver corrupt (1 = simulate corrupt file, 0 = don't simulate corrupt file) "
	exit
fi
SERVER_CLASS="$SERVER_CLASS_DIR/SFTPServer.class"
if [ ! -f "$SERVER_CLASS" ]; then
	echo "server class not found on system"
	echo "please run ./build.sh first."
	exit
fi
cd $SERVER_BIN
#This must be run from /bin
java -cp . -Djava.security.policy=policy sftp.server.SFTPServer $1 
echo "sftp server up.."
echo " please run client.sh in a different shell"
