package disasm;

import disasm.ELF.ELFFile;
import disasm.ELF.SymtabSection;
import disasm.ELF.TextSection;
import disasm.RISC_V.DisassemblerToString;
import disasm.util.BytesOperations;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Disassembler {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.printf("USAGE: java %s <input_filename> <output_filename>", Disassembler.class.getName());
            return;
        }
        String input = args[0];
        String output = args[1];
        try {
            byte[] bytes = BytesOperations.readFile(input);
            ELFFile file = new ELFFile(bytes);
            SymtabSection symtab = new SymtabSection(file);
            TextSection text = new TextSection(file);
            DisassemblerToString disasm = new DisassemblerToString(symtab, text);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))){
                writer.write(disasm.disasm());
            } catch (IOException e) {
                System.out.println("Cannot write to output file: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot read input file: " + e.getMessage());
        }
    }
}
