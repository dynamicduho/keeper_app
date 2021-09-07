# i'm hosting a SQL server on google cloud platform using mySQL
# thus, i need to use mysql.connector as connector for the mySQL server (pyodbc for MS SQL server)
# tutorial for connecting to cloud SQL in python: https://www.youtube.com/watch?v=f8vLmgRWd00
# tim's tutorial(hell yeah): https://www.youtube.com/watch?v=3vsC05rxZ8c
# connect to google database guide: https://cloud.google.com/sql/docs/mysql/connect-admin-ip

# i initially thought of having a table of user accounts and then a separate table for the list
#  of receipts for each account. this idea is too complicated. instead, it's better to create
#  a table for accounts and then put the transactions of all accounts into the same table and
#  have the accounts as the foreign key

# it's good practice to install a virtual environment (pip install virtualenv)
#  because it makes sense that all the requirements the app needs is contained in
# the project itself so you can transfer the project around and others can just install
# the requirements and use your app

# retarded microsoft again: sometimes, there is erroneous python installed in windows apps. and
#  its path variable goes in front of the python you install on your own. this causes python
#  to not run correctly. to fix this, delete those files (force delete if necessary)
#  also, when you reset environment variables, make sure that you restart CMD otherwise, it
#  won't refresh

# you have to "set FLASK_APP = yourAppName" before you try to run your flask app
# then you just have to python yourAppName.py

# web hosting is as simple as building a webapp or html document on your computer and connecting it to the internet.
# 1. how to deploy your python FLASK app to heroku: https://www.youtube.com/watch?v=Li0Abz-KT78
#     since hosting services like heroku and google cloud are hard to deal with, i'm moving all our hosted
#     apps to CSC machines. i plan to host it on caffeine. to request to use mysql server, use ceo
# 2. how to host your website on linux server: https://www.youtube.com/watch?v=wFjYzhkEBys, https://www.youtube.com/watch?v=goToXTC96Co
# 3. how to host your website on a server where you don't have full permissions: using others' computer really is a pain, you don't get full access and you get permission errors everywhere.
#     so you have to make workarounds, contact the system admin, and tamper with system level stuff as less as possible (this is where virtual env really comes in handy)

# on a server, you want to run your app in a virtual environment so that your dependencies are all contained within your project folder.

# how to host a webapp on a linux server on your own instead of heroku: it seems that static web hosting
#  and dynamic website hosting seem to be quite different. static websites is like you go on the ip address then
#  traverse through the directory of the ip address like a local directory so you can get them through "csclub.uwaterloo.ca/~username". On the other hand,
# to deploy a flask app on linux server, you need to run it using flask and set the host to 0.0.0.0 so it's not just
#  a loopback on localhost, and specify a port for it to listen on. then, you should be able to access it through specifying
#  ip address and port. however, sometimes, it can be blocked by firewall on your linux server. to bypass that firewall, you can
#  forward your port to an allowed port on your network (e.x. SSH tunneling), you can also reverse proxy through your web space
#  , or use VPN.
# you can use curl, nc, telnet, netstat -lntp to test your webapp status on linux cli
# localhost vs 127.0.0.1 vs 0.0.0.0: https://stackoverflow.com/questions/20778771/what-is-the-difference-between-0-0-0-0-127-0-0-1-and-localhost

# what is port forwarding: https://www.youtube.com/watch?v=2G1ueMDgwxw
# SSH tunneling: https://www.youtube.com/watch?v=N8f5zv9UUMI
#  how to remove an SSH forwarding: exit the SSH connection, or use "ps aux | grep ssh", then "kill <id>"

# how i bypassed the campus firewall for this flask app to host on csclub machines: 
#  i hosted my app on caffeine because of the mysql server, then used ssh tunneling to forward to a open tcp port on corn-syrup.
#  so i first flask run my app on caffeine in a screen. then i remote port forward it to corn-syrup. however, we are not done yet.
#  ssh by default binds remote port forwardins to the loopback address to prevent you from bypassing the firewall. you can change
#  GatewayPorts to allow remote port forwarding to any address but this requires root permissions. instead, i dealt with it using
#  another local port forwarding. so i did ssh -R 28400:localhost:5000 user@corn-syrup.csclub.uwaterloo.ca then I did 
#  ssh -g -L 28401@localhost:28400 user@corn-syrup.csclub.uwaterloo.ca at the remote: https://serverfault.com/questions/997124/ssh-r-binds-to-127-0-0-1-only-on-remote
# csclub firewalls: https://wiki.csclub.uwaterloo.ca/Firewall#General_Use
# csclub machines: https://wiki.csclub.uwaterloo.ca/Machine_List
# csclub webhosting: https://wiki.csclub.uwaterloo.ca/Web_Hosting

# when you activate virtual environment for your project. for some reason, your mysql.connector will
#  have the error of mysql module cannot be found. i don't know why tho
#  you deactivate a virtual environment by simply typing "deactivate"

# import pyodbc # imports our DB connector
import mysql.connector

from flask import Flask, jsonify, request # we use flask to handle our web requests

from dotenv import load_dotenv # import dotenv since we store our DB password in dotenv
import os # import os for getting environment variable
load_dotenv()

import time
from threading import Thread # imports and time and thread to regularly send the database some query so it doesn't cut connection because of idling

app = Flask(__name__)

DBuser = os.getenv("USER")
DBdatabase= os.getenv("DATABASE")
DBpasswd = os.getenv("DB_PASSWORD")# input("enter database password: ")
# DBpasswd = os.environ.get("DB_PASSWORD")

