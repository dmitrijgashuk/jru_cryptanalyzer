package ua.com.javarush.dhashuk.javarush_project_1.parser;

import ua.com.javarush.dhashuk.javarush_project_1.command.*;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class ArgumentParser {

    private static final String NO_ARGUMENT_MESSAGE = "No arguments, you have to write parameter with paths " +
            "to files and cipher key, you should read '-help'";
    private static final String INCORRECT_PARAMETERS_MESSAGE = " Invalid command line: parameters less or more " +
            "then four, read -help";
    private static final String INVALID_KEY_VALUE_MESSAGE = "Key must be positive or negative integer number";
    private static final String HELP_PARAMETERS_MESSAGE = "-help command dose not have parameters";

    private final String[] lineArguments;

    public ArgumentParser(String[] arguments) {
        lineArguments = arguments;
    }

    public Command getCommand(){
        checksExistsOfArguments();

        if("-help".equals(lineArguments[0])){
            if(lineArguments.length>1){
                throw new WrongArgumentException(HELP_PARAMETERS_MESSAGE);
            }
            new HelpCommand(lineArguments);
        }

        if("-encrypt".equals(lineArguments[0])){
            checkCountOfParametrs();
            checkInputFile();
            checkKey();
            return new EncryptCommand(lineArguments);
        }

        if("-decode".equals(lineArguments[0])){
            checkCountOfParametrs();
            checkInputFile();
            checkKey();
            return new DecodeCommand(lineArguments);
        }

        return new NotACommand(lineArguments);
    }

    private void checkCountOfParametrs() {
        if(lineArguments.length>4||lineArguments.length<3){
            throw new WrongArgumentException(INCORRECT_PARAMETERS_MESSAGE);
        }
    }

    private void checkInputFile() {
        String inputFileName = lineArguments[1];

        Path pathToInputFile = null;
        try {
            pathToInputFile = Path.of(inputFileName);
        }catch (InvalidPathException ex){
            throw new WrongArgumentException("cant be a path",ex);
        }

        if(!Files.isRegularFile(pathToInputFile)){
            throw new WrongArgumentException(inputFileName + " is not file");
        }

        var isTxtFile = pathToInputFile.getFileName().toString().endsWith(".txt");
        if(!isTxtFile){
            throw new WrongArgumentException("File "+ pathToInputFile + " is not .txt file");
        }

        if(Files.notExists(pathToInputFile)){
            throw new WrongArgumentException("Error, file " + inputFileName + " is not exist");
        }
    }

    private void checksExistsOfArguments() {
        if(lineArguments.length<1){
            throw new WrongArgumentException(NO_ARGUMENT_MESSAGE);
        }
    }

    private void checkKey() {
        try {
          Integer.parseInt(lineArguments[3]);
        }catch (NumberFormatException ex){
            throw new WrongArgumentException(INVALID_KEY_VALUE_MESSAGE,ex);
        }
    }

}
