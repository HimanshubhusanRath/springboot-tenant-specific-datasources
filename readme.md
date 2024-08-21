## Setup:
  * Data sources configured:
    * Database-1 (india)
    * Database-2 (china)

  * URLs to test:
    * http://localhost:8080/users/1?tenant=india ---> Gets the data from tenant-1 (INDIA) database.
    * http://localhost:8080/users/1?tenant=china ---> Gets the data from tenant-2 (CHINA) database.

