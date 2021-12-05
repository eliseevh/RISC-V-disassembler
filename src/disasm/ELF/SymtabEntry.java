package disasm.ELF;

import java.util.Map;

import static disasm.util.BytesOperations.*;

public class SymtabEntry {
    private final int value;
    private final int size;
    private final byte type;
    private final byte bind;
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
            Map.entry((byte) 10, "OS"),
            Map.entry((byte) 11, "OS"),
            Map.entry((byte) 12, "OS"),
            Map.entry((byte) 13, "PROC"),
            Map.entry((byte) 14, "PROC"),
            Map.entry((byte) 15, "PROC")
    );

    private final static Map<Byte, String> BIND_DECODE = Map.of(
            (byte) 0, "LOCAL",
            (byte) 1, "GLOBAL",
            (byte) 3, "WEAK",
            (byte) 10, "OS",
            (byte) 11, "OS",
            (byte) 12, "OS",
            (byte) 13, "PROC",
            (byte) 14, "PROC",
            (byte) 15, "PROC"
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


    public static String decodeIdx(short index) {
        int idx = Short.toUnsignedInt(index);
        if (idx == 0) {
            return "UNDEF";
        }
        if (idx == 0xff00) {
            return "BEFORE";
        }
        if (idx == 0xff01) {
            return "AFTER";
        }
        if (idx == 0xfff1) {
            return "ABS";
        }
        if (idx == 0xfff2) {
            return "IDX";
        }
        if (idx == 0xffff) {
            return "XINDEX";
        }
        if (0xff00 <= idx && idx <= 0xff1f) {
            return "PROC";
        }
        if (0xff20 <= idx && idx <= 0xff3f) {
            return "OS";
        }
        if (0xff00 <= idx) {
            return "RESERVE";
        }
        return Integer.toString(idx);
    }

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
        int nameOffset = getIntLittleEndian(bytes, offset);
        value = getIntLittleEndian(bytes, offset + 4);
        size = getIntLittleEndian(bytes, offset + 8);
        byte info = bytes[offset + 0xc];
        type = (byte) (info & 0xf);
        bind = (byte) (info >>> 4);
        byte other = bytes[offset + 0xd];
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
                decodeIdx(idx),
                name);
    }
}
