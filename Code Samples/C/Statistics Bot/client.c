#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>

/*
  The main standard library functions needed to get this working are
 - socket() 
 - connect() 

 You'll also need to know:
 host = 128.119.243.147
 port = 27994
 SSID = tbeaudin@umass.edu
*/


int main(int argc, char **argv) {
    char *flag;
    int socket_fd = socket(AF_INET, SOCK_STREAM, 0); // TODO write your socket creation code here, using socket()
    char *hostIP = argv[3];
    int port = atoi(argv[2]);
    char *sid = argv[1];
    if (socket_fd < 0) {
        printf("Error creating socket\n");
        exit(-1);
    }
    //printf("Socket successfully created\n");

    struct sockaddr_in server_address;
    bzero((char *)&server_address, sizeof(server_address));
    /* set server_address attributes here
     * sin_family, sin_port, sin_addr, sin_zero need all be set
     * hint: you may want to look at htons(), htonl(), ntohs(), ntohl(),
     * memset(), bzero(), and inet_pton() functions
     */
     server_address.sin_family = AF_INET;
     server_address.sin_port = htons(port);
     
     if (inet_pton(AF_INET, hostIP, &server_address.sin_addr) < 1) {
        printf("Error storing IP adress\n");
        exit(1);
     }
     //printf("IP adress (%s) and port (%d) stored\n", inet_ntoa(server_address.sin_addr), port);

    /* 
     * Then, once server_address is configured, call connect()
     * and store its return value in status.
     */
    int status = connect(socket_fd, (struct sockaddr *)&server_address, sizeof(server_address)); // TODO insert correct arguments here

    if (status < 0) {
        printf("Connection unsuccessful");
        close(socket_fd);
        exit(-1);
    }

    //printf("Connection successful\n");

    // Build the message to send to the server
    char *message = (char *) malloc(256);
    sprintf(message, "cs230 HELLO %s\n", sid); // sprintf(&buffer, "these strings %s will be %s combined", &str1, &str2) stores string "these strings str1 will be str2 combined" into buffer

    //printf("%s", message);

    // Now send it on the socket_fd
    status = send(socket_fd, message, strlen(message), 0); // TODO insert correct arguments here

    // don't forget to check status for errors
    if (status < 0) {
        printf("\nError sending string cs230 Hello tbeaudin@umass.edu to server\n");
    }
    //printf("(Sent succesfully to server)\n");
    

    // then, one-by-one, start doing each of the following tasks:

    // receive the response from the server -- first allocate
    // a char* buffer to receive the message
    char* buffer = (char *)malloc(8192);
    char *token;
    while(1) {
        // then use recv() to receive the message into that buffer
        status = recv(socket_fd, buffer, 8192, 0);
        if (status < 0) {
            printf("Error getting message from server\n");
        }
        //printf("Bytes recieved: %d\n", status);
        //printf("Received message: %s\n", buffer);

        // then split the received message and parse the ints
        token = strtok(buffer, " ");
        token = strtok(NULL, " ");

        // Check to see if the server sends us the key
        if (strcmp(token, "STATUS") != 0) {
            printf("%s\n", token);
            break;
        }

        // Determine the operation to perform
        token = strtok(NULL, " ");
        int operationType; // 1 = min, 2 = max, 3 = median

        if (strcmp(token, "min") == 0) {
            operationType = 1;
        }
        else if (strcmp(token, "max") == 0) {
            operationType = 2;
        }
        else {
            operationType = 3;
        }

        // Capture the inputs into an array
        int inputs[5]; 
        int i;

        for(i=0; i < 5; i++) {
            token = strtok(NULL, " ");
            inputs[i] = atoi(token);

            // Sort elements as they are added
            int j;
            int temp;
            for(j=i; j > 0; j--) {
                if (inputs[j] < inputs[j-1]) {
                    temp = inputs[j];
                    inputs[j] = inputs[j-1];
                    inputs[j-1] = temp;
                }
            }
        }

        // print out sorted list
        /*for(i=0; i<5; i++) {
            printf("input %d: %d\n", i+1, inputs[i]);
        } */

        // then do math
        int answer;
        if (operationType == 1) {
            answer = inputs[0];
        }
        else if (operationType == 2) {
            answer = inputs[4];
        }
        else {
            answer = inputs[2];
        }

        // then format the correct response (maybe print it, too)
        sprintf(message, "cs230 %d\n", answer);
        //printf("%s", message);

        // then send it to the server on the socket
        status = send(socket_fd, message, strlen(message), 0); // TODO insert correct arguments here

        // don't forget to check status for errors
        if (status < 0) {
            printf("\nError sending string cs230 Hello tbeaudin@umass.edu to server\n");
        }
        //printf("(Sent succesfully to server)\n");

        // then wrap it all up in a loop

        // make sure to stop when you get the flag!
    }

    // then add the rest: parse argv[] as required by the project directions,
    // use the passed email, host, and port, etc.

    // you won't get to all of this in lab! Just do one part at a time, and
    // get it working, before you move to the next part.
    free(message);
    free(buffer);
    return 0;
}