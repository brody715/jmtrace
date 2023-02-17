ARGS =

build-examples:
	cd examples && gradle build

WITH = app1
WITH_DIR = examples/${WITH}/build/libs
run-with:
	./jmtrace -jar examples/${WITH}/build/libs/${WITH}.jar ${ARGS}

## TOOLS ##
copy-deps:
	./gradlew copyDeps

CLASS_PATH = app/build/deps/asm-9.2.jar:app/build/deps/asm-util-9.2.jar
TEXTIFIER_CMD = java -classpath ":${WITH_DIR}/${WITH}.jar"
ASMIFIER_CMD = java -classpath "app/build/deps/asm-9.2.jar:app/build/deps/asm-util-9.2.jar:${WITH_DIR}/${WITH}.jar"

textifier-with:
	@java -classpath "${CLASS_PATH}:${WITH_DIR}/${WITH}.jar" \
		org.objectweb.asm.util.Textifier cn.xxxxxx.iser.jmtrace.examples.${WITH}.App

TC = OnlyStatic
textifier-tc-with:
	@java -classpath "${CLASS_PATH}:app/build/classes/java/test/cn/xxxxxx/iser/jmtrace/testcases/" \
		org.objectweb.asm.util.Textifier app/build/classes/java/test/cn/xxxxxx/iser/jmtrace/testcases/${TC}.class

asmifier-with:
	@java -classpath "${CLASS_PATH}:${WITH_DIR}/${WITH}.jar" \
		org.objectweb.asm.util.ASMifier cn.xxxxxx.iser.jmtrace.examples.${WITH}.App
