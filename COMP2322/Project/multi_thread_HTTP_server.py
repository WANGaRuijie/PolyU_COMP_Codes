"""
COMP2322 Computer Networking Project, Spring 2024
A Multi-Thread HTTP Web Server
:file multi_thread_HTTP_web_server.py
:author: Wang Ruijie
:date: 2024-04-16
:language: Python 3.13.0a1
"""

import socket, threading, time, os
from datetime import datetime

"""
:SERVER_HOST: host of the server
:PORT: port of the server
"""
SERVER_HOST = "127.0.0.1"
PORT = 8000

class HTTPServer:
    """
    This class is the HTTP server that listens to the specified port and host
    """

    def __init__(self, host, port):
        """
        :host: host of the server
        :port: port of the server
        :threaded_connections: list of connections that are handled by threads
        :server_socket: socket object of the server
        :server_log: log file of the server
        """

        self.host = host
        self.port = port
        self.threaded_connections = []
        self.server_socket = None
        self.server_log = None
        
    def run(self):
        """
        This function runs the server and listens to the specified port
        """

        try:
            print("\n" + "*" * 81)
            print("The HTTP server is now running on port {}".format(self.port).center(81))
            print("*" * 81 + "\n")

            """
            The server listens to the specified port and accepts connections
            It creates a thread for each connection and handles the connection in the thread
            It also opens a log file to record the requests and responses
            """
            self.server_log = open("server_log.txt", "a")
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            self.server_socket.bind((self.host, self.port))
            self.server_socket.listen(5)

            """
            The server accepts connections and creates a thread for each connection
            The thread handles the connection by calling the receive function
            The connection is added to the list of threaded connections
            Append the connection to the list of threaded connections
            """
            while True:
                connection, address = self.server_socket.accept()
                thread = threading.Thread(target = self.receive, args = (connection, address))
                thread.start()
                self.threaded_connections.append(connection)
                

        except KeyboardInterrupt:

            """
            Fetch the KeyboardInterrupt and print a message
            """
            print("\n\n" + "*" * 81)
            print("KeyboardInterrupt Fetched".center(81))
            print("The server is ready to close".center(81))
            print("Please wait for the server to close all connections".center(81))
            print("Or press Ctrl+C again to force the threads to terminate".center(81))
            print("*" * 81 + "\n")

        finally:

            """
            Close the server socket and log file
            Close the connections in the list of threaded connections
            """
            self.server_log.close()
            self.server_socket.close()
            for connection in self.threaded_connections:
                connection.close()   
            
    def receive(self, connection, address):
        """
        This function iteratively receives the request from the client and sends the response until the connection is closed or the server is interrupted
        It parses the request into method and url, and calls the appropriate response function to send the response
        It also checks if the connection is keep-alive or not
        :connection: connection object of the client
        :address: address of the client
        :keep_alive_timeout: timeout for keep-alive connections
        :request: request message from the client
        :request_method: method of the request
        :request_url: url of the request
        :keep_alive: boolean to check if the connection is keep-alive
        """

        keep_alive_timeout = 10
        connection.settimeout(keep_alive_timeout)

        while True:

            try:

                """
                Listen to the connection and receive the request
                """
                request = connection.recv(1024).decode()

                """
                If the request is empty, the server waits for 1 second and continues to receive the request
                """
                if (request == "") or (request is None):
                    time.sleep(1)
                    continue
                
                print("Client " + str(address) + " requested: \n\n" + request)
                
                """
                The server checks if the connection is keep-alive or not
                """
                keep_alive = False
                for line in request.split("\n"):
                    if ("Connection: keep-alive" in line) or ("Connection: Keep-Alive" in line):
                        keep_alive = True
                        break
                
                """
                Parse the request into method and url
                """
                request_method = request.split("\n")[0].split(" ")[0]
                request_url = request.split("\n")[0].split(" ")[1]

                """
                The server calls the appropriate response function based on the method of the request
                """
                if (request_method == "GET"):
                    self.response_GET(connection, request, request_url, address)
                elif (request_method == "HEAD"):
                    self.response_HEAD(connection, request, request_url, address)
                else:
                    self.response_bad_request(connection, request_url, address)

                """
                If the connection is not keep-alive, the server immediately closes the connection
                """
                if (keep_alive == False):
                    connection.close()
                    print("\n" + "*" * 81)
                    print(("Client" + str(address) + " disconnected: no keep-alive").center(81))
                    print("*" * 81 + "\n")
                    break

            except socket.timeout:

                """
                If the connection is keep-alive and the timeout is reached, the server closes the connection
                """
                connection.close()
                print("\n" + "*" * 81)
                print(("Client " + str(address) + " disconnected: keep-alive timeout").center(81))
                print("*" * 81 + "\n")
                break

            except KeyboardInterrupt:
                """
                If the server is interrupted, the server closes the connection and removes it from the list of threaded connections
                """
                self.threaded_connections.remove(connection)
                break


    def response_GET(self, connection, request, request_url, address):
        """
        This function sends the response to the client for the GET request
        It checks if the file exists and sends the appropriate response
        If the file is not found, it sends a 404 Not Found response
        :connection: connection object of the client
        :request: request message from the client
        :request_url: url of the request
        :address: address of the client
        :file_type: type of the file requested
        :file: file object of the requested file
        :contents: contents of the file
        :check_time: time to check if the file is modified
        :file_time: time of the file
        :response_parts: parts of the response message
        :response: response message
        """

        """
        If the request url is "/", the server changes it to "/index.html in default"
        """
        if request_url == "/":
            request_url = "/index.html"

        """
        Get the file type of the requested file
        If the file type is text/html, the file is read as text
        Otherwise, the file is read as binary
        """
        file_type = self.get_file_type(request_url)

        try:
            if (file_type == "text/html"):
                file = open("htdocs" + request_url, "r")
                contents = file.read()
                file.close()
            else:
                file = open("htdocs" + request_url, "rb")
                contents = file.read()
                file.close()

            """
            If the request has "If-Modified-Since" header, the server checks if the file is modified
            If the file is not modified, the server sends a 304 Not Modified response
            Otherwise, the server sends a 200 OK response
            """
            if "If-Modified-Since" in request:

                for line in request.split("\n"):
                    if "If-Modified-Since" in line:
                        check_time = line[19:48]
                        break;

                """
                Convert the time to datetime format
                """
                check_time = datetime.strptime(check_time, '%a, %d %b %Y %H:%M:%S %Z')
                file_time = datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))
                
                if (file_time <= check_time):

                    """
                    The server sends a 304 Not Modified response if the file is not modified
                    """
                    response_parts = ["HTTP/1.1 304 Not Modified\r\n",
                                    "Last-Modified: " + str(datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))) + "\r\n",
                                    "Content-Length: " + str(len(contents)) + "\r\n",
                                    "Content-Type: " + file_type + "\r\n\r\n"]
                    
                    """
                    If the file type is text/html, the server sends the contents as text using encode()
                    Otherwise, the server sends in the binary format for files like images
                    """
                    response = "".join(response_parts)
                    connection.sendall(response.encode())

                    print("Server response:\n")
                    print(response)
                    self.write_log(address, request_url, "304 Not Modified")
                    
                
                else:

                    """
                    The server sends a 200 OK response if the file is modified
                    """
                    response_parts = ["HTTP/1.1 200 OK\r\n",
                                "Last-Modified: " + str(datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))) + "\r\n",
                                "Content-Length: " + str(len(contents)) + "\r\n",
                                "Content-Type: " + file_type + "\r\n\r\n"]
                    response = "".join(response_parts)
                    connection.sendall(response.encode())

                    if (file_type == "text/html"):
                        connection.sendall(contents.encode())
                    else:
                        connection.sendall(contents)

                    print("Server response:\n")
                    print(response)
                    self.write_log(address, request_url, "200 OK")

            else:

                """
                The server sends a 200 OK response if there is no "If-Modified-Since" header
                """
                response_parts = ["HTTP/1.1 200 OK\r\n",
                                "Last-Modified: " + str(datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))) + "\r\n",
                                "Content-Length: " + str(len(contents)) + "\r\n",
                                "Content-Type: " + file_type + "\r\n\r\n"]
                response = "".join(response_parts)
                connection.sendall(response.encode())

                if (file_type == "text/html"):
                    connection.sendall(contents.encode())
                else:
                    connection.sendall(contents)

                print("Server response:\n")
                print(response)
                self.write_log(address, request_url, "200 OK")
            
        except FileNotFoundError:

            """
            If the file is not found, the server sends a 404 Not Found response
            """
            self.response_file_not_found(connection, request_url, address)

    
    def response_HEAD(self, connection, request, request_url, address):
        """
        This function sends the response to the client for the HEAD request
        Very similar to the response_GET function, but it does not send the contents of the file
        :connection: connection object of the client
        :request: request message from the client
        :request_url: url of the request
        :address: address of the client
        :file_type: type of the file requested
        :file: file object of the requested file
        :contents: contents of the file
        :check_time: time to check if the file is modified
        :file_time: time of the file
        :response_parts: parts of the response message
        :response: response message
        """

        file_type = self.get_file_type(request_url)
        if request_url == "/":
            request_url = "/index.html"
        
        try:

            if (file_type == "text/html"):
                file = open("htdocs" + request_url, "r")
                contents = file.read()
                file.close()
            else:
                file = open("htdocs" + request_url, "rb")
                contents = file.read()
                file.close()

            if ("If-Modified-Since" in request):

                for line in request.split("\n"):
                    if "If-Modified-Since" in line:
                        check_time = line[19:48]
                        break;
                
                check_time = datetime.strptime(check_time, '%a, %d %b %Y %H:%M:%S %Z')
                file_time = datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))

                if (file_time <= check_time):

                    response_parts = ["HTTP/1.1 304 Not Modified\r\n",
                                    "Last-Modified: " + str(datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))) + "\r\n",
                                    "Content-Length: " + str(len(contents)) + "\r\n",
                                    "Content-Type: " + file_type + "\r\n\r\n"]
                    response = "".join(response_parts)
                    connection.sendall(response.encode())

                    print("Server response:\n")
                    print(response)
                    self.write_log(address, request_url, "304 Not Modified")

                else:

                    response_parts = ["HTTP/1.1 200 OK\r\n",
                                    "Last-Modified: " + str(datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))) + "\r\n",
                                    "Content-Length: " + str(len(contents)) + "\r\n",
                                    "Content-Type: " + file_type + "\r\n\r\n"]
                    response = "".join(response_parts)
                    connection.sendall(response.encode())

                    print("Server response:\n")
                    print(response)
                    self.write_log(address, request_url, "200 OK")

            else:

                response_parts = ["HTTP/1.1 200 OK\r\n",
                                    "Last-Modified: " + str(datetime.fromtimestamp(os.path.getmtime("htdocs" + request_url))) + "\r\n",
                                    "Content-Length: " + str(len(contents)) + "\r\n",
                                    "Content-Type: " + file_type + "\r\n\r\n"]
                response = "".join(response_parts)
                connection.sendall(response.encode())

                print("Server response:\n")
                print(response)
                self.write_log(address, request_url, "200 OK")
            
        except FileNotFoundError:
            self.response_file_not_found(connection, request_url, address)
    

    def response_bad_request(self, connection, request_url, address):
        """
        This function sends a 400 Bad Request response to the client
        :connection: connection object of the client
        :request_url: url of the request
        :address: address of the client
        :response_parts: parts of the response message
        :response: response message
        """

        response_parts = ["HTTP/1.1 400 Bad Request\r\n",
                        "Content-Length : 0\r\n",
                        "Content-Type: text/plain\r\n\r\n"]
        response = "".join(response_parts)
        connection.sendall(response.encode())

        print("Server response:\n")
        print(response)
        self.write_log(address, request_url, "400 Bad Request")


    def response_file_not_found(self, connection, request_url, address):
        """
        This function sends a 404 Not Found response to the client
        :connection: connection object of the client
        :request_url: url of the request
        :address: address of the client
        :response_parts: parts of the response message
        :response: response message
        """

        response_parts = ["HTTP/1.1 404 Not Found\r\n", 
                      "Content-Length : 0\r\n",
                      "Content-Type: text/plain\r\n\r\n"]
        response = "".join(response_parts)
        connection.sendall(response.encode())

        print("Server response:\n")
        print(response)
        self.write_log(address, request_url, "404 Not Found")


    def write_log(self, address, file_name, response_type):
        """
        This function writes the request and response to the log file
        :address: address of the client
        :file_name: name of the requested file
        :response_type: type of the response
        :writing_parts: parts of the log message
        :writing: log message
        """

        writing_parts = [str(datetime.now()), ": client", str(address), " requested file ", file_name, " with response ", response_type, "\n"]
        writing = "".join(writing_parts)
        self.server_log.write(writing)
        self.server_log.flush()


    def get_file_type(self, request_url):
        """
        This function returns the type of the file requested
        :request_url: url of the request
        """

        if request_url.endswith(".html"):
            return "text/html"
        elif request_url.endswith(".jpg"):
            return "image/jpg"
        elif request_url.endswith(".png"):
            return "image/png"
        else:
            return "text/plain"
        
        
if __name__ == "__main__":
    """
    The main function runs the server
    """

    server = HTTPServer(SERVER_HOST, PORT)
    server.run()