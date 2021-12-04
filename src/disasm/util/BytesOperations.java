package disasm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class BytesOperations {
    private BytesOperations() {}

    public static byte[] readFile(String filename) throws IOException {
        InputStream input = new FileInputStream(filename);
        byte[] bytes = new byte[1024];
        int read;
        int size = 0;
        while ((read = input.read(bytes, size, bytes.length - size)) != -1) {
            size += read;
            if (size == bytes.length) {
                bytes = Arrays.copyOf(bytes, 2 * size);
            }
        }
        input.close();
        return Arrays.copyOf(bytes, size);
    }

    private static int getNumLittleEndian(byte[] bytes, int offset, int numBytes) {
        int num = 0;
        for (int i = offset; i < offset + numBytes; i++) {
            int byteVal = bytes[i] + (bytes[i] >= 0 ? 0 : 256);
            num |= byteVal << 8 * (i - offset);
        }
        return num;
    }

    public static int getSignExtension(int val, int size) {
        // >> is arithmetic shift, so it works fine
        return (val << (32 - size)) >> (32 - size);
    }

    public static int getIntLittleEndian(byte[] bytes, int offset) {
        return getNumLittleEndian(bytes, offset, 4);
    }
    public static short getShortLittleEndian(byte[] bytes, int offset) {
        return (short) getNumLittleEndian(bytes, offset, 2);
    }

    public static String getNullTerminatedString(byte[] bytes, int offset) {
        StringBuilder builder = new StringBuilder();
        char ch;
        int i = 0;
        while ((ch = (char)getNumLittleEndian(bytes, offset + i, 1)) != 0) {
            builder.append(ch);
            i++;
        }
        return builder.toString();
    }
}
