# Teaching-HEIGVD-RES-2017-Labo-SMTP
## Description
This application is used to send a prank mail between people defined in a list of users between them. The application will form groups depending on the number of victims and the number of groups the user decides. A mail randomly chosen for each group will be sent with one of the victims in the group as the sender and the others as the receiver.

## Setup Instructions
the simplest way to use the programm is to use the files in the dist folder which contains the files needed to run the application.

* Change the *mails.txt* file with mails you want to send, they will be picked at random when you use the application. All you have to do is make sure you split the mails correctly using the separator *####*. Anything after it in the line will be ignored
```
First line for each mail is the subject
After, everything is content of the mail
You can put anything

But if you want to split to the next mail, use the delimiter
####
There goes the second mail
If you want, you can put something after the delimiter
#### this will not be in any mail
And we go with the third

There you go for the examples
```

* Then, change the *victims.txt* file with a victim's address in each line

```
usera@mail.com
user.B@address.mail
...
```

* finally, launch the application by command line with the parameters you want to set

```
java -jar LaboSMTP.jar -a 127.0.0.1 -p 25
```
## parameters
### -a|-A
#### _[default : localhost]_
sets the address of the smtp server you want to connect to
### -p|-P
#### [default : 2525]
sets the port you want to communicate with
### -g|-G
#### [default : 3]
sets the number of groups you want to set. A group is composed of minimum one sender and two receivers. The more groups you use, the less people there will be in a group. The application tries to make every group with different victims. If it's not possible, it will be completely random with the minimum of victims in each group
