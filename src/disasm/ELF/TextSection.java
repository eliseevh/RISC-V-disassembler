package disasm.ELF;

public class TextSection extends ELFSection {

    public TextSection(ELFFile file) {
        super(file, ".text");
    }

    public byte getByte(int idx) {
        return bytes[idx];
    }
}
