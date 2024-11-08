import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriter {
    private String filename;
    private PrintWriter writeOut;
    private int labelNumber;

    private void out(String line) {
        writeOut.println(line);
    }

    private String nextLabel() {
        return String.valueOf(labelNumber++);
    }

    public CodeWriter(File output) throws FileNotFoundException {
        try {
            writeOut = new PrintWriter(output);
            labelNumber = 0;
        } catch (FileNotFoundException fnf) {
            throw new FileNotFoundException("File not found: " + fnf.getMessage());
        }
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public void writeInit() {
        // TODO: set SP to 256
        out("@256");
        out("D=A");
        out("@SP");
        out("M=D");

        // uncomment in project 8
        // writeCall("Sys.init", 0);
    }

    public void writeArithmetic(String operation) {
        out("// " + operation);

        switch (operation.toLowerCase()) {
            case "add":
                 out("@SP");
                 out("AM=M-1");
                 out("D=M");
                 out("A=A-1");
                 out("D=D+M");
                 out("D=M");
            case "sub":
                out("@SP");
                out("AM=M-1");
                out("D=M");
                out("A=A-1");
                out("D=M-D");
                out("D=M");
            case "and":
                out("@SP");
                out("AM=M-1");
                out("D=M");
                out("A=A-1");
                out("D=D&M");
                out("D=M");
            case "or":
                out("@SP");
                out("AM=M-1");
                out("D=M");
                out("A=A-1");
                out("D=D|M");
                out("D=M");

                // TODO: implement arithmetic operations
            case "eq":
            case "lt":
            case "gt":
                // TODO: implement comparison operations
                String labelTrue = "COMPARE.TRUE." + nextLabel();
                String labelEnd = "COMPARE.END." + nextLabel();

                out("@SP");
                out("AM=M-1");
                out("D=M");
                out("A=A-1");
                out("D=M-D");

                switch (operation.toLowerCase()){
                    case "eq":
                        out("@" + labelTrue);
                        out("D;JEQ");
                        break;
                    case "lt":
                        out("@" + labelTrue);
                        out("D;JLT");
                        break;
                    case "gt":
                        out("@" + labelTrue);
                        out("D;JGT");
                        break;
                }
                out("@SP");
                out("A=M-1");
                out("M=0");
                out("@" + labelEnd);
                out("0;JMP");

                out("(" + labelTrue + ")");
                out("@SP");
                out("A=M-1");
                out("M=-1");

                out("(" + labelEnd + ")");
                break;
            case "not":
                out("@SP");
                out("A=M-1");
                out("M=!M");
                break;
                // TODO: implement not operation
            case "neg":
                out("@SP");
                out("A=M-1");
                out("M=-M");
                // TODO: implement negation operation
                break;
        }
    }

    public void writePushPop(CommandType command, String segment, int index) {
        out("// " + command + " " + segment + " " + index);
        if (command == CommandType.C_PUSH) {
            // save value to D
            switch (segment.toLowerCase()) {
                case "pointer":
                    out("@" + (index == 0 ? "THIS" : "THAT"));
                    out("D=M");
                    // TODO: save pointer value to D
                    break;
                case "static":
                    out("@" + filename + "." + index);
                    out("D=M");
                    // TODO: save static value to D
                    break;
                case "constant":
                    out("@" + index);
                    out("D=A");
                    // TODO: save constant value to D
                    break;
                case "temp":
                    out("@" + (5 + index));
                    out("D=M");
                    // TODO: save temp value to D
                    break;
                default:
                    out(getLabel(segment));
                    out("D=M");
                    out("@" + index);
                    out("A=D+A");
                    out("D=M");
                    // TODO: save value from segment to D
                    break;
            }

            // push D to stack
            finishPush();
        } else if (command == CommandType.C_POP) {
            // save address to D
            switch (segment.toLowerCase()) {
                case "pointer":
                    out("@" + (index == 0 ? "THIS" : "THAT"));
                    out("D=A");
                    // TODO: save pointer address to D
                    break;
                case "static":
                    out("@" + filename + "." + index);
                    out("D=A");
                    // TODO: save static address to D
                    break;
                case "temp":
                    out("@" + (5 + index));
                    out("D=A");
                    // TODO: save temp address to D
                    break;
                default:
                    out(getLabel(segment));
                    out("D=M");
                    out("@" + index);
                    out("D=D+A");
                    // TODO: save address from segment to D
                    break;
            }
            out("@R13");
            out("M=D");
            out("@SP");
            out("AM=M-1");
            out("D=M");
            out("@R13");
            out("A=M");
            out("M=D");
            // pop to an address pointed by D
            // TODO: pop to address pointed by D
        }
    }

    private void finishPush() {
        out("@SP");
        out("A=M");
        out("M=D");
        out("@SP");
        out("M=M+1");
        // TODO: push D to stack
    }

    public void writeLabel(String label) {
        out("// C_LABEL " + label);
        out("(" + label + ")");
        out("// C_LABEL " + label);
        // TODO: write label
    }

    public void writeGoto(String label) {
        out("// C_GOTO " + label);
        out("// C_GOTO " + label);
        out("@" + label);
        // TODO: write goto
    }

    public void writeIf(String label) {
        out("// C_IF " + label);
        out("@SP");
        out("AM=M-1");
        out("D=M");
        out("@" + label);
        out("D;JNE");
        // TODO: write if
    }

    public void writeCall(String functionName, int numArgs) {
        String label = nextLabel();

        out("// call " + functionName + " " + numArgs);
        // R13 = SP
        // TODO: store SP in R13

        // push return address
        // TODO: push return address

        // push LCL
        // TODO: push LCL

        // push ARG
        // TODO: push ARG

        // push THIS
        // TODO: push THIS

        // push THAT
        // TODO: push THAT

        // ARG = R13 - numArgs
        // TODO: set ARG to R13 - numArgs, where numArgs is the number of arguments and R13 is the current SP

        // LCL = SP
        // TODO: set LCL to SP

        // goto functionName
        // TODO: goto functionName

        // declare return address label
        // TODO: declare return address label
    }

    public void writeReturn() {
        out("// return");

        // store return address in R13 = LCL - 5
        // TODO: store return address in R13

        // store return value *(SP-1) in *ARG
        // TODO: store return value in *ARG

        // restore SP = ARG + 1
        // TODO: restore SP

        // restore THAT = *(LCL - 1); LCL--
        // TODO: restore THAT

        // restore THIS = *(LCL - 1); LCL--
        // TODO: restore THIS

        // restore ARG = *(LCL - 1); LCL--
        // TODO: restore ARG

        // restore LCL=*(LCL - 1)
        // TODO: restore LCL

        // Jump to return address stored in R13
        // TODO: jump to return address
    }

    public void writeFunction(String functionName, int numLocals) {
        writeOut.println("// function " + functionName + numLocals);

        // declare label for function entry
        // TODO: declare label for function entry

        // initialize local variables to 0
        // TODO: initialize local variables to 0
    }

    private String getLabel(String segment) {
        switch (segment.toLowerCase()) {
            case "local":
                return "@LCL";
            case "argument":
                return "@ARG";
            case "this":
                return "@THIS";
            case "that":
                return "@THAT";
            default:
                return null;
        }
    }

    public void close() {
        writeOut.close();
    }
}
