

CLASS =       Asteroids
JOGL_PATH =   $(HOME)/lib/jogl-2.0
CLASS_PATH =  ./bin:./lib/\*:$(JOGL_PATH)/jar/\*
NATIVE_PATH = $(JOGL_PATH)/lib
JAVA_ARGS =   -Djava.library.path=$(NATIVE_PATH) -classpath $(CLASS_PATH)

all: bin/$(CLASS).class bin/Actor.class bin/Asteroid.class bin/BannerPanel.class bin/Bullet.class bin/GUI.class bin/PlayerShip.class bin/ScenePanel.class bin/ScorePanel.class bin/Sprite.class bin/Vector.class



bin/%.class: src/%.java
	javac -sourcepath ./src:$(CLASS_PATH) -classpath $(CLASS_PATH) -d bin $<

clean:
	rm bin/*.class

run: all
	java $(JAVA_ARGS) $(CLASS)

test: all
	java -enableassertions $(JAVA_ARGS) $(CLASS)

