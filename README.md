# Xml Document Inversion

A small application that reads an XML file from disk, inverts parent-child element relationships and prints the result to the standard output.
## Assumptions

* Application will strip out all 'text' elements. As an example, processing of below xml:

        <a>
            <b>foo</b>
        </a>

will yield:

        <b>
            </a>
        </b>

* Provided Xml has a single leaf element, i.e. below document is invalid:
    <a>
        <b>
            <c/>
            <d/>
        </b>
    </a>

Inversion of such a document will yield:

`Error in Running Document Inversion Program:
 Unexpected error in creating inverted document
`
## Pre-requisites For Building the source:

* Java Jdk 8
* Maven 3.6.x
* Network Connection so that maven can download dependencies based on your local maven settings.

## Build Steps

1. Clone the project.
2. Goto project root on a terminal / console window and execute
    * mvn clean install
 
## How to Run

1. After completing build steps, goto project root on a terminal / console window
2. cd target
3. java -jar Document-Inversion-1.0-SNAPSHOT.jar <path to source file>

* Example:

`java -jar Document-Inversion-1.0-SNAPSHOT.jar "/home/user1/source.xml"`

## Scenarios

See unit tests
* Examples

These examples are using sample source files under src/test/resources

`java -jar Document-Inversion-1.0-SNAPSHOT.jar ../src/test/resources/test1.xml `

will yield:

    <c>
        <b>
            <a/>
        </b>
    </c>
