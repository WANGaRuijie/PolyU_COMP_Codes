# import the socket library 
import socket
# create a socket object
serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
print("socket successfully created")
serverPort = 43808
# bind to the port
# we have not typed any ip in the ip field, instead we have inputted an empty string.
# this makes the server listen to requests coming from other computers on the network 
serverSocket.bind(('', serverPort))
print("socket binded to %s" %(serverPort))
# put the socket into listening mode 
serverSocket.listen(5)
print ("socket is listening")
# a forever loop until we interrupt it or an error occurs while True:
while True:
    connectionSocket, addr = serverSocket.accept()
    print ('got connection from', addr )
    sentence='thank you for connecting'
    connectionSocket.send(sentence.encode())

    password = connectionSocket.recv(1024).decode()
    if password == "03808":  
        response = "Your password is correct!"
    else:
        response = "Your password is incorrect!"
    connectionSocket.send(response.encode())

    connectionSocket.close()
    break
