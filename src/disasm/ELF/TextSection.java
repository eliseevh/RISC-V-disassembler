package disasm.ELF;

public class TextSection extends ELFSection {
    private final int address;

    public TextSection(ELFFile file) {
        super(file, ".text");
        address = header.getAddress();
    }

    public byte getByte(int idx) {
        return bytes[idx];
    }

    public byte[] getBytes(int offset, int size) {
        byte[] result = new byte[size];
        System.arraycopy(bytes, offset, result, 0, size);
        return result;
    }

    public int getSize() {
        return bytes.length;
    }

    public int getAddress() {
        return address;
    }
}
