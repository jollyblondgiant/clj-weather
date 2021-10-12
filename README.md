# compokedex

clone this directory, then cd into the newly created folder

make sure you have leiningen installed:
$ sudo apt-get install leiningen

once it is installed, run
$ lein ring server.

a browser window will open to localhost:3000

Then navigate to localhost:3000/{lat}/{lng}.

alternatively, run:
$ curl 127.0.0.1:3000/{lat}/{lng}

Libraries used:
compojure, ring: for capture lat/lng as url params
clj-http: helps with sending api calls and parsing responses
clojure.data.json: for parsing json
