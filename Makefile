#
# Written by Justin Ethier
#
# Make file used to build AVL tree
#

all: Test.java AvlTree.java
	javac -Xlint:unchecked AvlTree.java
	javac -Xlint:unchecked Test.java

# Delete all temporary files generated by a build
clean:
	rm -f *.class
