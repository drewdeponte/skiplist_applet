CLASS_NAME = SkipListApplet

SRC = $(CLASS_NAME).java
CLASS = $(CLASS_NAME).class

all : $(CLASS)

$(CLASS) : $(SRC)
	javac $(SRC)

output : $(SRC)
	java $(CLASS_NAME)

clean :
	rm -f $(CLASS) *.class *~
