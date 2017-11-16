Functional style impala DB access
=================================

Purpose
-------

This api demonstrate a possible functional style 
Impala DB access API.

Depencencies
------------

### Cloudera JDBC4 Driver

The main dependency is the proprietary Impala JDBC4
driver from Cloudera. 

See:

https://www.cloudera.com/downloads/connectors/impala/jdbc/2-5-5.html


It is composed from the main jars and their dependencies.

The jars that are Cloudera proprietary have been added to
the `./this-project/lib` directory.

The other jars are classical dependencies in the `build.sbt` of this project.

### Hikari

I originally needed a connection pool to run mt example 
and [Hikari](https://github.com/brettwooldridge/HikariCP)
seemed to me the best as so it is used as a dependencies 
but it is used as a DataSource in the code so it may be changed 
easily.

The url in `./src/test/resources/hikari.properties` 
need adaptation to correspond to your own Impala hosts.

Note: 
The single threaded example do NOT use Hikari so the url need change also 
(`./src/test/com/ss/dao/impala/test/ClouderaJDBCImpalaExample.scala`).

Security: Kerberos
------------------

The Impala (Kudu in fact) table I needed to access were 
behind Kerberos so I needed the last lines of the `sbtops` 
to automatically run the ticket granting procedure via jaas.

If you wish to use Kerberos this way you will have to rename   
`sbtopts` to `.sbtopts`.