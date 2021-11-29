package disasm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ByteFileReader {
    private final byte[] file;

    public static int getNumBigEndian(byte[] bytes, int offset, int numBytes) {
        int num = 0;
        for (int i = offset; i < offset + numBytes; i++) {
            num <<= 8;
            int byteVal = bytes[i] + (bytes[i] >= 0 ? 0 : 256);
            num |= byteVal;
        }
        return num;
    }
    public static int getNumLittleEndian(byte[] bytes, int offset, int numBytes) {
        int num = 0;
        for (int i = offset; i < offset + numBytes; i++) {
            int byteVal = bytes[i] + (bytes[i] >= 0 ? 0 : 256);
            num |= byteVal << 8 * (i - offset);
        }
        return num;
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

    public ByteFileReader(String filename) throws IOException {
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
        file = Arrays.copyOf(bytes, size);
    }

    public byte[] getFile() {
        return file;
    }
}
