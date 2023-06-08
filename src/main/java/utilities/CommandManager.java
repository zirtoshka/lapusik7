package utilities;


import IO.ConsoleManager;
import IO.ScannerManager;
import client.Client;
import commands.*;
import data.StudyGroup;
import data.User;
import exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static config.ConfigData.*;
import static data.StudyGroup.wrongId;
import static utilities.GeneratorRandomData.generateRandomGroup;

public class CommandManager {
    private final String runCmd = "Running the command ";

    private final List<String> script = new LinkedList<>();
    boolean runScript = false;
    Scanner scriptScanner = null;
    private final int NAME_CMD = 0;
    private final int ARG_CMD = 1;
    private final CheckIdCommand checkIdCmd;
    private User user;
    private final HistoryWriter historyWriter;
    private Map<String, Command> commandMap = new HashMap<>();

    private final Client client;

    public CommandManager(Client client, User user) {
        this.client = client;
        this.user = user;
        this.historyWriter = new HistoryWriter();
        commandMap.put(INFO, new InfoCommand());
        commandMap.put(ADD, new AddCommand());
        commandMap.put(SHOW, new ShowCommand());
        commandMap.put(ADD_IF_MAX, new AddIfMaxCommand());
        commandMap.put(CLEAR, new ClearCommand(user));
        commandMap.put(HEAD, new HeadCommand());
        commandMap.put(EXECUTE_SCRIPT, new ExecuteScriptCommand());
        commandMap.put(EXIT, new ExitCommand());
        commandMap.put(FILTER_CONTAINS_NAME, new FilterContainsNameCommand());
        commandMap.put(UPDATE_BY_ID, new UpdateByIdCommand());
        commandMap.put(HEAD, new HeadCommand());
        commandMap.put(HISTORY, new HistoryCommand(historyWriter, NUMBER_OF_CMD));
        commandMap.put(PRINT_FIELD_DESCENDING_SEMESTER, new PrintFieldDescendingSemesterCommand());
        commandMap.put(PRINT_UNIQUE_GROUP_ADMIN, new PrintUniqueGroupAdminCommand());
        commandMap.put(LOG_OUT, new LogOutCommand());
        commandMap.put(SHOW, new ShowCommand());


        commandMap.put(REMOVE_BY_ID, new RemoveByIdCommand(user));
        commandMap.put(HELP, new HelpCommand(commandMap.entrySet()
                .stream()
//                .map(entry -> entry.getValue().getName() + ": " + entry.getValue().getDescription() + "\n")
                .map((entry) -> entry.getValue().toString())
                .collect(Collectors.joining())));
        this.checkIdCmd = new CheckIdCommand(user);
        commandMap.put(CHECK_ID, checkIdCmd);

    }

    private void runCmd(Command cmd) {
        System.out.println(runCmd + cmd.getName() + " ...");
        System.out.println(client.run(cmd));
        historyWriter.addInHistory(cmd.getName());
    }

    private void validateAdd(AddCommand cmd) throws IncorrectScriptException, IncorrectValuesForGroupException {
        StudyGroup clientGroup;
        if (ScannerManager.askQuestion("Do you want to generate data for a new study group?", runScript, scriptScanner)) {
            clientGroup = generateRandomGroup();
        } else {
            clientGroup = ScannerManager.askGroup(runScript, scriptScanner);
        }
        clientGroup.setOwner(user);
        cmd.setArgGroup(clientGroup);

    }

    private void validateAddIfMax(AddIfMaxCommand command) throws IncorrectScriptException {
        StudyGroup clientGroup = ScannerManager.askGroup(runScript, scriptScanner);
        clientGroup.setOwner(user);
        command.setArgGroup(clientGroup);
    }

    private void validateRemoveById(RemoveByIdCommand removeByIdCommand, String[] data) {
        LinkedList<String> toId = new LinkedList<String>();
        int lengthData = data.length;
        boolean successGetId = false;
        Integer id = wrongId;
        while (!successGetId) {
            try {
                if (lengthData == 1) {
                    lengthData = 0;
                    throw new ArgsException();
                }
                if (lengthData > 1) {
                    toId.addLast(data[1]);
                    lengthData = 0;
                }

                id = Integer.parseInt(toId.getLast());
                if (!(id > 0)) {
                    throw new NumberFormatException();
                }
                successGetId = true;
            } catch (NumberFormatException e) {
                System.out.println("It can't be id\nEnter id:");
                toId.addLast(ScannerManager.askArgForCmd());
            } catch (ArgsException e) {
                System.out.println("what id is? why it is empty?\nEnter id:");
                toId.addLast(ScannerManager.askArgForCmd());
            }
        }
        removeByIdCommand.setArgId(id);
    }

    private void validateFilterContainsName(FilterContainsNameCommand command, String[] data) {
        LinkedList<String> toName = new LinkedList<String>();
        int lengthData = data.length;
        boolean successGetName = false;
        while (!successGetName) {
            try {
                if (lengthData == 1) {
                    lengthData = 0;
                    throw new ArgsException();
                }
                if (lengthData > 1) {
                    toName.addLast(data[1]);
                    lengthData = 0;
                }
                successGetName = true;
            } catch (ArgsException e) {
                System.out.println("What do I need to find??? why it is empty?\nEnter name:");
                toName.addLast(ScannerManager.askArgForCmd());
            }
        }
        command.setName(toName.getLast());
    }

