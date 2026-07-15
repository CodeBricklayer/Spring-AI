package com.tp.springai.aimedicalpartner.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalOperationToolTest {

    @Test
    void executeTerminalCommand() {
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        String[] strings = new String[3];
        strings[0] = "cmd";
        strings[1] = "/d";
        strings[2] = "dir";
        System.out.println(terminalOperationTool.executeTerminalCommand(strings));
    }
}