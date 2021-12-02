package disasm.ELF;

import java.util.Map;

import static disasm.util.BytesOperations.*;

public class SymtabEntry {
    private final int nameOffset;
    private final int value;
    private final int size;
    private final byte info;
    private final byte type;
    private final byte bind;
    private final byte other;
    private final byte visibility;
    private final short idx;

    private final int entryIdx;

    private final String name;

    private final static Map<Byte, String> TYPE_DECODE = Map.ofEntries(
            Map.entry((byte) 0, "NOTYPE"),
            Map.entry((byte) 1, "OBJECT"),
            Map.entry((byte) 2, "FUNC"),
            Map.entry((byte) 3, "SECTION"),
            Map.entry((byte) 4, "FILE"),
            Map.entry((byte) 5, "COMMON"),
            Map.entry((byte) 6, "TLS"),
            Map.entry((byte) 10, "LOOS"),
            Map.entry((byte) 12, "HIOS"),
            Map.entry((byte) 13, "STT_LOPROC"),
            Map.entry((byte) 14, "SPARC_REGISTER"),
            Map.entry((byte) 15, "HIPROC")
    );

    private final static Map<Byte, String> BIND_DECODE = Map.of(
            (byte) 0, "LOCAL",
            (byte) 1, "GLOBAL",
            (byte) 3, "WEAK",
            (byte) 10, "LOOS",
            (byte) 12, "HIOS",
            (byte) 13, "LOPROC",
            (byte) 15, "HIPROC"
    );

    private final static Map<Byte, String> VISIBILITY_DECODE = Map.of(
            (byte) 0, "DEFAULT",
            (byte) 1, "INTERNAL",
            (byte) 2, "HIDDEN",
            (byte) 3, "PROTECTED",
            (byte) 4, "EXPORTED",
            (byte) 5, "SINGLETON",
            (byte) 6, "ELIMINATE"
    );


    //TODO: idx decoding

    public String getType() {
        return TYPE_DECODE.get(type);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public SymtabEntry(byte[] bytes, int offset, ELFFile file, int entryIdx) {
        this.entryIdx = entryIdx;
        nameOffset = getIntLittleEndian(bytes, offset);
        value = getIntLittleEndian(bytes, offset + 4);
        size = getIntLittleEndian(bytes, offset + 8);
        info = bytes[offset + 0xc];
        type = (byte) (info & 0xf);
        bind = (byte) (info >>> 4);
        other = bytes[offset + 0xd];
        visibility = (byte) (other & 0x3);
        idx = getShortLittleEndian(bytes, offset + 0xe);
        if (nameOffset == 0) {
            name = "";
        } else {
            name = file.getString(nameOffset);
        }
    }

    @Override
    public String toString() {
        return String.format("[%4d] 0x%-15X %5d %-8s %-8s %-8s %6s %s\n",
                entryIdx,
                value,
                size,
                TYPE_DECODE.get(type),
                BIND_DECODE.get(bind),
                VISIBILITY_DECODE.get(visibility),
                idx,
                name);
    }
}
