# README

# This a program written in MIPS assembly language. Please run the program via QtSpim Simulator.
# The program receives a user input and converts the decimal input to its binary, quaternary, and octal representation with repetition available.

# The program first requests a number input from the user and prints the input number.
# Then, it is divided into three parts: binary, quaternary, and octal conversion.
# In each of the three parts, the program will iteratively shift the number and divide it by the base of the target representation.
# For binary conversion, 32 loops are used to convert the number to binary, as each binary bit represent itself.
# For quaternary conversion, 16 loops are used to convert the number to quaternary, as two bits are combined to represent a quaternary digit.
# For octal conversion, 33 loops are sufficient to convert the number to octal, as three bits are combined to represent an octal digit, and 32 is not divisible by 3.
# At last, the program asks the user whether to continue the program or not. If the user inputs 1, the program will restart; otherwise, it will print "Bye!" and terminate.

# The method of conversion is similar to the conventional method of dividing the number by the base and list the remainder in reverse order.
# However, it avoids using a stack to store the remainder for reverse printing by utilizing logical right shift. 
# For example, consider the decimal number 12, whcih is store as 1100 in the register.
# If we first shift 3 bits, the remaining part is 0001. Divide it by 2, the remainder is 1.
# Then shift 2 bits, the remaining part is 0011. Divide it by 2, and the remainder is 1.
# After that, shift 1 bit, the remaining part is 0110. Divide it by 2, and the remainder is 0.
# Finally, shift 0 bit, the remaining part is 1100. Divide it by 2, and the remainder is 0.
# The binary representation of 12 is 1100, with the remainder of 1, 1, 0, 0 in the given order.
# The reason is that by shifting n bits, we have already obtained the quotinet after divide the number by 2^n.
# Hence, the program does not need to print the result reversely.
# This method is also applicable to quaternary and octal conversion.

# Please feel free to send email to ruijie.wang@connect.polyu.hk if you have any questions.

# Author: Wang Ruijie 22103808d
# Date: March 28 2024

.data   

prompt_request_input:   .asciiz "Enter a number: "
prompt_show_input:      .asciiz "Input number is "
binary:                 .asciiz "\nBinary: "
quaternary:             .asciiz "\nQuaternary: "
octal:                  .asciiz "\nOctal: "
prompt_continue:        .asciiz "\nContinue?(1=Yes/0=No) "
prompt_bye:             .asciiz "Bye!"

.text   
                        .globl  main

main:                   

    # Request for a number input
    la      $a0,    prompt_request_input
    li      $v0,    4
    syscall 

    # Read the number from user input
    li      $v0,    5
    syscall 
    move    $t0,    $v0

    # Print "Input number is "
    la      $a0,    prompt_show_input
    li      $v0,    4
    syscall 

    # Print the input number
    move    $a0,    $t0
    li      $v0,    1
    syscall 

    # The number of loops equals to the number of bits
    li      $t1,    32

    # Print the prompt of "Binart: "
    li      $v0,    4
    la      $a0,    binary
    syscall 

binary_convertion:      

    # $t2 temporarily stores the value of the number
    move    $t2,    $t0

    # $t3 stores the value of 2 for the division
    li      $t3,    2

    # Decrement the loop counter
    addiu   $t1,    $t1,                    -1

    # Shift right $t1 bits for $t2 and store in $t3
    # The remaining part is the objective of the current loop
    srl     $t4,    $t2,                    $t1

    # Hold the remainder of the division of $t4 by 2
    divu    $t4,    $t3
    mfhi    $t5

    # Print the binary bit of the current bit
    # Each loop prints one bit of the final result
    move    $a0,    $t5
    li      $v0,    1
    syscall 

    # If the loop counter is not 0, then continue the loop
    bnez    $t1,    binary_convertion

    # Print the prompt of "Quaternary: "
    la      $a0,    quaternary
    li      $v0,    4
    syscall 

    # Reset the loop counter to 32
    li      $t1,    32

quaternary_convertion:  

    # $t2 temporarily stores the value of the number
    move    $t2,    $t0

    # $t3 stores the value of 4 for the division
    li      $t3,    4

    # Decrement the loop counter by 2
    # Because the base of quaternary is 4, each object of a loop contains 2 bits
    addiu   $t1,    $t1,                    -2

    # Shift right $t1 bits for $t2 and store in $t3
    srl     $t4,    $t2,                    $t1

    # Hold the remainder of the division of $t3 by 4
    divu    $t4,    $t3
    mfhi    $t5

    # Print the quaternary bit of the current loop
    move    $a0,    $t5
    li      $v0,    1
    syscall 

    # If the loop counter is not 0, then continue the loop
    bnez    $t1,    quaternary_convertion

    # Print the prompt of "Octal: "
    la      $a0,    octal
    li      $v0,    4
    syscall 

    # Reset the loop counter to 33
    # Because the base of octal is 8, each object of a loop contains 3 bits
    # Since 32 is not divisible by 3, the loop counter is set to 33 to ensure all bits will be considered
    li      $t1,    33

octal_convertion:       

    # $t2 temporarily stores the value of the number
    move    $t2,    $t0

    # $t3 stores the value of 8 for the division
    li      $t3,    8

    # Decrement the loop counter by 3
    addiu   $t1,    $t1,                    -3

    # Shift right $t1 bits for $t2 and store in $t3
    srl     $t4,    $t2,                    $t1

    # Hold the remainder of the division of $t3 by 8
    divu    $t4,    $t3
    mfhi    $t5

    # Print the octal bit of the current loop
    move    $a0,    $t5
    li      $v0,    1
    syscall 

    # If the loop counter is not 0, then continue the loop
    bnez    $t1,    octal_convertion

    # Ask user to continue or not
    la      $a0,    prompt_continue
    li      $v0,    4
    syscall 

    # Read the number from user input
    li      $v0,    5
    syscall 

    # If user input is 1, then continue the program
    beq     $v0,    1,                      main

    # If user input is 0, print “Bye!"
    la      $a0,    prompt_bye
    li      $v0,    4
    syscall 

    # Terminate the program and exit
    li      $v0,    10
    syscall 