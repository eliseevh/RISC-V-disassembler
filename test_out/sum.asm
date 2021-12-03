.text
00010074         register_fini: addi a5, zero, 0
00010078                        c.beqz a5, 12
0001007a                        auipc a0, 0
0001007e                        addi a0, a0, 694
00010082                        c.j 742
00010084                        null
00010086                _start: auipc gp, 2
0001008a                        addi gp, gp, -1126
0001008e                        addi a0, gp, -964
00010092                        addi a2, gp, -928
00010096                        c.sub a2, a0
00010098                        c.li a1, 0
0001009a                        c.jal 304
0001009c                        auipc a0, 0
000100a0                        addi a0, a0, 716
000100a4                        c.beqz a0, 12
000100a6                        auipc a0, 0
000100aa                        addi a0, a0, 650
000100ae                        c.jal 698
000100b0                        c.jal 176
000100b2                        null
000100b4                        addi4spn a1, sp, 4
000100b6                        c.li a2, 0
000100b8                        c.jal 116
000100ba                        c.j 140
000100bc __do_global_dtors_aux: c.addi sp, -16
000100be                        null
000100c0                        addi s0, gp, -964
000100c4                        lbu a5, 0(s0)
000100c8                        null
000100ca                        c.bnez a5, 30
000100cc                        addi a5, zero, 0
000100d0                        c.beqz a5, 18
000100d2                        auipc a0, 1
000100d6                        addi a0, a0, 818
000100da                        auipc ra, 0
000100de                        jalr ra, 0(zero)
000100e2                        c.li a5, 1
000100e4                        sb a5, 0(s0)
000100e8                        null
000100ea                        null
000100ec                        c.addi sp, 16
000100ee                        null
000100f0           frame_dummy: addi a5, zero, 0
000100f4                        c.beqz a5, 22
000100f6                        addi a1, gp, -960
000100fa                        auipc a0, 1
000100fe                        addi a0, a0, 778
00010102                        auipc t1, 0
00010106                        jalr zero, 0(zero)
0001010a                        null
0001010c                   sum: c.addi sp, -32
0001010e                        null
00010110                        addi4spn s0, sp, 32
00010112                        sw a0, -20(s0)
00010116                        sw a1, -24(s0)
0001011a                        lw a4, -20(s0)
0001011e                        lw a5, -24(s0)
00010122                        null
00010124                        null
00010126                        null
00010128                        c.addi16sp sp, 32
0001012a                        null
0001012c                  main: c.addi sp, -16
0001012e                        null
00010130                        null
00010132                        addi4spn s0, sp, 16
00010134                        c.li a1, 3
00010136                        c.li a0, 3
00010138                        c.jal -44
0001013a                        c.li a5, 0
0001013c                        null
0001013e                        null
00010140                        null
00010142                        c.addi sp, 16
00010144                        null
00010146                  exit: c.addi sp, -16
00010148                        c.li a1, 0
0001014a                        null
0001014c                        null
0001014e                        null
00010150                        c.jal 290
00010152                        lw a0, -976(gp)
00010156                        c.lw a5, 60(a0)
00010158                        c.beqz a5, 4
0001015a                        null
0001015c                        null
0001015e                        c.jal 640
00010160     __libc_init_array: c.addi sp, -16
00010162                        null
00010164                        null
00010166                        auipc s0, 1
0001016a                        addi s0, s0, 674
0001016e                        auipc s2, 1
00010172                        addi s2, s2, 666
00010176                        sub s2, s2, s0
0001017a                        null
0001017c                        null
0001017e                        srai s2, s2, 2
00010182                        beq s2, zero, 18
00010186                        c.li s1, 0
00010188                        c.lw a5, 0(s0)
0001018a                        c.addi s1, 1
0001018c                        c.addi s0, 4
0001018e                        null
00010190                        bne s2, s1, -8
00010194                        auipc s0, 1
00010198                        addi s0, s0, 628
0001019c                        auipc s2, 1
000101a0                        addi s2, s2, 628
000101a4                        sub s2, s2, s0
000101a8                        srai s2, s2, 2
000101ac                        beq s2, zero, 18
000101b0                        c.li s1, 0
000101b2                        c.lw a5, 0(s0)
000101b4                        c.addi s1, 1
000101b6                        c.addi s0, 4
000101b8                        null
000101ba                        bne s2, s1, -8
000101be                        null
000101c0                        null
000101c2                        null
000101c4                        null
000101c6                        c.addi sp, 16
000101c8                        null
000101ca                memset: c.li t1, 15
000101cc                        null
000101ce                        bgeu t1, a2, 38
000101d2                        andi a5, a4, 15
000101d6                        c.bnez a5, 126
000101d8                        c.bnez a1, 106
000101da                        andi a3, a2, -16
000101de                        c.andi a2, 15
000101e0                        null
000101e2                        c.sw a1, 0(a4)
000101e4                        c.sw a1, 4(a4)
000101e6                        c.sw a1, 8(a4)
000101e8                        c.sw a1, 12(a4)
000101ea                        c.addi a4, 16
000101ec                        bltu a4, a3, -10
000101f0                        c.bnez a2, 4
000101f2                        null
000101f4                        sub a3, t1, a2
000101f8                        null
000101fa                        auipc t0, 0
000101fe                        null
00010200                        jalr zero, 10(a3)
00010204                        sb a1, 14(a4)
00010208                        sb a1, 13(a4)
0001020c                        sb a1, 12(a4)
00010210                        sb a1, 11(a4)
00010214                        sb a1, 10(a4)
00010218                        sb a1, 9(a4)
0001021c                        sb a1, 8(a4)
00010220                        sb a1, 7(a4)
00010224                        sb a1, 6(a4)
00010228                        sb a1, 5(a4)
0001022c                        sb a1, 4(a4)
00010230                        sb a1, 3(a4)
00010234                        sb a1, 2(a4)
00010238                        sb a1, 1(a4)
0001023c                        sb a1, 0(a4)
00010240                        null
00010242                        andi a1, a1, 255
00010246                        slli a3, a1, 8
0001024a                        c.or a1, a3
0001024c                        slli a3, a1, 16
00010250                        c.or a1, a3
00010252                        c.j -120
00010254                        slli a3, a5, 2
00010258                        auipc t0, 0
0001025c                        null
0001025e                        null
00010260                        jalr ra, -88(a3)
00010264                        null
00010266                        c.addi a5, -16
00010268                        c.sub a4, a5
0001026a                        null
0001026c                        bgeu t1, a2, -120
00010270                        c.j -152
00010272      __call_exitprocs: c.addi16sp sp, -48
00010274                        null
00010276                        lw s4, -976(gp)
0001027a                        null
0001027c                        lw s2, 328(s4)
00010280                        null
00010282                        null
00010284                        null
00010286                        null
00010288                        null
0001028a                        null
0001028c                        null
0001028e                        null
00010290                        beq s2, zero, 48
00010294                        null
00010296                        null
00010298                        c.li s5, 1
0001029a                        c.li s3, -1
0001029c                        lw s1, 4(s2)
000102a0                        addi s0, s1, -1
000102a4                        blt s0, zero, 28
000102a8                        null
000102aa                        null
000102ac                        beq s7, zero, 44
000102b0                        lw a5, 260(s1)
000102b4                        beq a5, s7, 36
000102b8                        c.addi s0, -1
000102ba                        c.addi s1, -4
000102bc                        bne s0, s3, -16
000102c0                        null
000102c2                        null
000102c4                        null
000102c6                        null
000102c8                        null
000102ca                        null
000102cc                        null
000102ce                        null
000102d0                        null
000102d2                        null
000102d4                        c.addi16sp sp, 48
000102d6                        null
000102d8                        lw a5, 4(s2)
000102dc                        c.lw a3, 4(s1)
000102de                        c.addi a5, -1
000102e0                        beq a5, s0, 68
000102e4                        sw zero, 4(s1)
000102e8                        c.beqz a3, -48
000102ea                        lw a5, 392(s2)
000102ee                        sll a4, s5, s0
000102f2                        lw s8, 4(s2)
000102f6                        c.and a5, a4
000102f8                        c.bnez a5, 26
000102fa                        null
000102fc                        lw a4, 4(s2)
00010300                        lw a5, 328(s4)
00010304                        bne a4, s8, 8
00010308                        beq a5, s2, -80
0001030c                        c.beqz a5, -76
0001030e                        null
00010310                        c.j -116
00010312                        lw a5, 396(s2)
00010316                        lw a1, 132(s1)
0001031a                        c.and a4, a5
0001031c                        c.bnez a4, 14
0001031e                        null
00010320                        null
00010322                        c.j -38
00010324                        sw s0, 4(s2)
00010328                        c.j -64
0001032a                        null
0001032c                        null
0001032e                        c.j -50
00010330     __libc_fini_array: c.addi sp, -16
00010332                        null
00010334                        auipc a5, 1
00010338                        addi a5, a5, 224
0001033c                        auipc s0, 1
00010340                        addi s0, s0, 212
00010344                        c.sub a5, s0
00010346                        null
00010348                        null
0001034a                        srai s1, a5, 2
0001034e                        c.beqz s1, 16
00010350                        c.addi a5, -4
00010352                        null
00010354                        c.lw a5, 0(s0)
00010356                        c.addi s1, -1
00010358                        c.addi s0, -4
0001035a                        null
0001035c                        c.bnez s1, -8
0001035e                        null
00010360                        null
00010362                        null
00010364                        c.addi sp, 16
00010366                        null
00010368                atexit: null
0001036a                        c.li a3, 0
0001036c                        c.li a2, 0
0001036e                        c.li a0, 0
00010370                        c.j 2
00010372   __register_exitproc: lw a4, -976(gp)
00010376                        lw a5, 328(a4)
0001037a                        c.beqz a5, 64
0001037c                        c.lw a4, 4(a5)
0001037e                        c.li a6, 31
00010380                        blt a6, a4, 90
00010384                        slli a6, a4, 2
00010388                        c.beqz a0, 38
0001038a                        add t1, a5, a6
0001038e                        sw a2, 136(t1)
00010392                        lw a7, 392(a5)
00010396                        c.li a2, 1
00010398                        sll a2, a2, a4
0001039c                        or a7, a7, a2
000103a0                        sw a7, 392(a5)
000103a4                        sw a3, 264(t1)
000103a8                        c.li a3, 2
000103aa                        beq a0, a3, 26
000103ae                        c.addi a4, 1
000103b0                        c.sw a4, 4(a5)
000103b2                        null
000103b4                        c.sw a1, 8(a5)
000103b6                        c.li a0, 0
000103b8                        null
000103ba                        addi a5, a4, 332
000103be                        sw a5, 328(a4)
000103c2                        c.j -70
000103c4                        lw a3, 396(a5)
000103c8                        c.addi a4, 1
000103ca                        c.sw a4, 4(a5)
000103cc                        c.or a2, a3
000103ce                        sw a2, 396(a5)
000103d2                        null
000103d4                        c.sw a1, 8(a5)
000103d6                        c.li a0, 0
000103d8                        null
000103da                        c.li a0, -1
000103dc                        null
000103de                 _exit: addi a7, zero, 93
000103e2                        ecall
000103e6                        blt a0, zero, 6
000103ea                        c.j 0
000103ec                        c.addi sp, -16
000103ee                        null
000103f0                        null
000103f2                        null
000103f4                        sub s0, zero, s0
000103f8                        c.jal 6
000103fa                        c.sw s0, 0(a0)
000103fc                        c.j 0
000103fe               __errno: lw a0, -968(gp)
00010402                        null

