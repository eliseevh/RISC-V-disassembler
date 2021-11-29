package disasm.ELF;

public class ELFSection {
    protected final byte[] bytes;
    protected final ELFSectionHeader header;

    public ELFSection(ELFFile file, String name) {
        header = file.getSectionHeader(name);
        int size = header.getSize();
        int offset = header.getOffset();
        bytes = new byte[size];
        System.arraycopy(file.getFile(), offset, bytes, 0, size);
    }
}
