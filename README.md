# ebanx-challenge
Ebanx Challenge

use

	https://ipkiss.pragmazero.com/
	https://ngrok.com/
	
docs swagger

	http://localhost:8080/swagger-ui/index.html

ngrok

	initialize spring application in port 8080
	install ngrok and creat acooount
	registre it in plataform with key 
	run in cmd after start spring boot application
	expose ngrok service in cmd
	ngrok http http://localhost:8080	
	see line generated Forwarding use it in ipkiss
	


ipkiss automate testes suite:

https://ipkiss.pragmazero.com/
* use ngrok generated forwarding url local in UTL input and test it!

	
______


Teste overall instruction


	EBANX Software Engineer Take-home assignment
		 
	
	Implement the following API in the simplest way you can.
	 
	
	Durability *IS NOT* a requirement, that is, you don’t need to use a database or persistence mechanism.
	 
	
	The main goal of this exercise is to create a common ground to conduct the interview process.
	 
	
	The API consists of two endpoints, GET /balance, and POST /event. Using your favorite programming language, build a system that can handle those requests, publish it on the internet, and test it using our automated test suite.
		 
	
	After getting green light from our test suite, please submit bellow the source code for your solution to continue the interview process.
		 
	
	Keep in mind that
	There is no hidden agenda, if you code passes the tests, and you are happy about it:  you are done;
	Pay attention to the package/directory structure, naming and encapsulation;
	Separate your business logic from the HTTP transport layer;
	Keep your code simple, do not try to anticipate anything that is not part of the spec;
	Keep your code malleable, we may ask for modifications;
	AGAIN, Keep your code malleable, we may ask for modifications;
	Use version control, we would love to see your step-by-step process;
	Take your time, don’t rush it;
	
