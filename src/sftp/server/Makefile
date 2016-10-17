
BIN_DIR = bin/
SERVER_DIR = ./
COMMON_DIR = ../

SERVER_SRC = $(SERVER_DIR)SFTPServer.java $(SERVER_DIR)SFTPWorker.java

COMMON_SRC = $(COMMON_DIR)SFTPFile.java

CLASSES = $(SERVER_SRC) $(COMMON_SRC)

JFLAGS = -classpath "../.." -g -d $(BIN_DIR)
JC = javac

.SUFFIXES:.java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm -fr $(BIN_DIR)sftp