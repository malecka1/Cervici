SOURCEFILES=./cervici/*.java ./cervici/GUI/*.java
CLASSES=./cervici/*.class ./cervici/GUI/*.class

all: compile package

compile:
	javac $(SOURCEFILES)

package:
	jar cfm Cervici.jar manifest.mf $(CLASSES)

run:
	java -jar ./Cervici.jar

clean:
	rm -rf ./Cervici.jar $(CLASSES)
