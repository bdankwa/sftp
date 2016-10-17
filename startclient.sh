#!/bin/bash
CLIENT_BIN=$PWD/src/sftp/client/bin
CLIENT_CLASS_DIR=$CLIENT_BIN/sftp/client
if [[ $# < 1 ]]; then
	echo "Please provide port number for application."
	echo "Usage: ./startclient port"
	exit
fi
CLIENT_CLASS="$CLIENT_CLASS_DIR/SFTPClient.class"
if [ ! -f "$CLIENT_CLASS" ]; then
	echo "server class not found on system"
	echo "please run ./build.sh first."
	exit
fi
cd $CLIENT_BIN
#This must be run from /bin
java -cp . -Djava.security.policy=policy sftp.client.SFTPClient $1 
echo "sftp client up.."
