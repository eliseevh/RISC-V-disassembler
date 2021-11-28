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
            num |= bytes[i];
        }
        return num;
    }
    public static int getNumLittleEndian(byte[] bytes, int offset, int numBytes) {
        int num = 0;
        for (int i = offset; i < offset + numBytes; i++) {
            num |= bytes[i] << 8 * (i - offset);
        }
        return num;
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