    private void validateUpdateById(UpdateByIdCommand command, String[] data) throws IncorrectScriptException {
        LinkedList<String> toId = new LinkedList<String>();
        int lengthData = data.length;
        boolean successGetId = false;
        Integer id = wrongId;
        while (!successGetId) {
            try {
                if (lengthData == 1) {
                    lengthData = 0;
                    throw new ArgsException();
                }
                if (lengthData > 1) {
                    toId.addLast(data[1]);
                    lengthData = 0;
                }

                id = Integer.parseInt(toId.getLast());
                if (!(id > 0)) {
                    throw new NumberFormatException();
                }
                successGetId = true;
            } catch (NumberFormatException e) {
                System.out.println("It can't be id\nEnter id:");
                toId.addLast(ScannerManager.askArgForCmd());
            } catch (ArgsException e) {
                System.out.println("what id is? why it is empty?\nEnter id:");
                toId.addLast(ScannerManager.askArgForCmd());
            }
        }
        command.setId(id);
        checkIdCmd.setId(id);
        String res = client.run(checkIdCmd);
        if (res.contains("The command could not be executed ((") || res.contains("You can't update this study group because it's not yours")) {
            System.out.println(res);
        } else {
            StudyGroup clientGroup = ScannerManager.askQuestionForUpdate(runScript, scriptScanner);
            command.setArgGroup(clientGroup);
        }
    }

    private void validateScriprt(ExecuteScriptCommand command, String[] data) throws ExitingException, IOException, LogOutException {
        LinkedList<String> toNameFile = new LinkedList<String>();
        int lengthData = data.length;
        boolean successGetFileName = false;
        while (!successGetFileName) {
            try {
                if (lengthData == 1) {
                    lengthData = 0;
                    throw new ArgsException();
                }
                if (lengthData > 1) {
                    toNameFile.addLast(data[1]);
                    lengthData = 0;
                }
                successGetFileName = true;
            } catch (ArgsException e) {
                System.out.println("What do I need to execute??? why it is empty?\nEnter fileName:");
                toNameFile.addLast(ScannerManager.askArgForCmd());
            }
        }
        scriptMode(toNameFile.getLast());
    }


    public void managerWork(String s) throws IncorrectScriptException, IncorrectValuesForGroupException, ExitingException, IOException, LogOutException {
        String[] data = cmdParser(s);
        Command cmd = commandMap.get(data[0]);
        if (cmd != null) {
            if (cmd.getName().equals(ADD_IF_MAX)) {
                validateAddIfMax((AddIfMaxCommand) cmd);
            } else if (cmd.getName().equals(ADD)) {
                validateAdd((AddCommand) cmd);
            } else if (cmd.getName().equals(REMOVE_BY_ID)) {
                validateRemoveById((RemoveByIdCommand) cmd, data);
            } else if (cmd.getName().equals(FILTER_CONTAINS_NAME)) {
                validateFilterContainsName((FilterContainsNameCommand) cmd, data);
            } else if (cmd.getName().equals(UPDATE_BY_ID)) {
                validateUpdateById((UpdateByIdCommand) cmd, data);
            } else if (cmd.getName().equals(EXECUTE_SCRIPT)) {
                validateScriprt((ExecuteScriptCommand) cmd, data);
            } else if (cmd.getName().equals(EXIT)) {
                throw new ExitingException();
            } else if (cmd.getName().equals(LOG_OUT)) {
                this.user = null;
                throw new LogOutException();
            }
            runCmd(cmd);
        } else {
            ConsoleManager.printError("I don't know this command");
        }


    }


    public String[] cmdParser(String s) {
        try {
            Scanner scanner = new Scanner(s);
            if (!(s.indexOf(" ") == -1)) {
                scanner.useDelimiter("\\s");
                String command = scanner.next();
                String data = "";
                if (scanner.hasNext()) {
                    data = scanner.next();
                }
                return new String[]{command, data};
            } else {
                String commandwodata = scanner.next();
                return new String[]{commandwodata};
            }
        } catch (NoSuchElementException e) {
            return new String[]{"  "};
        }
    }

    public void scriptMode(String arg) throws ExitingException, IOException, LogOutException {
        String path;
        String[] userCmd = {"", ""};
        script.add(arg);
        try {
            path = System.getenv("PWD") + "/" + arg;
            File file = new File(path);
            if (file.exists() && !file.canRead()) throw new NoAccessToFileException();
            Scanner scriptScanner = new Scanner(file);
            setScannerScript(scriptScanner);
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            runScript = true;
            do {
                userCmd = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                userCmd[ARG_CMD] = userCmd[ARG_CMD].trim();
                while (scriptScanner.hasNextLine() && userCmd[NAME_CMD].isEmpty()) {
                    userCmd = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCmd[ARG_CMD] = userCmd[ARG_CMD].trim();
                }
                System.out.println(inputCommand + String.join(" ", userCmd));
                if (userCmd[NAME_CMD].equals(EXECUTE_SCRIPT)) {
                    for (String scri : script) {
                        if (userCmd[ARG_CMD].equals(scri)) throw new ScriptRecurentException();
                    }
                }
                managerWork(userCmd[NAME_CMD] + " " + userCmd[ARG_CMD]);
                if (userCmd[NAME_CMD].equals(EXIT)) {
                    System.exit(1);
                }
            } while (scriptScanner.hasNextLine());

        } catch (NoAccessToFileException e) {
            System.out.println("No rules");
        } catch (NoSuchElementException e) {
            System.out.println("I can't do anything with empty file");
        } catch (FileNotFoundException e) {
            System.out.println("No such file with script");
        } catch (ScriptRecurentException e) {
            System.out.println("Recurrent is cool, but I don't know how to use it");
        } catch (IncorrectScriptException e) {
            System.out.println("Script is incorrect");
        } catch (IncorrectValuesForGroupException e) {
            System.out.println("Script consists of incorrect data for group");
        } finally {
            script.remove(script.size() - 1);
        }
        runScript = false;
        setScannerScript(null);
    }

    private void setScannerScript(Scanner scanner) {
        this.scriptScanner = scanner;
    }


}
