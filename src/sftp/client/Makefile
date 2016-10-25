
BIN_DIR = ../../../bin
CLIENT_DIR = ./
COMMON_DIR = ../

CLIENT_SRC = $(CLIENT_DIR)SFTPClient.java $(CLIENT_DIR)SFTPShell.java

COMMON_SRC = $(COMMON_DIR)SFTPFile.java

CLASSES = $(CLIENT_SRC) $(COMMON_SRC)

JFLAGS = -classpath "../.." -g -d $(BIN_DIR)
JC = javac

.SUFFIXES:.java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm -fr $(BIN_DIR)sftp/*.class $(BIN_DIR)sftp/client/*.class