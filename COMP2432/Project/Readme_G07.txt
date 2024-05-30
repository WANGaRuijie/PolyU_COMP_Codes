Project title: A Steel-Making Production Line Scheduler (PLS) Group 07

Group memebers: Wang Ruijie, Zhu Jin Shun, Zeng Tianyi, Liu Yuyang and Li Haoxuan

Program Configuration and Execution:
Since the object-oriented programming paradigm is adapted in the program, the definitions of the classes of order, factory and day and their functions are given in three header files order.h, factory.h and day.h respectively. The implementa- tion of the creator, getter, and setter functions are written in three corresponding source files order.c, factory.c and day.c respectively. The main function is in PLS_G07.c, where the header files are included and the functions are called insides. Meanwhile, the header files and their source files are dependent on each other.
Among those files, C standard libraries stdio.h, string.h, stdlib.h, time.h, stdbool.h and limits.h are also included for basic logics including but not limited to stream input and output handling, string manipulation, memory allocation, time manipulation, boolean values, and integer limits. Two libraries related to the operat- ing system, unistd.h and sys/wait.h, are also included for the system call fork(), pipe(), and wait() for creating child processes and inter-process communication.
An external library glpk.h is included for the simplex method to solve the linear programming problem. GLPK (GNU Linear Programming Kit) is a useful package for solving linear programming (LP), mixed integer programming (MIP), and other related problems. It is a set of routines written in ANSI C and organized in the form of a callable library. We introduces the simplex methods provided by GLPK to implement our third scheduling algorithm, the Smallest Global Waste (SGW) algorithm.
Accordingly, please ensure that the gcc compiler is set to support at least the ISO/IEC 9899:1999 C standard (C99). To include the GLPK library, please upload the glpk-5.0.tar file provided to the apollo machine. Afterwards, execute the following command to unarchive the file:

                         tar -xf glpk-5.0.tar

Then, go to the directory of the unarchived files and execute the following commands to install. Remember to replace my netID 22103808d with your own path.

     cd ./glpk-5.0
     ./configure --prefix=/home/22103808d/myusr/local/lib/include
     make
     make install

The procedure above may take a few minutes to complete. After that, use a text editor, e.g., pico to open the file ∼/.bashrc, and add the two lines to the file and save it: 

export C_INCLUDE_PATH=/home/22103808d/myusr/local/lib/include /include:$C_INCLUDE_PATH, and 
export LD_LIBRARY_PATH=/home/22103808d/ myusr/local/lib/include/lib:$LD_LIBRARY_PATH. 

Remember to use your own path instead of mine as well. Finally, execute the command source ∼/.bashrc to make the changes take effect.
In order to run the program, please upload all C source files and header files (order.h, factory.h, day.h, order.c, factory.c, day.c, and PLS_G07.c) to the same directory on apollo. Enter the directory, and input the command below to compile the program: gcc -o PLS_G07 PLS_G07.c order.c factory.c day.c -L/home/22103808d/myusr/local/lib/include/lib -lglpk. Remember not to have a new line in between the command and use your own path. Finally, run the compiled executable file with the command ./PLS_G07.