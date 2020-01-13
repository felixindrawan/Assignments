/*
argv[0] filename
argv[1] server ip adress
argv[2] portno
argv[3] requested filename (server)
argv[4] filename to save to (client)
*/

#include <arpa/inet.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <ctype.h> //FOR isspace()

#define CHARLIMIT 4096
void error (const char *msg){
	perror(msg);
	exit(1);
}

int main(int argc, char **argv){
	int sockfd; //File descriptor to server
	int port_number;
	struct sockaddr_in server_addr; //Server address
	
	if (argc < 5) {
		fprintf(stderr, "Invalid arguments\n");
		return 1;
	}
	
	memset(&server_addr, 0, sizeof(struct sockaddr_in));
	server_addr.sin_family = AF_INET;
	if (!inet_pton(AF_INET, argv[1], &server_addr.sin_addr)){
		error("Invalid address");
	}
	
	if(sscanf(argv[2], "%d", &port_number) != 1) {
		error("Invalid port number");
	}
	server_addr.sin_port = htons(port_number);

	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0){
		error("Error opening socket");
	}
	if (connect(sockfd, (Struct sockaddr *)&server_addr, sizeof(struct sockaddr_in))){
		error("Connecting failed");
	}
	
	int n;
	char buffer[CHARLIMIT];
	
	if ((n = read(sockfd, buffer, 1)) != 1){
		error("Reading failed");
	}
	if (buffer[0] == 0) {
		error("Server error in sending");
	}
	
	int user_fd = open(argv[4], O_WRONLY | O_CREAT | O_TRUNC, 0666);
	if (user_fd < 0){
		error("Opening file failed");
	}
	
	while ((n = read(sockfd, buffer, CHARLIMIT)) > 0){
		write(user_fd, buffer, n);
	}
	close(userfd);
	close(sockfd);
	
	return 0;
}