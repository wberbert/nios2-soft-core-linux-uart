#include <asm-generic/vmlinux.lds.h>
.section .dtb.init.rodata,"a"
.balign STRUCT_ALIGNMENT
.global __dtb_nios2_soft_core_begin
__dtb_nios2_soft_core_begin:
.incbin "arch/nios2/boot/dts/nios2_soft_core.dtb" 
__dtb_nios2_soft_core_end:
.global __dtb_nios2_soft_core_end
.balign STRUCT_ALIGNMENT
