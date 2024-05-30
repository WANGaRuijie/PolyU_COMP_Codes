# import the socket library 
import socket
# create a socket object
clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# define the server's name and port on which you want to connect 
serverName = '127.0.0.1'
serverPort = 43808
# connect to the server 
clientSocket.connect((serverName, serverPort))
print("TCP connection established between:")
print("Source IP:", clientSocket.getsockname()[0])
print("Source port:", clientSocket.getsockname()[1])
print("Destination IP:", clientSocket.getpeername()[0])
print("Destination port:", clientSocket.getpeername()[1])
# receive data from the server and decode to get the string. 
sentence = clientSocket.recv(1024).decode()
print ("from server:", sentence)

password = input("Enter the 5-digit password: ")
clientSocket.send(password.encode())
response = clientSocket.recv(1024).decode()
print("From server:", response)

# close the connection 
clientSocket.close()