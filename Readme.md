
## Prerequisite

- OpenJDK 11
- Gradle 7.2

## Build

Just use gradle

```shell
# build and create an all-in-one jar
gradle build

# the output jar will be app/build/libs/app-all.jar
ls app/build/libs/app-all.jar

# (Optional) build examples jar
cd examples && gradle build && cd -
```

## Test

We have written some test cases using junit. Use gradle to run those tests.

```sh
gradle test --info
```

The testcases are located in `app/src/test/java/cn/xxxxxx/iser/jmtrace/testcases`

## Usage

You can use the script `./jmtrace`

```shell
./jmtrace -jar <xxx.jar>
```

Or `make`

```shell
# build examples jar
make build-examples

# run with examples
make run-with WITH=app1 ARGS=""
```

## Resource

**Java agent and ASM**

- https://asm.ow2.io/
- https://www.alibabacloud.com/blog/how-to-use-java-agents-with-asm-bytecode-framework_596199
- https://blogs.oracle.com/javamagazine/post/real-world-bytecode-handling-with-asm
- https://www.baeldung.com/java-asm

**JVM Spec**

- https://docs.oracle.com/javase/specs/jvms/se11/html/index.html
