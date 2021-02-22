package edu.nju;

import org.apache.commons.cli.*;

public class CommandLineUtil {
    private static CommandLine commandLine;
    private static CommandLineParser parser = new DefaultParser();
    private static Options options = new Options();
    private boolean sideEffect;
    public static final String WRONG_MESSAGE = "Invalid input.";

    static {
        options.addOption("h", "help", false, "打印出所有预定义的选项与用法");
        options.addOption("p", "print", true, "打印出arg");
        options.addOption("s", "将CommandlineUtil中sideEffect变量置为true");
    }


    public void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println(WRONG_MESSAGE);
            System.exit(-1);
        }


        parseInput(args);
        handleOptions();

    }


    private static void printHelpMessage() {
        //Print "help"
        System.out.println("help");
    }

    public void parseInput(String[] args) {
        try {
            commandLine = parser.parse(options, args);
            //System.out.println(Arrays.toString(commandLine.getArgs()));
        } catch (ParseException e) {
            System.out.println(WRONG_MESSAGE);
            System.exit(-1);
        }
    }


    public void handleOptions() {
        if (commandLine.hasOption("h")) {
            printHelpMessage();
        } else {

            if (commandLine.hasOption("s")) {
                this.sideEffect = true;
            }

            if (commandLine.hasOption("p")) {
                //getArgs是获得其他参数，可以debug看到
                //像跟在p后面的参数他根本获取不到(感觉需求文档有点误导)
                if (commandLine.getArgs().length == 0) {
                    System.out.println(WRONG_MESSAGE);
                    return;
                }
                String[] arr = commandLine.getOptionValues("p");
                for (int i = 0; i < arr.length; i++) {
                    System.out.println(arr[i]);
                }
            }
        }
    }

    public boolean getSideEffectFlag() {
        return this.sideEffect;
    }

}
