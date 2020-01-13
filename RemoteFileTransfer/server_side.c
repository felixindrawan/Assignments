#include <arpa/inet.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <ctype.h>

#define CHARLIMIT 4096
void error (const char *msg){
	perror(msg);
	exit(1);
}

int main(int argc, char **argv){
	if (argc < 2) {
		fprintf(stderr, "Invalid argument\n");
		exit(1);
	}
	
	int sockfd; //File descriptor
	int port_number;
	struct sockaddr_in server_addr; //Server address
	
	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
		error("Socket not created");
	}
	
	memset(&server_addr, 0, sizeof(struct sockaddr_in));
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = INADDR_ANY;
	if(sscanf(argv[1], "%d", &port_number) != 1) {
		error("Invalid port number");
	}
	server_addr.sin_port = htons(port_number);
	
	if (bind(sockfd, (struct sockaddr *) &server_addr, sizeof(struct sockaddr_in)) < 0){
		error("Binding failed");	
	}
	listen(sockfd, 5); //Max client is 5;
	
	while(1){
		int newsockfd;
		int n;
		char buffer[CHARLIMIT];
		
		if ((newsockfd = accept(sockfd, NULL, NULL)) < 0){
			error("Accepting failed");
		}
		
		//Get file name from client
		n = read(newsockfd, buffer, CHARLIMIT);
		if (n != 1){
			close(newsockfd);
			error("Reading failed");
		}
		
		int user_fd; //File descriptor for user's file
		if((user_fd = open(buffer, 0_RDONLY)) == -1){ //If can't open/send, send 0
			buffer[0] = 0;
			write(newsockfd, buffer, 1);
		} else { //If can open/send, send 1
			buffer[0] = 1;
			write(newsockfd, buffer, 1);
			
			//Writting and sending file
			while ((n = read(user_fd, buffer, CHARLIMIT)) > 0){
				if (n != write(newsockfd, buffer, n)){ //Error in sending file to client
					break;
				}
			}
			
			close(user_fd);
		}
		close(newsockfd);
		
		return 0;
	}
}