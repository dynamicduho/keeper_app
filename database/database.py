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

# how to deploy your python FLASK app to heroku: https://www.youtube.com/watch?v=Li0Abz-KT78

# import pyodbc # imports our DB connector
import mysql.connector

from flask import Flask, jsonify, request # we use flask to handle our web requests

from dotenv import load_dotenv # import dotenv since we store our DB password in dotenv
import os # import os for getting environment variable
load_dotenv()

app = Flask(__name__)

DBpasswd = os.getenv("DB_PASSWORD")# input("enter database password: ")
# DBpasswd = os.environ.get("DB_PASSWORD")

db = mysql.connector.connect(
        host="34.130.177.4",
        user="root",
        password= DBpasswd, # delete this before upload to github
        database='receiptArchive'
    )

cursor = db.cursor()

cursor.execute("SET AUTOCOMMIT = true;") # turns autocommit on so that your changes to the databases are commited without having to explicitly commit a change

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
    cursor.execute("SELECT * FROM transactions;")
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
    return table

# getReceipts: returns the whole transactions table as a json file
@app.route("/getReceipts")
def getReceipts():
    cursor.execute("SELECT * FROM transactions;")
    table = jsonify(cursor.fetchall())
    return table

@app.route("/") # route is just the URL you want to access
def index():
    return "hello, world"

if __name__ == "__main__":
    app.run(debug=True) # i can't use flask run in cmd for whatever reason, i have to use "python -m flask run"

# SQL notes:
# meaning of -> in mysql shell: https://superuser.com/questions/160197/what-does-an-arrow-symbol-mean-on-the-command-line/160201
# varchar(max) dynamically allocates storage so it scales the data you store

# http request notes:
# scheme don't have a registered handler” error: http:// is probably missing