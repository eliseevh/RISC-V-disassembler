package disasm.util;

public final class CompressedInstructionDecoding {
    private CompressedInstructionDecoding() {}

    public static String getCompressedRegisterName(int register) {
        if (register < 2) {
            return "s" + register;
        }
        return "a" + (register - 2);
    }

}
