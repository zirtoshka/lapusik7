package utilities;

import java.io.Serializable;

import static config.ConfigData.NUMBER_OF_CMD;

public class HistoryWriter implements Serializable {
    private String[] history;

    public HistoryWriter() {
        this.history = new String[NUMBER_OF_CMD];
    }

    public String[] getHistory() {
        return history;
    }

    public void addInHistory(String newCommand) {
        for (int i = 0; i < NUMBER_OF_CMD - 1; i++) {
            history[i] = history[i + 1];
        }
        history[NUMBER_OF_CMD - 1] = newCommand;
    }

    public String getHistoryExc() {
        String[] history = this.history;
        String res = "Last " + NUMBER_OF_CMD + " commands:\n";
        for (String s : history) {
            if (!(s == null)) {
                res += s + "\n";
            }
        }
        return res;
    }
}