.symtab
Symbol Value              Size Type     Bind     Vis       Index Name
[   0] 0x0                   0 NOTYPE   LOCAL    DEFAULT   UNDEF 
[   1] 0x10074               0 SECTION  LOCAL    DEFAULT       1 
[   2] 0x11404               0 SECTION  LOCAL    DEFAULT       2 
[   3] 0x11408               0 SECTION  LOCAL    DEFAULT       3 
[   4] 0x11410               0 SECTION  LOCAL    DEFAULT       4 
[   5] 0x11418               0 SECTION  LOCAL    DEFAULT       5 
[   6] 0x11848               0 SECTION  LOCAL    DEFAULT       6 
[   7] 0x1185C               0 SECTION  LOCAL    DEFAULT       7 
[   8] 0x0                   0 SECTION  LOCAL    DEFAULT       8 
[   9] 0x0                   0 SECTION  LOCAL    DEFAULT       9 
[  10] 0x0                   0 FILE     LOCAL    DEFAULT     ABS __call_atexit.c
[  11] 0x10074              18 FUNC     LOCAL    DEFAULT       1 register_fini
[  12] 0x0                   0 FILE     LOCAL    DEFAULT     ABS crtstuff.c
[  13] 0x11404               0 OBJECT   LOCAL    DEFAULT       2 
[  14] 0x100BC               0 FUNC     LOCAL    DEFAULT       1 __do_global_dtors_aux
[  15] 0x1185C               1 OBJECT   LOCAL    DEFAULT       7 completed.1
[  16] 0x11410               0 OBJECT   LOCAL    DEFAULT       4 __do_global_dtors_aux_fini_array_entry
[  17] 0x100F0               0 FUNC     LOCAL    DEFAULT       1 frame_dummy
[  18] 0x11860              24 OBJECT   LOCAL    DEFAULT       7 object.0
[  19] 0x1140C               0 OBJECT   LOCAL    DEFAULT       3 __frame_dummy_init_array_entry
[  20] 0x0                   0 FILE     LOCAL    DEFAULT     ABS sum.c
[  21] 0x0                   0 FILE     LOCAL    DEFAULT     ABS exit.c
[  22] 0x0                   0 FILE     LOCAL    DEFAULT     ABS impure.c
[  23] 0x11420            1064 OBJECT   LOCAL    DEFAULT       5 impure_data
[  24] 0x0                   0 FILE     LOCAL    DEFAULT     ABS init.c
[  25] 0x0                   0 FILE     LOCAL    DEFAULT     ABS fini.c
[  26] 0x0                   0 FILE     LOCAL    DEFAULT     ABS atexit.c
[  27] 0x0                   0 FILE     LOCAL    DEFAULT     ABS __atexit.c
[  28] 0x0                   0 FILE     LOCAL    DEFAULT     ABS sys_exit.c
[  29] 0x0                   0 FILE     LOCAL    DEFAULT     ABS errno.c
[  30] 0x0                   0 FILE     LOCAL    DEFAULT     ABS crtstuff.c
[  31] 0x11404               0 OBJECT   LOCAL    DEFAULT       2 __FRAME_END__
[  32] 0x0                   0 FILE     LOCAL    DEFAULT     ABS 
[  33] 0x11414               0 NOTYPE   LOCAL    DEFAULT       4 __fini_array_end
[  34] 0x11410               0 NOTYPE   LOCAL    DEFAULT       4 __fini_array_start
[  35] 0x11410               0 NOTYPE   LOCAL    DEFAULT       3 __init_array_end
[  36] 0x11408               0 NOTYPE   LOCAL    DEFAULT       3 __preinit_array_end
[  37] 0x11408               0 NOTYPE   LOCAL    DEFAULT       3 __init_array_start
[  38] 0x11408               0 NOTYPE   LOCAL    DEFAULT       3 __preinit_array_start
[  39] 0x11C20               0 NOTYPE   GLOBAL   DEFAULT     ABS __global_pointer$
[  40] 0x103FE               6 FUNC     GLOBAL   DEFAULT       1 __errno
[  41] 0x11850               0 NOTYPE   GLOBAL   DEFAULT       6 __SDATA_BEGIN__
[  42] 0x1010C              32 FUNC     GLOBAL   DEFAULT       1 sum
[  43] 0x11854               0 OBJECT   GLOBAL   HIDDEN        6 __dso_handle
[  44] 0x11850               4 OBJECT   GLOBAL   DEFAULT       6 _global_impure_ptr
[  45] 0x10160             106 FUNC     GLOBAL   DEFAULT       1 __libc_init_array
[  46] 0x10330              56 FUNC     GLOBAL   DEFAULT       1 __libc_fini_array
[  47] 0x10272             190 FUNC     GLOBAL   DEFAULT       1 __call_exitprocs
[  48] 0x10086              54 FUNC     GLOBAL   DEFAULT       1 _start
[  49] 0x10372             108 FUNC     GLOBAL   DEFAULT       1 __register_exitproc
[  50] 0x11880               0 NOTYPE   GLOBAL   DEFAULT       7 __BSS_END__
[  51] 0x1185C               0 NOTYPE   GLOBAL   DEFAULT       7 __bss_start
[  52] 0x101CA             168 FUNC     GLOBAL   DEFAULT       1 memset
[  53] 0x1012C              26 FUNC     GLOBAL   DEFAULT       1 main
[  54] 0x10368              10 FUNC     GLOBAL   DEFAULT       1 atexit
[  55] 0x11858               4 OBJECT   GLOBAL   DEFAULT       6 _impure_ptr
[  56] 0x11420               0 NOTYPE   GLOBAL   DEFAULT       5 __DATA_BEGIN__
[  57] 0x1185C               0 NOTYPE   GLOBAL   DEFAULT       6 _edata
[  58] 0x11880               0 NOTYPE   GLOBAL   DEFAULT       7 _end
[  59] 0x10146              26 FUNC     GLOBAL   DEFAULT       1 exit
[  60] 0x103DE              32 FUNC     GLOBAL   DEFAULT       1 _exit
