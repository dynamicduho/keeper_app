# i'm hosting a SQL server on google cloud platform using mySQL
# thus, i need to use mysql.connector as connector for the mySQL server (pyodbc for MS SQL server)
# tutorial for connecting to cloud SQL in python: https://www.youtube.com/watch?v=f8vLmgRWd00
# tim's tutorial(hell yeah): https://www.youtube.com/watch?v=3vsC05rxZ8c
# connect to google database guide: https://cloud.google.com/sql/docs/mysql/connect-admin-ip

# import pyodbc # imports our DB connector
import mysql.connector

db = mysql.connector.connect(
    host="34.130.177.4",
    user="root",
    password="", # delete this before upload to github
    database='helloWorld'
)

cursor = db.cursor()

cursor.execute("SET autocommit = 1;")

# cursor.execute("CREATE DATABASE helloWorld") # python connected to server successfully!!

# cursor.execute("CREATE TABLE hackers (hacker_id INT PRIMARY KEY, name VARCHAR(20), major VARCHAR(20));") # construct table
cursor.execute("INSERT INTO hackers VALUES(2, 'rose', 'astronomy');")
cursor.execute("SELECT * FROM hackers;")
# cursor.execute("DELETE FROM hackers WHERE hacker_id = 1;")

print(cursor.fetchall())
# SQL queries working properly now!!

# SQL notes:
# meaning of -> in mysql shell: https://superuser.com/questions/160197/what-does-an-arrow-symbol-mean-on-the-command-line/160201