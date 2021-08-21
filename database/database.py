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

# import pyodbc # imports our DB connector
import mysql.connector

from flask import Flask # we use flask to handle our web requests

app = Flask(__name__)

db = mysql.connector.connect(
        host="34.130.177.4",
        user="root",
        password="", # delete this before upload to github
        database='helloWorld'
    )

cursor = db.cursor()

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
    cursor.execute("SELECT * FROM hackers;")
    table = str(cursor.fetchall())
    return table

@app.route("/") # route is just the URL you want to access
def index():
    return "hello, world"

if __name__ == "__main__":
    app.run(debug=True) # i can't use flask run in cmd for whatever reason, i have to use "python -m flask run"

# SQL notes:
# meaning of -> in mysql shell: https://superuser.com/questions/160197/what-does-an-arrow-symbol-mean-on-the-command-line/160201
# varchar(max) dynamically allocates storage so it scales the data you store