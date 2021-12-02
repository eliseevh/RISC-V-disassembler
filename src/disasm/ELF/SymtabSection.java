package disasm.ELF;

public class SymtabSection extends ELFSection {
    private final static int ENTRY_SIZE = 0x10;
    private final SymtabEntry[] entries;
    private final static String TABLE_HEADER = "Symbol Value              Size Type     Bind     Vis       Index Name\n";

    public SymtabSection(ELFFile file) {
        super(file, ".symtab");
        if (header.getSize() % ENTRY_SIZE != 0) {
            throw new AssertionError("Incorrect symtab size (must be divisible by "
                    + ENTRY_SIZE + "(entry size))");
        }
        entries = new SymtabEntry[header.getSize() / ENTRY_SIZE];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new SymtabEntry(bytes, i * ENTRY_SIZE, file, i);
        }
    }

    public SymtabEntry[] getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TABLE_HEADER);
        for (SymtabEntry entry : entries) {
            builder.append(entry.toString());
        }
        return builder.toString();
    }
}
