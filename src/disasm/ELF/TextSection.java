package disasm.ELF;

public class TextSection extends ELFSection {

    public TextSection(ELFFile file) {
        super(file, ".text");
    }

    public byte getByte(int idx) {
        return bytes[idx];
    }

    public byte[] getBytes(int offset, int size) {
        byte[] result = new byte[size];
        System.arraycopy(bytes, offset, result, 0, size);
        return result;
    }
}
