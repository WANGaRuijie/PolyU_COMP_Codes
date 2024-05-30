COMP 2011 Project, Fall 2023

This project is independently completed by Wang Ruijie (22103808d)

The program terminates when the hard disk is full. Be careful!

Due to the limitation of my devices, I only examine the correctness of the sieved prime numbers under 1 billion.

Time complexity: O(NloglogN) (without considering I/O costs).

The idea is completely the same as Sieve of Eratosthenes. 

I use an ArrayList to store the sieved prime numbers. However, when reaching its limitation on the number of elements, the program stores the sieved prime numbers on the file and regard the file as the main memory.

That is, for each iteration of sieving, the program reads the data of the file and store it in a buffer. With multiple buffers, the program can read all sieved and recorded prime numbers without out of memory.

Hope you have a good time!