# temporal-user-registration-demo
Getting stated
Create an env file with the following

```
 TEMPORAL_VERSION=latest 
 TEMPORAL_UI_VERSION=latest
 MYSQL_VERSION=8.0
 ```
Then run the below docker command in the root, replace path-to-your.env-file with the env-file location you created above
> docker compose --env-file path-to-your.env-file up 

Start the api application and the worker application in the code

