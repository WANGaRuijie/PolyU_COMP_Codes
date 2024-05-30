A Multi-Thread HTTP Web Server by Wang Ruijie (NetID: 22103808d)
Programming Project of PolyU COMP2322 Computer Networking, Spring 2024

The program implements a multi-thread HTTP web server that realizes the functions above:
* proper request and response message exchanges
* multi-threaded handling of concurrent requests from multiple clients
* handling of GET/HEAD requests for text/image fils with four types of response methods
* Keep-Alive/Close connection management
* log generation

The package submitted contains the following files:
* README.txt                          README file
* multi_thread_HTTP_server.py         project source code
* project_report.pdf                  project report file
* sample_server_log.txt               the sample log file
* hotdocs 		              folder with HTML and image files for testing

The standard environment for running the program is:
* Python 3.13.0a1 
* Any operating system that supports the server's time stamp format and request message format
* Please refer to the section 2.1 - Platform, Language and Programming Conventions of the project report for more information

To run the server, just enter the following command in the terminal:
* python3 multi_thread_HTTP_server.py

You may use keyboard interrupt to terminate the program by pressing Ctrl + C

Please feel free to contact ruijie.wang@connect.polyu.hk if you have any question
Have a good time
