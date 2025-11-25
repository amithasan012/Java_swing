## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

To run the project : run the following command

javac -cp ".;lib/mysql-connector-j-8.0.33.jar" src/*.java
java -cp ".;lib/mysql-connector-j-8.0.33.jar;src" LoginFrame
