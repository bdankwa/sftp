#!/bin/bash
CLIENT_BIN=$PWD/src/sftp/client/bin
CLIENT_CLASS_DIR=$CLIENT_BIN/sftp/client
if [[ $# < 2 ]]; then
	echo "Please provide correct arguments"
	echo "Usage: ./startclient server_IP retries corrupt"
	echo "       server_IP : IP address of server"
	echo "       retries   : Maximum retries for corrupt files"
	exit
fi
CLIENT_CLASS="$CLIENT_CLASS_DIR/SFTPClient.class"
if [ ! -f "$CLIENT_CLASS" ]; then
	echo "client class not found on system"
	echo "please run ./build.sh first."
	exit
fi
cd $CLIENT_BIN
#This must be run from /bin
java -cp . -Djava.security.policy=policy sftp.client.SFTPClient $1 $2 $3
echo "sftp client up.."