db = mysql.connector.connect(
        host="localhost",
        user= DBuser,
        password= DBpasswd, # delete this before upload to github
        database= DBdatabase
    )

cursor = db.cursor()

cursor.execute("show variables like '%timeout%';")
print(cursor.fetchall())

cursor.execute("SET AUTOCOMMIT = true;") # turns autocommit on so that your changes to the databases are commited without having to explicitly commit a change

# this code regularly sends a dummy query to the mySQL database so that the database won't cut off connection because of idling
# notes: the code, cursor.execute("SET SESSION wait_timeout=10;"), is able to replicate the lost connection error in less than 10 seconds. this means 
#  that it's indeed the server cutting connection after idling for too long that caused the lost connection error
# notes: # it seems that this thread will be run twice since i have reloader activated for my flask, it's not a big deal tho
# note: interactive vs wait timeout in mySQL: https://serverfault.com/questions/375136/what-is-the-difference-between-wait-timeout-and-interactive-timeout
def dummy_query():
    while(True):
        time.sleep(5 * 60 * 60)
        cursor.execute("SELECT CURRENT_USER();")

keep_connection = Thread(target=dummy_query)
keep_connection.daemon = True # terminates the thread with the main thread if main thread is terminated
keep_connection.start()

# creates the transactions table if it doesn't exist
# notes: https://www.mysqltutorial.org/mysql-create-table/
#  VARCHAR(21844): max # of chars for utf8 varchars, but 
#  it seems that TEXT is better for specifying large variable 
#  strings, since it deals with size limits better
cursor.execute("CREATE TABLE IF NOT EXISTS transactions (transaction_id INT AUTO_INCREMENT PRIMARY KEY, receipt TEXT NOT NULL);") 

@app.route("/testSQL") # route is just the URL you want to access
def testSQL():
    # cursor.execute("SET autocommit = 1;")

    # cursor.execute("CREATE DATABASE helloWorld") # python connected to server successfully!!

    # cursor.execute("CREATE TABLE hackers (hacker_id INT PRIMARY KEY, name VARCHAR(20), major VARCHAR(20));") # construct table
    # cursor.execute("INSERT INTO hackers VALUES(2, 'rose', 'astronomy');")
    # cursor.execute("SELECT * FROM hackers;")
    # cursor.execute("DELETE FROM hackers WHERE hacker_id = 1;")

    # print(cursor.fetchall())
    # SQL queries working properly now!!
    cursor.execute("SELECT receipt FROM transactions;")
    table = str(cursor.fetchall())
    return table

# uploadReceipt: takes a parameter for the receipt to upload to database
# requires: receipt must be a valid string
#           submit a post request to send receipt over
# note: sql insertions - https://www.w3schools.com/python/python_mysql_insert.asp
@app.route("/uploadReceipt", methods=["POST"])
def uploadReceipt():
    receipt = request.form.get("receipt")
    cursor.execute("INSERT INTO transactions (receipt) VALUES(%s);", (receipt,))
    cursor.execute("SELECT * FROM transactions;")
    table = jsonify(cursor.fetchall())
    # table.headers.add('Access-Control-Allow-Origin', '*') # to avoid CORS error on the frontend (javascript in particular)
    return table

# getReceipts: returns the whole transactions table as a json file
@app.route("/getReceipts")
def getReceipts():
    cursor.execute("SELECT receipt FROM transactions;")
    table = jsonify(cursor.fetchall())
    # table.headers.add('Access-Control-Allow-Origin', '*') # to avoid CORS error on the frontend (javascript in particular)
    return table

@app.route("/") # route is just the URL you want to access
def index():
    return "Hello world"

@app.after_request
def after_request(response):
  response.headers.add('Access-Control-Allow-Origin', '*')
  response.headers.add('Access-Control-Allow-Headers', 'Content-Type,Authorization,Accept,Access-Control-Allow-Origin')
  response.headers.add('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS')
  return response

if __name__ == "__main__":
    app.run(debug=True) # i can't use flask run in cmd for whatever reason, i have to use "python -m flask run"
                        # the reason why you can't use flask run is because you are not running your virtualenv, plz activate your virtualenv before you flask run

# SQL notes:
# meaning of -> in mysql shell: https://superuser.com/questions/160197/what-does-an-arrow-symbol-mean-on-the-command-line/160201
# varchar(max) dynamically allocates storage so it scales the data you store

# http request notes:
# scheme don't have a registered handler error: http:// is probably missing

# turns out google cloud have to whitelist the IP of your app in order to let it access the database
#  and because heroku hosted apps has dynamic IP addresses, i have to whitelist all IP addresses to
#  allow it to work properly. this is a security hazard tho, keep it in mind
#  https://stackoverflow.com/questions/22373279/google-cloud-sql-heroku

# this app is not built as a full fledged flask app that has templates and static folders and return htmls
#  because I want it to work the same way for both mobile app frontend and the website frontend

# how git sync your web app code to your server: https://www.youtube.com/watch?v=9qIK8ZC9BnU

# when you run your app in development mode (export FLASK_ENV=development) in the server, the app will automatically
#  reload itself if you updated the source code of the flask app (e.x. pushing a new change to the server)
#  https://stackoverflow.com/questions/16344756/auto-reloading-python-flask-app-upon-code-changes

# also, we store all our project code into the same git repository. however, to host the website_frontend and database, i need to host them as separate repositories. so what i did is set up a git repository in both folders (in other words, set up two git repositories within a git repository), then i used gitignore to ignore those git repositories.
# i named the remote uw_server for the git repositories
# sometimes git gives me problems when i have nested git repositories, to deal with this, first take out the .git in the repository within, then upload the code, then add the .git back in