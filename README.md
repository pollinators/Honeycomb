Pollinator Survey Research
---


##### This is the home of our industry project for CMPEN 482W.

The project is a collaborative work with 
Medora Ebersole (from the College of Arts & Architecture at Penn State)
, Ted Papaioannou, Szymon Cymbalski, Mario Leone, and Manjot Singh, and others. We hope to create an application to collect research about pollinators world-wide.

---

## TODO:

Working on the database schema since I saw some limitations online. I found a good schema for a
database that works with surveys of any type.

This is the schema I found online that is general enough for us to expand. I created a MySQL
database with this scheme and also ported it to sqlite on the app.

![Survey Schema](http://i.stack.imgur.com/06AEQ.png)

To prepopulate the database with questions, we can supply a JSON object. Then for any future
additions to the survey database, remotely, we can just submit and parse a JSON object in the same
way.
                      
