#!/bin/bash
CLIENT_DIR=./src/sftp/client/
SERVER_DIR=./src/sftp/server/

echo "compiling server code..."
cd $SERVER_DIR
make clean; make
echo "compiling client code..."
cd -
#pwd
cd $CLIENT_DIR
pwd
make clean; make
cd -
echo "done!"
